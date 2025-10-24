package com.payflow.dto;

import jakarta.validation.constraints.*;

public record PaymentRequest(
  @Min(1) long amount,
  String currency,
  @Email String customerEmail,
  @NotBlank String customerName,
  @Pattern(regexp = "\\d{16}") String cardNumber,
  @Pattern(regexp = "\\d{2}/\\d{2}") String expiry,
  @Pattern(regexp = "\\d{3}") String cvv,
  String country,
  String postcode
) {}
