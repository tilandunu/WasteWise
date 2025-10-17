package com.example.foundation.service;

import com.example.foundation.model.Bin;
import com.example.foundation.model.Tag;
import com.example.foundation.repository.BinRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BinService {

    private final BinRepository binRepository;
    private final TagService tagService;

    //Constructor
    public BinService(BinRepository binRepository , TagService tagService) {
        this.binRepository = binRepository;
        this.tagService = tagService;
    }

    

    // Get all unassigned bins
    public List<Bin> getUnassignedBins() {
        return binRepository.findBinsByStatus("IN_STOCK");
    }

    //Get all assigned bins
    public List<Bin> getAssignedBins() {
        return binRepository.findBinsByStatus("ASSIGNED");
    }

    //Scan Bin
    public Tag scanBin(String tagId) {
        return tagService.scanTag(tagId);
    }

}
