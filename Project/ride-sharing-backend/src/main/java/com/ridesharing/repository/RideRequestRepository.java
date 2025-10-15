package com.ridesharing.repository;

import com.ridesharing.entity.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {

    List<RideRequest> findByDriverId(Long driverId);

    List<RideRequest> findByStatus(String status);
}