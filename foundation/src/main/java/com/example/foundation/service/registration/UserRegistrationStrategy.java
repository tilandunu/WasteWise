package com.example.foundation.service.registration;

import com.example.foundation.dto.request.RegisterUserRequest;

public interface UserRegistrationStrategy {
    boolean supports(String userRole);
    String register(RegisterUserRequest request);
}
