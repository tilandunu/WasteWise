package com.example.foundation.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.foundation.model.Truck;

public interface TruckRepository extends MongoRepository<Truck, String> {
}
