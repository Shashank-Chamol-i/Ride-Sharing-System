package com.ridesharing;

import com.ridesharing.entity.RideRequest;
import com.ridesharing.utils.DriverRunnable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class RideSharingApplication {

    private final BlockingQueue<RideRequest> rideRequestQueue = new ArrayBlockingQueue<>(100);

    private ExecutorService driverPool;

    public static void main(String[] args) {
        SpringApplication.run(RideSharingApplication.class, args);
    }

    @PostConstruct
    public void startDriverThreads() {
        driverPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            driverPool.submit(new DriverRunnable(rideRequestQueue));
        }
    }
}