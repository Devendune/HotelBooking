package com.example.HotelBooking.dtos;

import com.example.HotelBooking.entities.User;
import com.example.HotelBooking.enums.PaymentGateway;
import com.example.HotelBooking.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentDTO
{
    private Long id;

    private BigDecimal amount;

    private String transactionId;

    private PaymentGateway paymentGateway;

    private LocalDate paymentDate;

    private PaymentStatus paymentStatus;

    private String bookingReference;

    private String failureReason;

}
