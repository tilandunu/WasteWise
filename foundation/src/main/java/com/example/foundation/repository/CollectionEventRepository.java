package com.example.foundation.repository;

import com.example.foundation.model.CollectionEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionEventRepository extends MongoRepository<CollectionEvent, String> {
    // Additional query methods can be defined here if needed
}
