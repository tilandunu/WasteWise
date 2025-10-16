package com.example.foundation.controller;

import com.example.foundation.dto.request.RegisterBinRequest;
import com.example.foundation.dto.response.BinResponse;
import com.example.foundation.service.BinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bins")
public class BinController {


    // Inject the BinService
    private final BinService binService;


    // Dependency injection
    public BinController(BinService binService) {
        this.binService = binService;
    }

    // Create a new bin
    @PostMapping("/create")
    public ResponseEntity<?> registerBin(@RequestBody RegisterBinRequest request) {
        try {
            BinResponse response = binService.registerBin(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
