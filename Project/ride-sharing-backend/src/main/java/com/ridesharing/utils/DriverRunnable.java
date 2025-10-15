package com.ridesharing.utils;

import com.ridesharing.entity.RideRequest;

import com.ridesharing.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class DriverRunnable implements Runnable {

    private final BlockingQueue<RideRequest> rideRequestQueue;

    @Autowired
    private RideService rideService; // Assuming this is managed by Spring

   // Injecting the notification service

    // Constructor accepting queue
    public DriverRunnable(BlockingQueue<RideRequest> rideRequestQueue) {
        this.rideRequestQueue = rideRequestQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                RideRequest ride = rideRequestQueue.take();

                System.out.println("Driver thread processing ride request: " + ride.getId());

                // Assign a driver ID (for demo purposes, using a static ID)
               // Example driverId for demo

                // Update ride status to IN_PROGRESS
                
                rideService.updateRideRequest(ride);

                // Notify the driver about the new ride request
         

                System.out.println("Ride " + ride.getId() + " started by driver.");

                // Simulate driving time
                Thread.sleep(5000);

                // Update ride status to COMPLETED
                
                rideService.updateRideRequest(ride);

                System.out.println("Ride " + ride.getId() + " completed by driver.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Driver thread interrupted.");
            } catch (Exception e) {
                System.err.println("Error processing ride request: " + e.getMessage());
            }
        }
    }
}
