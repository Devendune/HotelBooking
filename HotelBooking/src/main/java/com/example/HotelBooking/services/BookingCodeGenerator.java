package com.example.HotelBooking.services;

import com.example.HotelBooking.entities.Booking;
import com.example.HotelBooking.entities.BookingReference;
import com.example.HotelBooking.repository.BookingReferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BookingCodeGenerator
{
    private final BookingReferenceRepository bookingReferenceRepository;

    public String generateBookingReference()
    {
        String bookingReference;
        do {
            bookingReference=generateRandomAlphaNumericCode(10);
        }
        while(isBookingReferenceExist(bookingReference));
        return bookingReference;
    }
    private String generateRandomAlphaNumericCode(int inputLength)
    {
        String characters="ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        Random random=new Random();

        StringBuilder stringBuilder=new StringBuilder(inputLength);
        for(int counter=0;counter<inputLength;counter++)
        {
            int index=random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }
        return stringBuilder.toString();
    }

    private boolean isBookingReferenceExist(String bookingReference)
    {
        return bookingReferenceRepository.findByReferenceNo(bookingReference).isPresent();
    }

    private void saveBookingReferenceToDatabase(String bookingReference)
    {
        BookingReference saveBookingReference= BookingReference.builder().referenceNo(bookingReference)
                .build();

        BookingReference savedBookingReference=bookingReferenceRepository.save(saveBookingReference);
    }

}
