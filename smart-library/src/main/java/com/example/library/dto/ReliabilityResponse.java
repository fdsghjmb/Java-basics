package com.example.library.dto;

public record ReliabilityResponse(Long readerId, boolean reliable, int maxActiveLoans,
        int returnedLast30Days, boolean allReturnedOnTime, boolean hasOverdueActiveLoan) {}
