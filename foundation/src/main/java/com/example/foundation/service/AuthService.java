package com.example.foundation.service;

import com.example.foundation.model.User;
import com.example.foundation.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Method to verify ID token and retrieve user
    public User verifyIdTokenAndGetUser(String idToken) throws FirebaseAuthException {
        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        String uid = decodedToken.getUid();
        String email = decodedToken.getEmail();
        String name = (String) decodedToken.getClaims().get("name");
        String picture = (String) decodedToken.getClaims().get("picture");

        Optional<User> optionalUser = userRepository.findByUid(uid);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        // If user doesn't exist, create new one
        boolean isFirstUser = userRepository.count() == 0;

        User newUser = new User();
        newUser.setUid(uid);
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setPicture(picture);
        newUser.setRole(isFirstUser ? User.Role.ADMIN : User.Role.RESIDENT);

        return userRepository.save(newUser);
    }

    //get all users
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
