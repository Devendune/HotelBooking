package com.example.HotelBooking.repository;

import com.example.HotelBooking.entities.BookingReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingReferenceRepository extends JpaRepository<BookingReference,Long>
{
    Optional<Boolean> findByReferenceNo(String bookingReference);

}
