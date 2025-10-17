package com.example.foundation.service;

import com.example.foundation.model.Bin;
import com.example.foundation.model.CollectionEvent;
import com.example.foundation.repository.CollectionEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CollectionEventService {

    @Autowired
    private CollectionEventRepository collectionEventRepository;

    /**
     * Create a new collection event for a bin with the given weight.
     * Automatically sets collectedDate to current time.
     */
    public CollectionEvent createCollectionEvent(Bin assignedBin, float weight) {
        CollectionEvent event = new CollectionEvent(assignedBin, weight);
        event.setCollectedDate(new Date()); // explicitly set collected date
        return collectionEventRepository.save(event);
    }
}
