package com.example.foundation.dto.request;

public class ApplyCreditsRequest {
    private double creditsToApply; // amount of credits to apply in LKR

    public ApplyCreditsRequest() {}

    public double getCreditsToApply() {
        return creditsToApply;
    }

    public void setCreditsToApply(double creditsToApply) {
        this.creditsToApply = creditsToApply;
    }
}
