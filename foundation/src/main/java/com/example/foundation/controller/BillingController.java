package com.example.foundation.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foundation.dto.request.ApplyCreditsRequest;
import com.example.foundation.dto.request.PaymentRequest;
import com.example.foundation.dto.response.PaymentResponse;
import com.example.foundation.service.BillingService;

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
