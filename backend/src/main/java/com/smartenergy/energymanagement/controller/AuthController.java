package com.smartenergy.energymanagement.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartenergy.energymanagement.model.User;
import com.smartenergy.energymanagement.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000") // Ensure frontend can access the backend
public class AuthController {

    @Autowired
    private UserService userService;

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Email and password are required.");
        }

        Optional<User> loggedInUser = userService.login(user.getEmail(), user.getPassword());

        if (loggedInUser.isPresent()) {
            User foundUser = loggedInUser.get();
            Map<String, Object> response = new HashMap<>();
            response.put("email", foundUser.getEmail());
            response.put("companyName", foundUser.getCompanyName());
            response.put("dashboardLink", foundUser.getDashboardLink()); // Send dashboard link
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Invalid email or password.");
        }
    }


    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null ||
            user.getCompanyName() == null || user.getState() == null || user.getPhoneNumber() == null) {
            return ResponseEntity.badRequest().body("All fields are required.");
        }

        if (userService.emailExists(user.getEmail())) {
            return ResponseEntity.status(400).body("Email already exists!");
        }
        
        userService.register(user);
        return ResponseEntity.ok("User registered successfully!");
    }
}
