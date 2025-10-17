package com.example.foundation.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.foundation.dto.request.ApplyCreditsRequest;
import com.example.foundation.dto.request.PaymentRequest;
import com.example.foundation.dto.response.PaymentResponse;
import com.example.foundation.model.BillingAccount;
import com.example.foundation.model.PaymentRecord;
import com.example.foundation.repository.BillingAccountRepository;
import com.example.foundation.repository.PaymentRecordRepository;
import com.example.foundation.repository.UserRepository;

@Service
public class BillingService {

    // In-memory cache for payment history only (optional)
    private final Map<String, List<PaymentRecord>> paymentsByUser = new HashMap<>();

    private final UserRepository userRepository;
    private final PaymentRecordRepository paymentRecordRepository;
    private final BillingAccountRepository billingAccountRepository;

    public BillingService(UserRepository userRepository,
                          PaymentRecordRepository paymentRecordRepository,
                          BillingAccountRepository billingAccountRepository) {
        this.userRepository = userRepository;
        this.paymentRecordRepository = paymentRecordRepository;
        this.billingAccountRepository = billingAccountRepository;
    }

    // Initialize an account balance for a user if not present
    private BillingAccount ensureAccount(String userId) {
        paymentsByUser.putIfAbsent(userId, new ArrayList<>());
        return billingAccountRepository.findByUserId(userId)
                .orElseGet(() -> billingAccountRepository.save(new com.example.foundation.model.BillingAccount(userId, 0.0, 0.0)));
    }

    public synchronized PaymentResponse pay(String userId, PaymentRequest req) {
    BillingAccount account = ensureAccount(userId);

        if (req.getAmount() <= 0)
            throw new IllegalArgumentException("Amount must be > 0");

        // Simulate network/gateway: 90% success, 8% declined, 2% timeout -> pending
        double rnd = Math.random();
        PaymentRecord.Status status;
        String txId = UUID.randomUUID().toString();
        String method = req.getMethod() == null ? "UNKNOWN" : req.getMethod().toUpperCase();

        if (rnd < 0.90) {
            status = PaymentRecord.Status.SUCCESS;
            // apply amount to balance (reduce)
            double remaining = account.getBalance() - req.getAmount();
            account.setBalance(remaining);
            billingAccountRepository.save(account);
        } else if (rnd < 0.98) {
            status = PaymentRecord.Status.FAILED;
        } else {
            status = PaymentRecord.Status.PENDING;
        }

    PaymentRecord record = new PaymentRecord(txId, userId, req.getAmount(), method, status, Instant.now(), req.getDetails());
    // persist to Mongo
    paymentRecordRepository.save(record);
    // update optional cache
    paymentsByUser.get(userId).add(record);

    double remaining = account.getBalance();

    return new PaymentResponse(txId, status.name(), remaining);
    }

    public synchronized PaymentResponse applyCredits(String userId, ApplyCreditsRequest req) {
        BillingAccount account = ensureAccount(userId);

        double available = account.getCredits();
        double toApply = Math.min(available, req.getCreditsToApply());
        if (toApply <= 0)
            throw new IllegalArgumentException("No credits available to apply");

        account.setCredits(available - toApply);
        account.setBalance(account.getBalance() - toApply);
        billingAccountRepository.save(account);

        String txId = "CREDIT-" + UUID.randomUUID();
        PaymentRecord record = new PaymentRecord(txId, userId, toApply, "CREDIT", PaymentRecord.Status.SUCCESS, Instant.now(), "Applied recycling credits");
        paymentRecordRepository.save(record);
        paymentsByUser.get(userId).add(record);

        return new PaymentResponse(txId, "SUCCESS", account.getBalance());
    }

    // Helpers for tests / UI
    public double getBalance(String userId) {
        BillingAccount account = ensureAccount(userId);
        return account.getBalance();
    }

    public double getCredits(String userId) {
        BillingAccount account = ensureAccount(userId);
        return account.getCredits();
    }

    public List<PaymentRecord> getPayments(String userId) {
        ensureAccount(userId);
        // return persisted payments (keeps in sync across instances)
        return paymentRecordRepository.findByUserId(userId);
    }

    // Admin/test helper to set balances/credits quickly (not exposed via controller)
    public void setBalance(String userId, double amount) {
        BillingAccount account = ensureAccount(userId);
        account.setBalance(amount);
        billingAccountRepository.save(account);
    }

    public void addCredits(String userId, double amount) {
        BillingAccount account = ensureAccount(userId);
        account.setCredits(account.getCredits() + amount);
        billingAccountRepository.save(account);
    }

    /**
     * Award credits (LKR) to a user's billing account and create a PaymentRecord note.
     */
    public PaymentResponse awardCredits(String userId, double amount, String note) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");

        BillingAccount account = ensureAccount(userId);
        account.setCredits(account.getCredits() + amount);
        billingAccountRepository.save(account);

        String txId = "CREDIT-" + UUID.randomUUID();
        PaymentRecord record = new PaymentRecord(txId, userId, amount, "CREDIT", PaymentRecord.Status.SUCCESS, Instant.now(), note);
        paymentRecordRepository.save(record);
        paymentsByUser.get(userId).add(record);

        return new PaymentResponse(txId, "SUCCESS", account.getBalance());
    }

    /**
     * Generate a monthly invoice for all billing accounts by adding a charge to each account.
     * This creates a PaymentRecord with method INVOICE and status PENDING so residents can pay manually.
     */
    public void generateMonthlyInvoices(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Invoice amount must be > 0");

        var accounts = billingAccountRepository.findAll();
        for (var account : accounts) {
            account.setBalance(account.getBalance() + amount);
            billingAccountRepository.save(account);

            String txId = "INVOICE-" + UUID.randomUUID();
            PaymentRecord record = new PaymentRecord(txId, account.getUserId(), amount, "INVOICE", PaymentRecord.Status.PENDING, Instant.now(), "Monthly invoice generated");
            paymentRecordRepository.save(record);
            paymentsByUser.computeIfAbsent(account.getUserId(), k -> new java.util.ArrayList<>()).add(record);
        }
    }

}
