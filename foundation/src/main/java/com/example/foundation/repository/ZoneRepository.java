package com.example.foundation.repository;

import com.example.foundation.model.Zone;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepository extends MongoRepository<Zone, String> {
}
