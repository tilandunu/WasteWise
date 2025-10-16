package com.example.foundation.repository;

import com.example.foundation.model.Route;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends MongoRepository<Route, String> {

    // Find all routes that are active
    List<Route> findByActiveTrue();

    // Find routes by zone
    List<Route> findByZone_Id(String zoneId);

    // Find route by name
    Route findByRouteName(String routeName);
}
