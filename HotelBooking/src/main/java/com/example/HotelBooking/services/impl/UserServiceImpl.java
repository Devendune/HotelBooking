package com.example.HotelBooking.services.impl;

import com.example.HotelBooking.dtos.*;
import com.example.HotelBooking.entities.Booking;
import com.example.HotelBooking.entities.User;
import com.example.HotelBooking.enums.UserRole;
import com.example.HotelBooking.exceptions.InvalidCredentialException;
import com.example.HotelBooking.exceptions.NotFoundException;
import com.example.HotelBooking.repository.BookingRepository;
import com.example.HotelBooking.repository.UserRepository;
import com.example.HotelBooking.security.JwtUtils;
import com.example.HotelBooking.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.process.internal.UserTypeResolution;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BookingRepository bookingRepository;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;

    @Override
    public Response registerUser(RegistrationRequest registrationRequest)
    {
        UserRole role=UserRole.CUSTOMER;
        if(registrationRequest.getUserRole()!=null)
            role=registrationRequest.getUserRole();

        User userToSave=User.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .role(role)
                .phoneNumber(registrationRequest.getPhoneNumber())
                .isActive(Boolean.TRUE)
                .build();

        userRepository.save(userToSave);
        return Response.builder()
                .status(200)
                .message("User Registered successfully")
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        User user=userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new NotFoundException("User does not exist"));

        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword()))
            throw  new InvalidCredentialException("Password do not match");

        String token=jwtUtils.generateToken(user.getEmail());
        return Response.builder()
                .status(200)
                .message("User logged in successfully")
                .role(user.getRole())
                .token(token)
                .expirationTime("2 months")
                .isActive(user.getIsActive())
                .build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users=userRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        List<UserDTO> userDTOList=modelMapper.map(users,new TypeToken<List<UserDTO>>(){}.getType());
        return Response.builder()
                .status(200)
                .message("Users fetched successfully")
                .users(userDTOList)
                .build();
    }

    @Override
    public Response getOwnAccountDetails()
    {
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));

        UserDTO userDTO=modelMapper.map(user,UserDTO.class);
        return Response.builder()
                .status(200)
                .message("success")
                .userDTO(userDTO)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser()
    {
        String email=SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException("User not found"));
    }

    @Override
    public Response updateOwnAccount(UserDTO userDTO)
    {
        User existingUser=getCurrentLoggedInUser();

        if(userDTO.getPassword()!=null)
            existingUser.setPhoneNumber(userDTO.getPhoneNumber());

        if(userDTO.getEmail()!=null)
            existingUser.setEmail(existingUser.getEmail());

        if(userDTO.getEmail()!=null)
            existingUser.setPassword(existingUser.getPassword());

        if(userDTO.getEmail()!=null)
            existingUser.setFirstName(existingUser.getFirstName());

        if(userDTO.getEmail()!=null)
            existingUser.setLastName(existingUser.getLastName());

        User savedUser=userRepository.save(existingUser);
        return Response.builder()
                .status(200)
                .message("User update successfully")
                .build();
    }

    @Override
    public Response deleteOwnAccount()
    {
        User user=getCurrentLoggedInUser();
        userRepository.delete(user);

        return Response.builder()
                .status(200)
                .message("User deleted successfully")
                .build();

    }

    @Override
    public Response getMyBookingHistory()
    {
        User user=getCurrentLoggedInUser();
        List<Booking> bookingList=bookingRepository.findByUserId(user.getId());
        List<BookingDTO> bookingDTOList=modelMapper.map(bookingList,
                new TypeToken<List<BookingDTO>>(){}.getType());
        return Response.builder()
                .status(200)
                .message("Fetched Booking history")
                .bookings(bookingDTOList)
                .build();
    }

}
