package com.payflow.service;

import com.payflow.entity.Merchant;
import com.payflow.entity.Transaction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WebhookService {
  private final RestClient http = RestClient.create();

  public void emitPaymentCompleted(Merchant m, Transaction tx) {
    if (m.getWebhookUrl() == null || m.getWebhookUrl().isBlank()) return;

    String payload = "{"
        + "\"event\":\"payment.completed\","
        + "\"data\":{"
        + "\"transactionId\":\"" + tx.getId() + "\","
        + "\"status\":\"" + tx.getStatus().name() + "\","
        + "\"amount\":" + tx.getAmount() + ","
        + "\"currency\":\"" + tx.getCurrency() + "\""
        + "}}";

    try {
      http.post()
          .uri(m.getWebhookUrl())
          .contentType(MediaType.APPLICATION_JSON)
          .body(payload)
          .retrieve()
          .toBodilessEntity();
    } catch (Exception ignored) {}
  }
}
