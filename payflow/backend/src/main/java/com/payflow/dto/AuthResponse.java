package com.payflow.dto;

public record AuthResponse(String token, String merchantId, String name, String email, String apiKey, String webhookUrl) {}
