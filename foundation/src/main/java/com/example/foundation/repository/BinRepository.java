package com.example.foundation.repository;

import com.example.foundation.model.Bin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BinRepository extends MongoRepository<Bin, String> {

    // Find all bins by status
    @Query("{ 'status': ?0 }")
    List<Bin> findBinsByStatus(String status);

    // Find all bins
    List<Bin> findAll();
}
