package com.payflow.controller;

import com.payflow.dto.PaymentRequest;
import com.payflow.dto.PaymentResponse;
import com.payflow.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentController {
  private final PaymentService payments;
  public PaymentController(PaymentService payments) { this.payments = payments; }

  @PostMapping("/api/payments")
  public ResponseEntity<PaymentResponse> pay(
      @RequestHeader("X-PAYFLOW-KEY") String apiKey,
      @Valid @RequestBody PaymentRequest req) {
    return ResponseEntity.ok(payments.process(apiKey, req));
  }
}
