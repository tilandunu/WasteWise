package com.example.foundation.repository;

import com.example.foundation.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUid(String uid);
    Optional<User> findByEmail(String email);
}
