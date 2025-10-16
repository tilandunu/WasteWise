package com.example.foundation.service.payrewards;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.foundation.model.payrewards.Account;
import com.example.foundation.model.payrewards.Bill;
import com.example.foundation.model.payrewards.BillStatus;
import com.example.foundation.model.payrewards.Payment;
import com.example.foundation.model.payrewards.PaymentMethod;
import com.example.foundation.model.payrewards.PaymentStatus;
import com.example.foundation.model.payrewards.Receipt;
import com.example.foundation.model.payrewards.RewardCredit;
import com.example.foundation.repository.payrewards.AccountRepository;
import com.example.foundation.repository.payrewards.BillRepository;
import com.example.foundation.repository.payrewards.PaymentMethodRepository;
import com.example.foundation.repository.payrewards.PaymentRepository;
import com.example.foundation.repository.payrewards.ReceiptRepository;
import com.example.foundation.repository.payrewards.RewardCreditRepository;

@Service
public class PaymentService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private RewardCreditRepository rewardCreditRepository;
    @Autowired
    private BillRepository billRepository;

    public BigDecimal getOutstandingBalance(String userId) {
        Account account = accountRepository.findByUser_Id(userId);
        return account != null ? account.getBalance() : BigDecimal.ZERO;
    // ...existing code...
}

    public List<Payment> getPaymentHistory(String userId) {
        return paymentRepository.findByAccount_User_Id(userId);
    }

    public List<RewardCredit> getAvailableCredits(String userId) {
        return rewardCreditRepository.findByAccount_User_IdAndEligibleTrue(userId);
    }

    public List<PaymentMethod> getSavedPaymentMethods(String userId) {
        return paymentMethodRepository.findByUser_Id(userId);
    }

    @Transactional
    public Payment makePayment(String userId, BigDecimal amount, String paymentMethodId) {
        Account account = accountRepository.findByUser_Id(userId);
        PaymentMethod method = paymentMethodRepository.findById(paymentMethodId).orElse(null);
        if (account == null || method == null || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid payment details");
        }
        // Find current open bill
        Bill bill = getCurrentOpenBill(account);
        if (bill == null) {
            throw new IllegalStateException("No open bill found for payment");
        }
        BigDecimal minPayment = bill.getAmountDue().multiply(new BigDecimal("0.35"));
        if (amount.compareTo(minPayment) < 0) {
            throw new IllegalArgumentException("Minimum payment is 35% of the bill amount");
        }
        Payment payment = new Payment();
        payment.setAccount(account);
        payment.setAmount(amount);
        payment.setPaymentMethod(method);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionId(generateTransactionId());
        payment.setDate(java.time.LocalDateTime.now());
        payment = paymentRepository.save(payment);
        // Simulate payment gateway
        boolean gatewaySuccess = mockPaymentGateway(amount, method);
        if (gatewaySuccess) {
            payment.setStatus(PaymentStatus.SUCCESS);
            // Link payment to bill
            if (bill.getPayments() == null) bill.setPayments(new java.util.ArrayList<>());
            bill.getPayments().add(payment);
            // Update bill paid amount
            bill.setAmountPaid(bill.getAmountPaid().add(amount));
            // Update bill status
            boolean paidInFull = false;
            if (bill.getAmountPaid().compareTo(bill.getAmountDue()) >= 0) {
                bill.setStatus(BillStatus.PAID);
                paidInFull = true;
            } else if (bill.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
                bill.setStatus(BillStatus.PARTIALLY_PAID);
            }
            billRepository.save(bill);
            // Update account balance
            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.save(account);
            Receipt receipt = new Receipt();
            receipt.setTransactionId(payment.getTransactionId());
            receipt.setDate(payment.getDate());
            receipt.setUser(account.getUser());
            receipt.setFileUrl("/receipts/" + payment.getTransactionId() + ".pdf");
            receiptRepository.save(receipt);
            payment.setReceipt(receipt);

            // Reward credits if bill paid in full
            if (paidInFull) {
                RewardCredit credit = new RewardCredit();
                credit.setAccount(account);
                credit.setAmount(new BigDecimal("5"));
                credit.setEligible(true);
                credit.setDateEarned(java.time.LocalDateTime.now());
                rewardCreditRepository.save(credit);
            }
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }
        return paymentRepository.save(payment);
    }

    // Helper to get current open bill for an account
    private Bill getCurrentOpenBill(Account account) {
        String currentPeriod = LocalDate.now().getYear() + "-" + String.format("%02d", LocalDate.now().getMonthValue());
        Bill bill = billRepository.findByAccount_IdAndBillingPeriod(account.getId(), currentPeriod);
        if (bill == null) {
            // Carry over unpaid from previous bill
            List<Bill> bills = billRepository.findByAccount_User_Id(account.getUser().getId());
            BigDecimal carryOver = BigDecimal.ZERO;
            for (Bill b : bills) {
                if (b.getStatus() != BillStatus.PAID) {
                    carryOver = carryOver.add(b.getAmountDue().subtract(b.getAmountPaid()));
                }
            }
            bill = new Bill();
            bill.setAccount(account);
            bill.setBillingPeriod(currentPeriod);
            bill.setAmountDue(carryOver); // You may add new charges here
            bill.setAmountPaid(BigDecimal.ZERO);
            bill.setStatus(BillStatus.OPEN);
            bill.setCreatedDate(LocalDate.now());
            bill.setDueDate(LocalDate.now().plusDays(30));
            billRepository.save(bill);
        }
        return bill;
    }
 

    @Transactional
    public boolean applyRewardCredits(String userId) {
        Account account = accountRepository.findByUser_Id(userId);
        List<RewardCredit> credits = rewardCreditRepository.findByAccount_User_IdAndEligibleTrue(userId);
        if (account == null || credits.isEmpty()) return false;
        BigDecimal totalCredit = credits.stream().map(RewardCredit::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        account.setBalance(account.getBalance().subtract(totalCredit));
        accountRepository.save(account);
        for (RewardCredit credit : credits) {
            credit.setEligible(false);
            credit.setDateApplied(java.time.LocalDateTime.now());
            rewardCreditRepository.save(credit);
        }
        return true;
    }

    private boolean mockPaymentGateway(BigDecimal amount, PaymentMethod method) {
        // Simulate payment gateway response
        return true; // Always success for mock
    }

    private String generateTransactionId() {
        return java.util.UUID.randomUUID().toString();
    }
}
