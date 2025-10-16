package com.example.foundation.repository;

import com.example.foundation.model.Truck;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TruckRepository extends MongoRepository<Truck, String> {

    // Find trucks by status (e.g., "Active", "In Maintenance")
    List<Truck> findByStatus(String status);

    // Find all trucks assigned to a specific route
    List<Truck> findByRoute_Id(String routeId);

    // Find all trucks that have a specific user (crew member) assigned
    List<Truck> findByAssignedUsers_Id(String userId);
}
