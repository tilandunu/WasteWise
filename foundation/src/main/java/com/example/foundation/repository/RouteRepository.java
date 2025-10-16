package com.example.foundation.repository;

import com.example.foundation.model.Route;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface RouteRepository extends MongoRepository<Route, String> {

    // Find all active routes
    List<Route> findByActiveTrue();

    // Find route by name
    Route findByRouteName(String routeName);

    // Find routes in a specific zone
    List<Route> findByZone_Id(String zoneId);

}
