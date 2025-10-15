package com.ridesharing.service;

import com.ridesharing.entity.RideRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RideNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public RideNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Notify driver about a new ride request.
     */
    public void notifyDriver(Long driverId, RideRequest rideRequest) {
        // Sending message to topic specific to driver
        messagingTemplate.convertAndSend("/topic/driver/" + driverId, rideRequest);
    }
}
