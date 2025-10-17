package com.example.foundation.repository;

import com.example.foundation.model.Tag;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends MongoRepository<Tag, String> {
    // Find a tag by its custom tagId field (not Mongo _id)
    Optional<Tag> findByTagId(String tagId);
}
