package com.ridesharing;

import com.ridesharing.entity.RideRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class QueueConfig {

    @Bean
    public BlockingQueue<RideRequest> rideRequestQueue() {
        return new LinkedBlockingQueue<>();
    }
}
