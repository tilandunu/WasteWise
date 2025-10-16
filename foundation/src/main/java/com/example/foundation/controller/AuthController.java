package com.example.foundation.controller;

import com.example.foundation.dto.response.AuthResponse;
import com.example.foundation.model.User;
import com.example.foundation.service.AuthService;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    //
    private final AuthService authService;

    // Constructor injection
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Endpoint for Google login
    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        String idToken = authorizationHeader.substring(7);

        try {
            User user = authService.verifyIdTokenAndGetUser(idToken);

            return ResponseEntity.ok().body(new AuthResponse("Login successful", user));
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }

    //Get all users
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok().body(authService.getAllUsers());
    }
}
