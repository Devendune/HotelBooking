package com.example.HotelBooking.dtos;

import com.example.HotelBooking.entities.Room;
import com.example.HotelBooking.entities.User;
import com.example.HotelBooking.enums.BookingStatus;
import com.example.HotelBooking.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
//using JsonInclude this gives control over fact that not null fields are excluded in Json
//JsonIgnoreProperties ignore properties that are not passed in the DTO, does not break in case new fields are there
public class BookingDTO
{
    private Long id;

    private UserDTO user;

    private RoomDTO room;

    private Long roomId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private BookingStatus bookingStatus;

    private LocalDate createdAt;

    private BigDecimal totalPrice;

    private PaymentStatus paymentStatus;

    private String bookingReference;

}
