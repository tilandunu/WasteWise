package com.example.foundation.service;

import com.example.foundation.model.Bin;
import com.example.foundation.model.Resident;
import com.example.foundation.model.Tag;
import com.example.foundation.model.User;
import com.example.foundation.repository.BinRepository;
import com.example.foundation.repository.TagRepository;
import com.example.foundation.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class OfficerService {

    private final BinRepository binRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public OfficerService(BinRepository binRepository, TagRepository tagRepository, UserRepository userRepository) {
        this.binRepository = binRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    public Bin assignTagToBin(String binId, String tagId) {
        Bin bin = binRepository.findById(binId)
                .orElseThrow(() -> new RuntimeException("Bin not found"));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        if (!tag.isActive()) {
            throw new RuntimeException("Tag is already assigned");
        }

        bin.setTag(tag);
        bin.setStatus("ASSIGNED");
        tag.setActive(false); // mark tag as assigned
        tagRepository.save(tag);

        return binRepository.save(bin);
    }

    public Bin assignBinToResident(String binId, String residentId) {
        Bin bin = binRepository.findById(binId)
                .orElseThrow(() -> new RuntimeException("Bin not found"));

        User resident = userRepository.findById(residentId)
                .orElseThrow(() -> new RuntimeException("Resident not found"));

        // Assign bin to resident
        bin.setAssignedUser(resident);

        // Activate resident account
        if (resident instanceof Resident) {
            ((Resident) resident).activateAccount();
            userRepository.save(resident);
        }

        // Save updated bin
        return binRepository.save(bin);
    }

}
