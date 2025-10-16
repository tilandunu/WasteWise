package com.example.foundation.repository;

import com.example.foundation.model.Bin;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface BinRepository extends MongoRepository<Bin, String> {

    List<Bin> findByStatus(String status);

    List<Bin> findByPremisesId(String premisesId);

    List<Bin> findByTagId(String tagId);

    // ✅ New method to get a single Bin (assuming binCode is unique)
    Optional<Bin> getBinByBinCode(String binCode);
}
