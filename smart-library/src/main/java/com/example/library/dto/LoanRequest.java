package com.example.library.dto;

import jakarta.validation.constraints.NotNull;

public record LoanRequest(@NotNull Long readerId, @NotNull Long bookId) {}
