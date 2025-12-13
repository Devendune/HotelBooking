package com.example.HotelBooking.services;

import com.example.HotelBooking.dtos.NotificationDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


public interface NotificationService
{
    void sendEmail(NotificationDTO notificationDTO);
    void sendSMS();

    void sendWhatsApp();
}
