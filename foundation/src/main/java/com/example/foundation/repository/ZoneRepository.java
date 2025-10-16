package com.example.foundation.repository;

import com.example.foundation.model.Zone;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ZoneRepository extends MongoRepository<Zone, String> {
    boolean existsByName(String name);
}
