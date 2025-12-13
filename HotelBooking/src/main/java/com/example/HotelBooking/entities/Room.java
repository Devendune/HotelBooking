package com.example.HotelBooking.entities;

import com.example.HotelBooking.enums.RoomType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="rooms")
public class Room
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value=1,message = "Room number must be atleast 1")
    @Column(unique = true)
    private Integer roomNumber;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "room type is required")
    private RoomType roomType;

    @Min(value=1)
    private Integer capacity;

    private String description;

    @DecimalMin(value="0.1",message = "Price per night is required")
    private BigDecimal pricePerNight;

    private String imageURL;
}
