package com.ridesharing.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "passenger_rides")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RideRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pickup_location", nullable = false)
    private String pickupLocation;

    @Column(name = "drop_location", nullable = false)
    private String dropLocation;

    @Column(name = "vehicle_type", nullable = false)
    private String vehicleType;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "driver_id", nullable = true)
    private Long driverId;
}