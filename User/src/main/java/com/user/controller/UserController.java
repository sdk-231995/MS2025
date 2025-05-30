package com.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.exception.UserNotFoundException;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Map<Integer, String> USER_DB = Map.of(
            1, "Alice", 2, "Bob", 3, "Charlie"
    );

    @GetMapping("/{id}")
    public ResponseEntity<String> getUserById(@PathVariable int id) {
        if (!USER_DB.containsKey(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        return ResponseEntity.ok(USER_DB.get(id));
    }
    
    @GetMapping("/UserInfo/{id}")
    public ResponseEntity<String> getUserInfo(@PathVariable int id) {
        if (!USER_DB.containsKey(id)) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
        return ResponseEntity.ok(USER_DB.get(id));
    }
}
