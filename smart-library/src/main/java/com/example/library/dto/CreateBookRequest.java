package com.example.library.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateBookRequest(@NotBlank String title, @NotBlank String author) {}
