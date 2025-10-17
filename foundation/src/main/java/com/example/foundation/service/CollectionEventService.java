package com.example.foundation.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.foundation.model.Bin;
import com.example.foundation.model.CollectionEvent;
import com.example.foundation.model.User;
import com.example.foundation.repository.CollectionEventRepository;

@Service
public class CollectionEventService {

    @Autowired
    private CollectionEventRepository collectionEventRepository;

    @Autowired
    private BillingService billingService;

    /**
     * Create a new collection event for a bin with the given weight.
     * Automatically sets collectedDate to current time.
     */
    public CollectionEvent createCollectionEvent(Bin assignedBin, float weight) {
        CollectionEvent event = new CollectionEvent(assignedBin, weight);
        event.setCollectedDate(new Date()); // explicitly set collected date
        CollectionEvent saved = collectionEventRepository.save(event);

        // Award credits to the assigned user (if any) — 50 LKR per kg
        if (assignedBin.getAssignedUser() != null) {
            User user = assignedBin.getAssignedUser();
            try {
                double kg = weight; // assuming weight is in kg
                double creditAmount = 10.0 * kg;
                String note = "Recycling credit for collection event: bin=" + assignedBin.getBinCode() + " weight=" + kg;
                billingService.awardCredits(user.getId(), creditAmount, note);
            } catch (Exception e) {
                // log but don't fail the collection event
                e.printStackTrace();
            }
        }

        return saved;
    }
}
