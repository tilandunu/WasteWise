package com.example.foundation.service;

import com.example.foundation.model.Bin;
import com.example.foundation.repository.BinRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BinService {

    private final BinRepository binRepository;

    public BinService(BinRepository binRepository) {
        this.binRepository = binRepository;
    }

    public List<Bin> getUnassignedBins() {
        return binRepository.findAll();
    }
}
