package com.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.order.exception.DownstreamServiceException;
import com.order.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Value("${user.service.url}")
	private String userServiceUrl;
	@Autowired
	private OrderService orderService;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/user/{id}")
	@CircuitBreaker(name = "userService", fallbackMethod = "userFallback")
	@Retry(name = "userService")
	@RateLimiter(name = "userService")
	public ResponseEntity<String> getUserForOrder(@PathVariable int id) {
		try {
			ResponseEntity<String> userResponse = restTemplate.getForEntity(userServiceUrl + id, String.class);
			return ResponseEntity.ok("Order placed by: " + userResponse.getBody());
		} catch (HttpClientErrorException | HttpServerErrorException ex) {
			throw new RuntimeException("Failed to retrieve user: " + ex.getMessage());
		}
	}

	@GetMapping("/UserInfo/{id}")
	public ResponseEntity<String> getOrderForUser(@PathVariable int id) {
		try {
			System.out.println("hello*************");

			ResponseEntity<String> userResponse = restTemplate.getForEntity("http://127.0.0.1:8081/users/UserInfo/" + id, String.class);
			return ResponseEntity.ok("Order placed by: " + userResponse.getBody());

		} catch (DownstreamServiceException ex) {
			// Log or transform
			throw ex;
		}

	}

	public String userFallback(int userId, Throwable ex) {
		return "Fallback: User service is down.";
	}
}
