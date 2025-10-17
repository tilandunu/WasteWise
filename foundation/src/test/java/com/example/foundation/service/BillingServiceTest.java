package com.example.foundation.service;

import com.example.foundation.dto.request.ApplyCreditsRequest;
import com.example.foundation.dto.request.PaymentRequest;
import com.example.foundation.dto.response.PaymentResponse;
import com.example.foundation.model.BillingAccount;
import com.example.foundation.model.PaymentRecord;
import com.example.foundation.repository.BillingAccountRepository;
import com.example.foundation.repository.PaymentRecordRepository;
import com.example.foundation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class BillingServiceTest {

    private UserRepository userRepository;
    private PaymentRecordRepository paymentRecordRepository;
    private BillingAccountRepository billingAccountRepository;
    private BillingService billingService;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        paymentRecordRepository = mock(PaymentRecordRepository.class);
        billingAccountRepository = mock(BillingAccountRepository.class);

        billingService = new BillingService(userRepository, paymentRecordRepository, billingAccountRepository);
    }

    @Test
    @DisplayName("pay throws if amount <= 0")
    void pay_throws_on_non_positive_amount() {
        PaymentRequest req = new PaymentRequest();
        req.setAmount(0);

        assertThatThrownBy(() -> billingService.pay("user-1", req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Amount must be > 0");
    }

    @Test
    @DisplayName("applyCredits applies available credits and records payment")
    void applyCredits_success() {
        String userId = "u1";
        BillingAccount account = new BillingAccount(userId, 100.0, 50.0);

        given(billingAccountRepository.findByUserId(userId)).willReturn(Optional.of(account));
        given(billingAccountRepository.save(any(BillingAccount.class))).willAnswer(i -> i.getArgument(0));

        ApplyCreditsRequest req = new ApplyCreditsRequest();
        req.setCreditsToApply(30.0);

        PaymentResponse resp = billingService.applyCredits(userId, req);

        // credits reduced and balance reduced by applied credits
        verify(billingAccountRepository, atLeastOnce()).save(any(BillingAccount.class));
        verify(paymentRecordRepository).save(any(PaymentRecord.class));

        assertThat(resp.getStatus()).isEqualTo("SUCCESS");
        assertThat(resp.getRemainingBalance()).isEqualTo(100.0 - 30.0);
    }

    @Test
    @DisplayName("applyCredits throws when no credits available")
    void applyCredits_noCredits_throws() {
        String userId = "u2";
        BillingAccount account = new BillingAccount(userId, 100.0, 0.0);
        given(billingAccountRepository.findByUserId(userId)).willReturn(Optional.of(account));

        ApplyCreditsRequest req = new ApplyCreditsRequest();
        req.setCreditsToApply(10.0);

        assertThatThrownBy(() -> billingService.applyCredits(userId, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No credits available to apply");
    }

    @Test
    @DisplayName("awardCredits increases credits and records payment")
    void awardCredits_success() {
        String userId = "u3";
        BillingAccount account = new BillingAccount(userId, 0.0, 5.0);
        given(billingAccountRepository.findByUserId(userId)).willReturn(Optional.of(account));
        given(billingAccountRepository.save(any(BillingAccount.class))).willAnswer(i -> i.getArgument(0));

        PaymentResponse resp = billingService.awardCredits(userId, 10.0, "Reward");

        verify(billingAccountRepository).save(any(BillingAccount.class));
        verify(paymentRecordRepository).save(any(PaymentRecord.class));

        assertThat(resp.getStatus()).isEqualTo("SUCCESS");
    }

    @Test
    @DisplayName("generateMonthlyInvoices charges all accounts and creates pending invoices")
    void generateMonthlyInvoices_createsInvoices() {
        BillingAccount a1 = new BillingAccount("r1", 0.0, 0.0);
        BillingAccount a2 = new BillingAccount("r2", 10.0, 0.0);

        given(billingAccountRepository.findAll()).willReturn(List.of(a1, a2));
        given(billingAccountRepository.save(any(BillingAccount.class))).willAnswer(i -> i.getArgument(0));

        billingService.generateMonthlyInvoices(15.0);

        // both accounts should be saved with increased balance
        verify(billingAccountRepository, times(2)).save(any(BillingAccount.class));
        // two payment records should be created
        verify(paymentRecordRepository, times(2)).save(any(PaymentRecord.class));
    }

    @Test
    @DisplayName("setBalance and getBalance work")
    void set_and_get_balance() {
        String userId = "u4";
        BillingAccount acc = new BillingAccount(userId, 0.0, 0.0);
        given(billingAccountRepository.findByUserId(userId)).willReturn(Optional.of(acc));
        given(billingAccountRepository.save(any(BillingAccount.class))).willAnswer(i -> i.getArgument(0));

        billingService.setBalance(userId, 123.45);

        verify(billingAccountRepository).save(any(BillingAccount.class));
        double bal = billingService.getBalance(userId);
        assertThat(bal).isEqualTo(123.45);
    }

    @Test
    @DisplayName("addCredits and getCredits work")
    void add_and_get_credits() {
        String userId = "u5";
        BillingAccount acc = new BillingAccount(userId, 0.0, 2.0);
        given(billingAccountRepository.findByUserId(userId)).willReturn(Optional.of(acc));
        given(billingAccountRepository.save(any(BillingAccount.class))).willAnswer(i -> i.getArgument(0));

        billingService.addCredits(userId, 3.0);

        verify(billingAccountRepository).save(any(BillingAccount.class));
        assertThat(billingService.getCredits(userId)).isEqualTo(5.0);
    }

    @Test
    @DisplayName("pay records a payment (non-deterministic outcome but record saved)")
    void pay_records_payment() {
        String userId = "p1";
        BillingAccount acc = new BillingAccount(userId, 500.0, 0.0);
        given(billingAccountRepository.findByUserId(userId)).willReturn(Optional.of(acc));
        given(billingAccountRepository.save(any(BillingAccount.class))).willAnswer(i -> i.getArgument(0));

        PaymentRequest req = new PaymentRequest();
        req.setAmount(50.0);
        req.setMethod("card");

        PaymentResponse resp = billingService.pay(userId, req);

        // tx id returned and status is one of the expected values
        assertThat(resp.getTransactionId()).isNotNull();
        assertThat(resp.getStatus()).isIn("SUCCESS", "FAILED", "PENDING");

        // payment record persisted
        verify(paymentRecordRepository).save(any(PaymentRecord.class));
    }
}
