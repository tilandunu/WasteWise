package com.example.foundation.repository;

import com.example.foundation.model.Bin;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface BinRepository extends MongoRepository<Bin, String> {
    List<Bin> findByStatus(String status);
    List<Bin> findByPremisesId(String premisesId);
}
