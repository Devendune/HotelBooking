package com.example.HotelBooking.entities;

import com.example.HotelBooking.enums.PaymentGateway;
import com.example.HotelBooking.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private BigDecimal amount;

    private String transactionId;

    @Enumerated(EnumType.STRING)
    private PaymentGateway paymentGateway;

    private LocalDate paymentDate=LocalDate.now();

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String bookingReference;

    private String failureReason;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
