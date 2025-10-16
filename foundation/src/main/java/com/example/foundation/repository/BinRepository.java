package com.example.foundation.repository;

import com.example.foundation.model.Bin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BinRepository extends MongoRepository<Bin, String> {

    @Query("{ 'status': ?0, 'assignedUser': { $exists: false } }")
    List<Bin> findUnassignedBins(String status);
}
