package com.ridesharing.controller;

import com.ridesharing.entity.RideRequest;
import com.ridesharing.service.RideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@CrossOrigin(origins = "*")
public class RideController {

    private static final Logger logger = LoggerFactory.getLogger(RideController.class);

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestRide(@RequestBody RideRequest rideRequest) {
        try {
            logger.info("Received ride request: {}", rideRequest);

            if (rideRequest.getPickupLocation() == null || rideRequest.getDropLocation() == null ||
                rideRequest.getVehicleType() == null || rideRequest.getPrice() == null) {
                logger.warn("Validation failed: pickupLocation={}, dropLocation={}, vehicleType={}, price={}",
                    rideRequest.getPickupLocation(), rideRequest.getDropLocation(),
                    rideRequest.getVehicleType(), rideRequest.getPrice());
                return ResponseEntity.badRequest().body("Pickup location, drop location, vehicle type, and price are required.");
            }

            RideRequest savedRide = rideService.addRideRequest(rideRequest);
            return ResponseEntity.ok(savedRide);
        } catch (Exception e) {
            logger.error("Error processing ride request: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Failed to place ride request: " + e.getMessage());
        }
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<RideRequest>> getRidesForDriver(@PathVariable Long driverId) {
        logger.info("Fetching pending rides for driverId: {}", driverId);
        List<RideRequest> rides = rideService.findPendingRides();
        logger.info("Found {} pending rides", rides.size());
        return ResponseEntity.ok(rides);
    }

    @PostMapping("/{rideId}/accept")
    public ResponseEntity<?> acceptRide(@PathVariable Long rideId, @RequestParam Long driverId) {
        try {
            RideRequest ride = rideService.getRideRequestById(rideId);
            if (ride == null) {
                return ResponseEntity.notFound().build();
            }
            if (!"PENDING".equalsIgnoreCase(ride.getStatus())) {
                return ResponseEntity.badRequest().body("Ride is not in pending state.");
            }
            ride.setStatus("ACCEPTED");
            ride.setDriverId(driverId);
            rideService.updateRideRequest(ride);
            return ResponseEntity.ok("Ride accepted successfully.");
        } catch (Exception e) {
            logger.error("Error accepting ride: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Failed to accept ride: " + e.getMessage());
        }
    }

    @PostMapping("/{rideId}/decline")
    public ResponseEntity<?> declineRide(@PathVariable Long rideId) {
        try {
            RideRequest ride = rideService.getRideRequestById(rideId);
            if (ride == null) {
                return ResponseEntity.notFound().build();
            }
            if (!"PENDING".equalsIgnoreCase(ride.getStatus())) {
                return ResponseEntity.badRequest().body("Ride is not in pending state.");
            }
            ride.setStatus("DECLINED");
            rideService.updateRideRequest(ride);
            return ResponseEntity.ok("Ride declined successfully.");
        } catch (Exception e) {
            logger.error("Error declining ride: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Failed to decline ride: " + e.getMessage());
        }
    }

    @PostMapping("/{rideId}/start")
    public ResponseEntity<?> startRide(@PathVariable Long rideId) {
        try {
            RideRequest ride = rideService.getRideRequestById(rideId);
            if (ride == null) {
                return ResponseEntity.notFound().build();
            }
            if (!"ACCEPTED".equalsIgnoreCase(ride.getStatus())) {
                return ResponseEntity.badRequest().body("Ride is not in accepted state.");
            }
            ride.setStatus("IN_PROGRESS");
            rideService.updateRideRequest(ride);
            return ResponseEntity.ok("Ride started successfully.");
        } catch (Exception e) {
            logger.error("Error starting ride: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Failed to start ride: " + e.getMessage());
        }
    }

    @PostMapping("/{rideId}/complete")
    public ResponseEntity<?> completeRide(@PathVariable Long rideId) {
        try {
            RideRequest ride = rideService.getRideRequestById(rideId);
            if (ride == null) {
                return ResponseEntity.notFound().build();
            }
            if (!"IN_PROGRESS".equalsIgnoreCase(ride.getStatus())) {
                return ResponseEntity.badRequest().body("Ride is not in progress.");
            }
            ride.setStatus("COMPLETED");
            rideService.updateRideRequest(ride);
            return ResponseEntity.ok("Ride completed successfully.");
        } catch (Exception e) {
            logger.error("Error completing ride: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Failed to complete ride: " + e.getMessage());
        }
    }
}