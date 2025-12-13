package com.example.HotelBooking.services.impl;

import com.example.HotelBooking.dtos.Response;
import com.example.HotelBooking.dtos.RoomDTO;
import com.example.HotelBooking.entities.Room;
import com.example.HotelBooking.enums.RoomType;
import com.example.HotelBooking.exceptions.InvalidBookingStateAndDateException;
import com.example.HotelBooking.exceptions.NotFoundException;
import com.example.HotelBooking.repository.RoomRepository;
import com.example.HotelBooking.services.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService
{
    private final ModelMapper modelMapper;
    private final RoomRepository roomRepository;

    private final String IMAGE_DIRECTORY=System.getProperty("user.dir")+"/product-image/";

    @Override
    public Response addRoom(RoomDTO roomDTO, MultipartFile imageFile)
    {
        Room roomToSave=modelMapper.map(roomDTO, Room.class);
        if(imageFile!=null)
        {
            String imagePath=saveImage(imageFile);
            roomToSave.setImageURL(imagePath);
        }
        roomRepository.save(roomToSave);
        return Response.builder()
                .status(HttpStatus.CREATED.value())
                .message("room is created")
                .build();
    }

    @Override
    public Response updateRoom(RoomDTO roomDTO, MultipartFile imageFile)
    {
        Optional<Room> existingRoom= Optional.ofNullable(roomRepository.findById(roomDTO.getId()).orElseThrow(() -> new NotFoundException("The room is not found")));
        if(imageFile!=null && !imageFile.isEmpty())
        {
            String imagePath=saveImage(imageFile);
            existingRoom.get().setImageURL(imagePath);
        }
        if(roomDTO.getRoomNumber()!=null && roomDTO.getRoomNumber()>0)
        {
            existingRoom.get().setRoomNumber(roomDTO.getRoomNumber());
        }

        if(roomDTO.getCapacity()!=null && roomDTO.getCapacity()>0)
        {
            existingRoom.get().setCapacity(roomDTO.getCapacity());
        }
        if(roomDTO.getRoomType()!=null)
        {
            existingRoom.get().setRoomType(roomDTO.getRoomType());
        }
        if(roomDTO.getDescription()!=null)
        {
            existingRoom.get().setDescription(roomDTO.getDescription());
        }

        roomRepository.save(existingRoom.get());
        return Response.builder()
                        .status(HttpStatus.NO_CONTENT.value())
                                .message("The room is updated")
                                        .build();

    }

    @Override
    public Response getAllRooms()
    {
        List<Room> roomList=roomRepository.findAll();
        List<RoomDTO> roomDTOList=modelMapper.map(roomList,new TypeToken<List<RoomDTO>>() {}.getType());
        return Response.builder()
                .status(HttpStatus.OK.value())
                .rooms(roomDTOList)
                .message("The rooms are fetched")
                .build();
    }

    @Override
    public Response getRoomsById(Long id)
    {
       Optional<Room> fetchedRoom= Optional.ofNullable(roomRepository.findById(id)
               .orElseThrow(() -> new NotFoundException("Room not found")));

       RoomDTO roomDTO=modelMapper.map(fetchedRoom.get(),RoomDTO.class);
       return Response.builder()
               .status(HttpStatus.OK.value())
               .message("The room is fetched")
               .build();
    }

    @Override
    public Response getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType)
    {
        if(checkInDate.isBefore(LocalDate.now()))
            throw new InvalidBookingStateAndDateException("The Checkin date cannot be earlier than current one");

        if(checkOutDate.isBefore(checkInDate))
            throw new InvalidBookingStateAndDateException("The checkout date cannot be earlier than checkin Date");

        List<Room> availableRooms=roomRepository.findAvailableRooms(checkInDate,checkOutDate,roomType);
        List<RoomDTO> availableRoomsDTO=modelMapper.map(availableRooms,new TypeToken<List<RoomDTO>>() {}.getType());
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("The available rooms are here")
                .rooms(availableRoomsDTO)
                .build();
    }

    @Override
    public List<RoomType> getALlRoomTypes() {
        return roomRepository.getAllRoomTypes();
    }

    @Override
    public Response searchRooms() {
        return null;
    }

    private String saveImage(MultipartFile imageFile)
    {
        if(!Objects.requireNonNull(imageFile.getContentType()).startsWith("image"))
            throw new IllegalArgumentException("only image is allowed");

        File directory=new File(IMAGE_DIRECTORY);
        if(!directory.exists())
            directory.mkdir();

        String uniqueFileName= UUID.randomUUID()+"_"+imageFile.getOriginalFilename();
        String imagePath=IMAGE_DIRECTORY+uniqueFileName;

        try{
            File destinationFilePath=new File(imagePath);
            imageFile.transferTo(destinationFilePath);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
        return imagePath;
    }
}
