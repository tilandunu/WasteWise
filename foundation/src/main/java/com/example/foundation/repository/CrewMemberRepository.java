package com.example.foundation.repository;

import com.example.foundation.model.CrewMember;
import com.example.foundation.model.Truck;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CrewMemberRepository extends MongoRepository<CrewMember, String> {

    // Find crew by username
    Optional<CrewMember> findByUsername(String username);

    // Find all available crew members
    List<CrewMember> findByAvailableTrue();

    // Find crew assigned to a specific truck
    List<CrewMember> findByAssignedTruck(Truck truck);

    // Find crew not assigned to any truck
    List<CrewMember> findByAssignedTruckIsNull();
}
