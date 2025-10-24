package com.payflow.dto;

public record PaymentResponse(String transactionId, String status, String message) {}
