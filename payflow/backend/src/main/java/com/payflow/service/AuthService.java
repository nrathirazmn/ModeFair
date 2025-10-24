package com.payflow.service;

import com.payflow.dto.*;
import com.payflow.entity.Merchant;
import com.payflow.repo.MerchantRepository;
import com.payflow.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
  private final MerchantRepository merchants;
  private final JwtUtil jwt;
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  public AuthService(MerchantRepository merchants, JwtUtil jwt) {
    this.merchants = merchants; this.jwt = jwt;
  }

  public AuthResponse register(RegisterRequest req) {
    Merchant m = new Merchant();
    m.setName(req.name());
    m.setEmail(req.email());
    m.setPasswordHash(encoder.encode(req.password()));
    m.setApiKey("pf_" + UUID.randomUUID());
    merchants.save(m);
    String token = jwt.issue(m.getId());
    return new AuthResponse(token, m.getId(), m.getName(), m.getEmail(), m.getApiKey(), m.getWebhookUrl());
  }

  public AuthResponse login(LoginRequest req) {
    Merchant m = merchants.findByEmail(req.email()).orElseThrow(() -> new RuntimeException("Invalid credentials"));
    if (!encoder.matches(req.password(), m.getPasswordHash())) throw new RuntimeException("Invalid credentials");
    String token = jwt.issue(m.getId());
    return new AuthResponse(token, m.getId(), m.getName(), m.getEmail(), m.getApiKey(), m.getWebhookUrl());
  }
}
