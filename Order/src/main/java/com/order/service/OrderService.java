package com.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {
	
	private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

//    @Retryable(
//        value = {HttpServerErrorException.class, RuntimeException.class},
//        maxAttempts = 3,
//        backoff = @Backoff(delay = 2000)
//    )
    @Retryable(
    	    value = {HttpServerErrorException.class}, // only retry on 5xx errors
    	    exclude = {IllegalArgumentException.class}, // don't retry on bad inputs
    	    maxAttempts = 4,
    	    backoff = @Backoff(delay = 1000, multiplier = 2.0, maxDelay = 5000)
    	)

    public String getUserNameById(int userId) {
    	 log.info("Attempting to call UserService for userId={}", userId);
        return restTemplate.getForObject(userServiceUrl + userId, String.class);
    }
    
    @Recover
    public String recoverFromFailure(RuntimeException ex, int userId) {
        log.error("All retries failed for userId={}, falling back. Reason: {}", userId, ex.getMessage());
        return "Unknown User (fallback)";
    }
    
}
