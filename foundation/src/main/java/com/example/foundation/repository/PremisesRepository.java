package com.example.foundation.repository;

import com.example.foundation.model.Premises;
import com.example.foundation.model.Route;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PremisesRepository extends MongoRepository<Premises, String> {
    List<Premises> findByZoneId(String zoneId);
    List<Premises> findByOwnerId(String ownerId);
     List<Premises> findByRoute(Route route);
}
