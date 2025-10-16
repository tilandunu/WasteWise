package com.example.foundation.service;

import com.example.foundation.model.Bin;
import com.example.foundation.model.Tag;
import com.example.foundation.model.Premises;
import com.example.foundation.dto.request.RegisterBinRequest;
import com.example.foundation.dto.response.BinResponse;
import com.example.foundation.repository.BinRepository;
import com.example.foundation.repository.TagRepository;
import com.example.foundation.repository.PremisesRepository;
import org.springframework.stereotype.Service;

@Service
public class BinService {

    private final BinRepository binRepository;
    private final TagRepository tagRepository;
    private final PremisesRepository premisesRepository;

    public BinService(BinRepository binRepository, TagRepository tagRepository, PremisesRepository premisesRepository) {
        this.binRepository = binRepository;
        this.tagRepository = tagRepository;
        this.premisesRepository = premisesRepository;
    }

    public BinResponse registerBin(RegisterBinRequest request) {
        Bin bin = new Bin();
        bin.setBinCode(request.getBinCode());
        bin.setType(request.getType());
        bin.setStatus("IN_STOCK"); // default status

        // Optional: Attach tag if provided
        if (request.getTagId() != null && !request.getTagId().isEmpty()) {
            Tag tag = tagRepository.findById(request.getTagId())
                    .orElseThrow(() -> new RuntimeException("Tag not found with id: " + request.getTagId()));
            bin.setTag(tag);
        }

        // Optional: Attach premises if provided
        if (request.getPremisesId() != null && !request.getPremisesId().isEmpty()) {
            Premises premises = premisesRepository.findById(request.getPremisesId())
                    .orElseThrow(() -> new RuntimeException("Premises not found with id: " + request.getPremisesId()));
            bin.setPremises(premises);
        }

        // Save bin in DB
        Bin savedBin = binRepository.save(bin);

        // Return response DTO
        return new BinResponse(
                savedBin.getId(),
                savedBin.getBinCode());
    }
}