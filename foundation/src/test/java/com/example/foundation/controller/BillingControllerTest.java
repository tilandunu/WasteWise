package com.example.foundation.controller;

import com.example.foundation.dto.request.ApplyCreditsRequest;
import com.example.foundation.dto.request.PaymentRequest;
import com.example.foundation.dto.response.PaymentResponse;
import com.example.foundation.service.BillingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillingControllerTest {
    @Mock
    private BillingService billingService;
    @InjectMocks
    private BillingController billingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSummary() {
        when(billingService.getBalance("user1")).thenReturn(100.0);
        when(billingService.getCredits("user1")).thenReturn(10.0);
        when(billingService.getPayments("user1")).thenReturn(List.of("payment1"));
        ResponseEntity<?> response = billingController.getSummary("user1");
        assertEquals(200, response.getStatusCodeValue());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(100.0, body.get("balance"));
        assertEquals(10.0, body.get("credits"));
        assertEquals(List.of("payment1"), body.get("payments"));
    }

    @Test
    void testPay_Success() {
        PaymentRequest req = mock(PaymentRequest.class);
        PaymentResponse resp = mock(PaymentResponse.class);
        when(billingService.pay("user1", req)).thenReturn(resp);
        ResponseEntity<?> response = billingController.pay("user1", req);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(resp, response.getBody());
    }

    @Test
    void testPay_IllegalArgumentException() {
        PaymentRequest req = mock(PaymentRequest.class);
        when(billingService.pay("user1", req)).thenThrow(new IllegalArgumentException("Invalid payment"));
        ResponseEntity<?> response = billingController.pay("user1", req);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid payment", response.getBody());
    }

    @Test
    void testPay_RuntimeException() {
        PaymentRequest req = mock(PaymentRequest.class);
        when(billingService.pay("user1", req)).thenThrow(new RuntimeException("Server error"));
        ResponseEntity<?> response = billingController.pay("user1", req);
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Server error", response.getBody());
    }

    @Test
    void testApplyCredits_Success() {
        ApplyCreditsRequest req = mock(ApplyCreditsRequest.class);
        PaymentResponse resp = mock(PaymentResponse.class);
        when(billingService.applyCredits("user1", req)).thenReturn(resp);
        ResponseEntity<?> response = billingController.applyCredits("user1", req);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(resp, response.getBody());
    }

    @Test
    void testApplyCredits_IllegalArgumentException() {
        ApplyCreditsRequest req = mock(ApplyCreditsRequest.class);
        when(billingService.applyCredits("user1", req)).thenThrow(new IllegalArgumentException("Invalid credits"));
        ResponseEntity<?> response = billingController.applyCredits("user1", req);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid credits", response.getBody());
    }

    @Test
    void testApplyCredits_RuntimeException() {
        ApplyCreditsRequest req = mock(ApplyCreditsRequest.class);
        when(billingService.applyCredits("user1", req)).thenThrow(new RuntimeException("Server error"));
        ResponseEntity<?> response = billingController.applyCredits("user1", req);
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Server error", response.getBody());
    }

    @Test
    void testGenerateMonthly_Success() {
        ResponseEntity<?> response = billingController.generateMonthly(50.0);
        verify(billingService).generateMonthlyInvoices(50.0);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Monthly invoices generated", response.getBody());
    }

    @Test
    void testGenerateMonthly_IllegalArgumentException() {
        doThrow(new IllegalArgumentException("Invalid amount")).when(billingService).generateMonthlyInvoices(50.0);
        ResponseEntity<?> response = billingController.generateMonthly(50.0);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid amount", response.getBody());
    }

    @Test
    void testGenerateMonthly_RuntimeException() {
        doThrow(new RuntimeException("Server error")).when(billingService).generateMonthlyInvoices(50.0);
        ResponseEntity<?> response = billingController.generateMonthly(50.0);
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Server error", response.getBody());
    }
}
