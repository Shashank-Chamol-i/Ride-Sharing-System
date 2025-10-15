package com.ridesharing.service;

import com.ridesharing.entity.RideRequest;
import com.ridesharing.repository.RideRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RideService {

    private static final Logger logger = LoggerFactory.getLogger(RideService.class);

    private final RideRequestRepository rideRequestRepository;
    private final ExecutorService passengerThreadPool;

    public RideService(RideRequestRepository rideRequestRepository) {
        this.rideRequestRepository = rideRequestRepository;
        this.passengerThreadPool = Executors.newFixedThreadPool(10); // Thread pool for passengers
    }

    public RideRequest addRideRequest(RideRequest rideRequest) {
        try {
            RideRequest savedRide = rideRequestRepository.save(rideRequest);
            logger.info("Ride request saved successfully: {}", savedRide);

            // Start a passenger thread for this ride request
            PassengerRunnable passengerRunnable = new PassengerRunnable(savedRide);
            passengerThreadPool.submit(passengerRunnable);

            return savedRide;
        } catch (Exception e) {
            logger.error("Error saving ride request: {}", e.getMessage(), e);
            throw e;
        }
    }

    public RideRequest getRideRequestById(Long id) {
        return rideRequestRepository.findById(id).orElse(null);
    }

    public RideRequest updateRideRequest(RideRequest rideRequest) {
        return rideRequestRepository.save(rideRequest);
    }

    public void deleteRideRequest(Long id) {
        rideRequestRepository.deleteById(id);
    }

    public List<RideRequest> getAllRideRequests() {
        return rideRequestRepository.findAll();
    }

    public List<RideRequest> findByDriverId(Long driverId) {
        return rideRequestRepository.findByDriverId(driverId);
    }

    public List<RideRequest> findPendingRides() {
        return rideRequestRepository.findByStatus("PENDING");
    }

    // Shutdown the thread pool when the application stops
    public void shutdown() {
        passengerThreadPool.shutdown();
    }
}