package com.example.HotelBooking.services;

import com.example.HotelBooking.dtos.BookingDTO;
import com.example.HotelBooking.dtos.Response;

public interface BookingService
{
    Response createBooking(BookingDTO bookingDTO);
    Response findBookingByReferenceId(String bookingReferece);


}
