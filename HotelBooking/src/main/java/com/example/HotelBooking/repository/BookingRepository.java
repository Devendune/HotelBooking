package com.example.HotelBooking.repository;

import com.example.HotelBooking.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Book;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long>
{
    List<Booking> findByUserId(Long id);

    Optional<Booking> findByBookingReference(String bookingReference);

    @Query("""
            select CASE WHEN COUNT(b)=0 THEN true ELSE false END
            FROM Booking b
            where b.room.id=:roomId
            AND :checkInDate<=b.checkInDate
            AND :checkOutDate >= b.checkOutDate
            AND b.bookingStatus IN ('BOOKED','CHECKED_IN')
           """
    )
    boolean isRoomAvailable(@Param("roomId") String roomId,
                            @Param("checkInDate")LocalDate checkInDate,
                            @Param("checkOutDate") LocalDate checkOutDate);

}
