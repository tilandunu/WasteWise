package com.example.foundation.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.foundation.dto.request.RegisterUserRequest;
import com.example.foundation.model.Resident;
import com.example.foundation.model.User;
import com.example.foundation.repository.UserRepository;
import com.example.foundation.service.registration.UserRegistrationStrategy;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRegistrationStrategy registrationStrategy;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, List.of(registrationStrategy));
    }

    @Test
    void testRegisterUser_SupportedRole() {
        RegisterUserRequest request = mock(RegisterUserRequest.class);
        when(request.getUserRole()).thenReturn("resident");
        when(registrationStrategy.supports("resident")).thenReturn(true);
        when(registrationStrategy.register(request)).thenReturn("success");
        String result = userService.registerUser(request);
        assertEquals("success", result);
    }

    @Test
    void testRegisterUser_UnsupportedRole() {
        RegisterUserRequest request = mock(RegisterUserRequest.class);
        when(request.getUserRole()).thenReturn("unknown");
        when(registrationStrategy.supports("unknown")).thenReturn(false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(request));
        assertEquals("Unsupported user role: unknown", ex.getMessage());
    }

    @Test
    void testLogin_Success() {
        User user = mock(User.class);
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn("pass");
        Object result = userService.login("user1", "pass");
        assertEquals(user, result);
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.login("user1", "pass"));
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void testLogin_InvalidPassword() {
        User user = mock(User.class);
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn("pass");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.login("user1", "wrong"));
        assertEquals("Invalid credentials", ex.getMessage());
    }

    @Test
    void testGetAllResidents() {
        Resident resident1 = mock(Resident.class);
        Resident resident2 = mock(Resident.class);
        User user = mock(User.class);
        when(userRepository.findAll()).thenReturn(List.of(resident1, resident2, user));
        List<Resident> result = userService.getAllResidents();
        assertEquals(2, result.size());
        assertTrue(result.contains(resident1));
        assertTrue(result.contains(resident2));
    }
}
