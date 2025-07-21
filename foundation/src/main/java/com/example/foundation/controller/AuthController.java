package com.example.foundation.controller;

import com.example.foundation.model.User;
import com.example.foundation.service.AuthService;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

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

    // DTO class for response
    public static class AuthResponse {
        private String message;
        private User user;

        public AuthResponse(String message, User user) {
            this.message = message;
            this.user = user;
        }

        public String getMessage() {
            return message;
        }

        public User getUser() {
            return user;
        }
    }
}
