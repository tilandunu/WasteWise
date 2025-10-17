package com.example.foundation.controller;

import com.example.foundation.dto.request.ApplyCreditsRequest;
import com.example.foundation.dto.request.PaymentRequest;
import com.example.foundation.dto.response.PaymentResponse;
import com.example.foundation.model.PaymentRecord;
import com.example.foundation.service.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getSummary(@PathVariable String userId) {
        Map<String, Object> body = new HashMap<>();
        body.put("balance", billingService.getBalance(userId));
        body.put("credits", billingService.getCredits(userId));
        body.put("payments", billingService.getPayments(userId));
        return ResponseEntity.ok(body);
    }

    @PostMapping("/{userId}/pay")
    public ResponseEntity<?> pay(@PathVariable String userId, @RequestBody PaymentRequest req) {
        try {
            PaymentResponse resp = billingService.pay(userId, req);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/{userId}/apply-credits")
    public ResponseEntity<?> applyCredits(@PathVariable String userId, @RequestBody ApplyCreditsRequest req) {
        try {
            PaymentResponse resp = billingService.applyCredits(userId, req);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
