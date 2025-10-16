package com.example.foundation.repository;

import com.example.foundation.model.Truck;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TruckRepository extends MongoRepository<Truck, String> {
    
    List<Truck> findByStatus(String status);
    
    // Change from findByRoute_Id to findByAssignedRoute_Id
    List<Truck> findByAssignedRoute_Id(String routeId);
    
    List<Truck> findByAssignedCrew_Id(String crewId);
}