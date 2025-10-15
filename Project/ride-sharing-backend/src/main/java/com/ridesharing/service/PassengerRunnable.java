package com.ridesharing.service;


import com.ridesharing.entity.RideRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PassengerRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PassengerRunnable.class);

    private final RideRequest rideRequest;

    public PassengerRunnable(RideRequest rideRequest) {
        this.rideRequest = rideRequest;
    }

    @Override
    public void run() {
        try {
            logger.info("Passenger thread started for ride request ID: {}", rideRequest.getId());
            // Simulate the passenger waiting for a driver to accept the ride
            // For simplicity, we'll just wait for 30 seconds or until the ride is accepted/declined
            long startTime = System.currentTimeMillis();
            long timeout = 30_000; // 30 seconds

            while (System.currentTimeMillis() - startTime < timeout) {
                if ("ACCEPTED".equals(rideRequest.getStatus()) || "DECLINED".equals(rideRequest.getStatus())) {
                    logger.info("Ride request ID: {} has been processed with status: {}", rideRequest.getId(), rideRequest.getStatus());
                    break;
                }
                Thread.sleep(1000); // Check every second
            }

            if ("PENDING".equals(rideRequest.getStatus())) {
                logger.info("Ride request ID: {} timed out after 30 seconds", rideRequest.getId());
                rideRequest.setStatus("TIMED_OUT");
                // Note: In a real app, you'd save this status to the database here
            }
        } catch (InterruptedException e) {
            logger.error("Passenger thread interrupted for ride request ID: {}", rideRequest.getId(), e);
            Thread.currentThread().interrupt();
        }
    }
}
