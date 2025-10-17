package com.example.foundation.service;

import com.example.foundation.dto.request.RegisterUserRequest;
import com.example.foundation.model.Resident;
import com.example.foundation.repository.UserRepository;
import com.example.foundation.service.registration.UserRegistrationStrategy;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final List<UserRegistrationStrategy> registrationStrategies;

    public UserService(UserRepository userRepository, List<UserRegistrationStrategy> registrationStrategies) {
        this.userRepository = userRepository;
        this.registrationStrategies = registrationStrategies;
    }

    // Register
    public String registerUser(RegisterUserRequest request) {
        String role = request.getUserRole();

        return registrationStrategies.stream()
                .filter(strategy -> strategy.supports(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported user role: " + role))
                .register(request);
    }

    // --- Login ---
    public Object login(String username, String password) {
        var userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty())
            throw new IllegalArgumentException("User not found");

        var user = userOpt.get();

        if (!user.getPassword().equals(password))
            throw new IllegalArgumentException("Invalid credentials");

        // Uncomment if activation is required:
        // if (!user.isActivated()) throw new IllegalStateException("User not
        // activated");

        return user;
    }

    // --- Get All Residents ---
    public List<Resident> getAllResidents() {
        return userRepository.findAll().stream()
                .filter(user -> user instanceof Resident)
                .map(user -> (Resident) user)
                .toList();
    }
}
