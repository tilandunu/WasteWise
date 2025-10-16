package com.example.foundation.repository;

import com.example.foundation.model.Zone;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ZoneRepository extends MongoRepository<Zone, String> {
    Optional<Zone> findByName(String name);
}
