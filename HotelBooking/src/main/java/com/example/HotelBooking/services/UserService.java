package com.example.HotelBooking.services;

import com.example.HotelBooking.dtos.LoginRequest;
import com.example.HotelBooking.dtos.RegistrationRequest;
import com.example.HotelBooking.dtos.Response;
import com.example.HotelBooking.dtos.UserDTO;
import com.example.HotelBooking.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

public interface UserService
{
    Response registerUser(RegistrationRequest registrationRequest);
    Response loginUser(LoginRequest loginRequest);
    Response getAllUsers();

    Response getOwnAccountDetails();
    User getCurrentLoggedInUser();
    Response updateOwnAccount(UserDTO userDTO);
    Response deleteOwnAccount();

    Response getMyBookingHistory();
}
