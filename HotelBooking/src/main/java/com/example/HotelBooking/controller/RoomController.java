package com.example.HotelBooking.controller;

import com.example.HotelBooking.dtos.Response;
import com.example.HotelBooking.dtos.RoomDTO;
import com.example.HotelBooking.entities.Room;
import com.example.HotelBooking.enums.RoomType;
import com.example.HotelBooking.services.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.multi.MultiPanelUI;
import java.math.BigDecimal;
import java.nio.channels.MulticastChannel;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/rooms")
public class RoomController
{
    private final RoomService roomService;

    @PostMapping("/addRoom")
    public ResponseEntity<Response>  addRoom(
            @RequestParam Integer roomNumber,
            @RequestParam BigDecimal pricePerNight,
            @RequestParam String description,
            @RequestParam Integer capacity,
            @RequestParam RoomType type,
            @RequestParam MultipartFile multipartFile
            )
    {
        RoomDTO roomDTO=RoomDTO.builder()
                .roomType(type)
                .description(description)
                .capacity(capacity)
                .pricePerNight(pricePerNight)
                .roomNumber(roomNumber)
                .build();

        return ResponseEntity.ok(roomService.addRoom(roomDTO,multipartFile));
    }

    @PutMapping("/updateRoom")
    public ResponseEntity<Response>  updateRoom(
            @RequestParam(value="roomNumber",required = false) Integer roomNumber,
            @RequestParam(value="pricePerNight",required = false) BigDecimal pricePerNight,
            @RequestParam(value="description",required = false) String description,
            @RequestParam(value="capacity",required = false) Integer capacity,
            @RequestParam(value="type",required = false) RoomType type,
            @RequestParam(value="multipartFile",required = false) MultipartFile multipartFile,
            @RequestParam(value="id", required = true) Long id
    )
    {
        RoomDTO roomDTO=RoomDTO.builder()
                .roomType(type)
                .description(description)
                .capacity(capacity)
                .pricePerNight(pricePerNight)
                .roomNumber(roomNumber)
                .id(id)
                .build();

        return ResponseEntity.ok(roomService.updateRoom(roomDTO,multipartFile));
    }

    @GetMapping("/allRooms")
    public ResponseEntity<Response>  getAllRooms()
    {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/allRooms/{id}")
    public ResponseEntity<Response> getRoomsById(@PathVariable Long id)
    {
        return ResponseEntity.ok(roomService.getRoomsById(id));
    }

    @GetMapping("/availableRooms")
    public ResponseEntity<Response> fetchAllAvailableRooms(
            @RequestParam LocalDate checkInDate,
            @RequestParam LocalDate checkOutDate,
            @RequestParam RoomType roomType
            )
    {
        return ResponseEntity.ok(roomService.getAvailableRooms(checkInDate,checkOutDate,roomType));
    }

    @GetMapping("/fetchAllRoomTypes")
    public ResponseEntity<List<RoomType>> fetchAllRoomTypes()
    {
        return ResponseEntity.ok(roomService.getALlRoomTypes());
    }

    @GetMapping("/search")
    public ResponseEntity<Response> searchRoom(@RequestParam String input)
    {
        return ResponseEntity.ok(roomService.searchRooms());
    }


}
