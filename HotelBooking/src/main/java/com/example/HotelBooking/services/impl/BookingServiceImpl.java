package com.example.HotelBooking.services.impl;

import com.example.HotelBooking.dtos.BookingDTO;
import com.example.HotelBooking.dtos.NotificationDTO;
import com.example.HotelBooking.dtos.Response;
import com.example.HotelBooking.entities.Booking;
import com.example.HotelBooking.entities.BookingReference;
import com.example.HotelBooking.entities.Room;
import com.example.HotelBooking.entities.User;
import com.example.HotelBooking.enums.BookingStatus;
import com.example.HotelBooking.enums.PaymentStatus;
import com.example.HotelBooking.exceptions.InvalidBookingStateAndDateException;
import com.example.HotelBooking.exceptions.NotFoundException;
import com.example.HotelBooking.repository.BookingRepository;
import com.example.HotelBooking.repository.RoomRepository;
import com.example.HotelBooking.services.BookingCodeGenerator;
import com.example.HotelBooking.services.BookingService;
import com.example.HotelBooking.services.NotificationService;
import com.example.HotelBooking.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService
{
    private final ModelMapper modelMapper;
    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final BookingCodeGenerator bookingCodeGenerator;
    private final RoomRepository roomRepository;

    @Override
    public Response createBooking(BookingDTO bookingDTO)
    {
        User currentUser=userService.getCurrentLoggedInUser();
        Room room=roomRepository.findById(bookingDTO.getId())
                        .orElseThrow(()->new NotFoundException("Room not found"));
        if(bookingDTO.getCheckInDate().isBefore(LocalDate.now()))
            throw new InvalidBookingStateAndDateException("check in date cannot be before current one");

        if(bookingDTO.getCheckOutDate().isBefore(bookingDTO.getCheckInDate()))
            throw new InvalidBookingStateAndDateException("checkout data cannot be before checkin date");

        if(bookingDTO.getCheckOutDate().isEqual(bookingDTO.getCheckOutDate()))
            throw new InvalidBookingStateAndDateException("Checkin data cannot be equal to checkout date");

        boolean roomAvailable=bookingRepository.isRoomAvailable(room.getId(),bookingDTO.getCheckInDate(),bookingDTO.getCheckOutDate());
        if(!roomAvailable)
            throw new NotFoundException("No room is available for booking");

        BigDecimal totalPrice=calculateTotalPrice(room,bookingDTO);
        String bookingReference=bookingCodeGenerator.generateBookingReference();
        Booking booking=new Booking();
        booking.setUser(currentUser);
        booking.setRoom(room);
        booking.setCheckInDate(bookingDTO.getCheckInDate());
        booking.setCheckOutDate(bookingDTO.getCheckOutDate());
        booking.setTotalPrice(totalPrice);
        booking.setBookingReference(bookingReference);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        booking.setBookingStatus(BookingStatus.BOOKED);

        Booking savedBooking=bookingRepository.save(booking);

        NotificationDTO notificationDTO=NotificationDTO
                .builder()
                .recipient("devendunegi06@gmail.com")
                .subject("Booking confirmation")
                .body("Booking is done bro")
                .build();

        notificationService.sendEmail(notificationDTO);

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Booking is successfuly")
                .booking(bookingDTO)
                .build();
    }

    @Override
    public Response findBookingByReferenceId(String bookingReference)
    {
        Optional<Booking> booking=bookingRepository.findByBookingReference(bookingReference);
        BookingDTO bookingDTO=modelMapper.map(booking.get(),BookingDTO.class);
        return Response.builder()
                .booking(bookingDTO)
                .status(HttpStatus.OK.value())
                .message("The Booking was found using reference id")
                .build();
    }



    private BigDecimal calculateTotalPrice(Room room, BookingDTO bookingDTO)
    {
        BigDecimal pricePerNight=room.getPricePerNight();
        long days= ChronoUnit.DAYS.between(bookingDTO.getCheckInDate(),bookingDTO.getCheckOutDate());
        return pricePerNight.multiply(BigDecimal.valueOf(days));
    }
}
