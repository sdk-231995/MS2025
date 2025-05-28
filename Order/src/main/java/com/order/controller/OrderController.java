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

import com.order.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Value("${user.service.url}")
    private String userServiceUrl;
    @Autowired
    private  OrderService orderService;

    @Autowired
    private  RestTemplate restTemplate;

    @GetMapping("/user/{id}")
    public ResponseEntity<String> getUserForOrder(@PathVariable int id) {
        try {
            ResponseEntity<String> userResponse =
                    restTemplate.getForEntity(userServiceUrl + id, String.class);
            return ResponseEntity.ok("Order placed by: " + userResponse.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            throw new RuntimeException("Failed to retrieve user: " + ex.getMessage());
        }
    }
    @GetMapping("/user/retry/{id}")
    public ResponseEntity<String> getOrderForUser(@PathVariable int id) {
        String username = orderService.getUserNameById(id);
        return ResponseEntity.ok("Order placed by: " + username);
    }
}
