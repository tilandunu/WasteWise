package com.example.foundation.service.registration;

import com.example.foundation.dto.request.RegisterUserRequest;
import com.example.foundation.model.Resident;
import com.example.foundation.model.Zone;
import com.example.foundation.repository.UserRepository;
import com.example.foundation.repository.ZoneRepository;
import org.springframework.stereotype.Service;

@Service
public class ResidentRegistrationStrategy implements UserRegistrationStrategy {

    private final UserRepository userRepository;
    private final ZoneRepository zoneRepository;

    public ResidentRegistrationStrategy(UserRepository userRepository, ZoneRepository zoneRepository) {
        this.userRepository = userRepository;
        this.zoneRepository = zoneRepository;
    }

    @Override
    public boolean supports(String userRole) {
        return "resident".equalsIgnoreCase(userRole);
    }

    @Override
    public String register(RegisterUserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        Zone zone = zoneRepository.findById(request.getZoneId())
                .orElseThrow(() -> new RuntimeException("Zone not found"));

        Resident newUser = new Resident(
                request.getUsername(),
                request.getPassword(),
                request.getAddress(),
                request.getContactNumber()
        );

        newUser.setZone(zone);
        newUser.setActivated(false);

        if (request.getPremisesType() != null) {
            try {
                newUser.setPremisesType(Resident.PremisesType.valueOf(request.getPremisesType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid premises type");
            }
        }

        userRepository.save(newUser);
        return "Resident registered successfully. Await activation.";
    }
}
