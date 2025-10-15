package com.ridesharing.controller;


import com.ridesharing.entity.User;
import com.ridesharing.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")  // Allow all origins, adjust in production
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Check if email already exists
        User existingUser = userService.loginUser(user.getEmail(), user.getPassword());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists.");
        }
        // Save new user
        try {
            User savedUser = userService.registerUser(user);
            // Do not send password back in response
            savedUser.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        User existingUser = userService.loginUser(user.getEmail(), user.getPassword());
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }
        // Do not send password back in response
        existingUser.setPassword(null);
        return ResponseEntity.ok(existingUser);
    }
}


