package com.example.foundation.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.foundation.model.Route;
import com.example.foundation.model.Truck;
import java.util.List;

public interface RouteRepository extends MongoRepository<Route, String> {
    List<Route> findByAssignedTruck(Truck truck);
}
