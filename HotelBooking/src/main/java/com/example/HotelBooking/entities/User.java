package com.example.HotelBooking.entities;

import com.example.HotelBooking.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email is required")
    @Column(unique=true)
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "phone number is required")
    private String phoneNumber;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Boolean isActive;
    private LocalDateTime localDate=LocalDateTime.now();

}
