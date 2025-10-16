package com.example.foundation.repository;

import com.example.foundation.model.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface TagRepository extends MongoRepository<Tag, String> {
    Optional<Tag> findByTagId(String tagId);
    boolean existsByTagId(String tagId);
}
