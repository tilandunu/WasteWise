package com.example.foundation.controller.payrewards;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.foundation.model.payrewards.Payment;
import com.example.foundation.model.payrewards.PaymentMethod;
import com.example.foundation.model.payrewards.Receipt;
import com.example.foundation.service.payrewards.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/balance/{userId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String userId) {
        return ResponseEntity.ok(paymentService.getOutstandingBalance(userId));
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Payment>> getPaymentHistory(@PathVariable String userId) {
        return ResponseEntity.ok(paymentService.getPaymentHistory(userId));
    }

    @GetMapping("/methods/{userId}")
    public ResponseEntity<List<PaymentMethod>> getSavedMethods(@PathVariable String userId) {
        return ResponseEntity.ok(paymentService.getSavedPaymentMethods(userId));
    }

    @PostMapping("/pay")
    public ResponseEntity<Payment> makePayment(@RequestParam String userId,
                                               @RequestParam BigDecimal amount,
                                               @RequestParam String paymentMethodId) {
        try {
            Payment payment = paymentService.makePayment(userId, amount, paymentMethodId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/receipt/{transactionId}")
    public ResponseEntity<Receipt> getReceipt(@PathVariable String transactionId) {
        // Implement logic to fetch receipt by transactionId
        return ResponseEntity.notFound().build();
    }
}
