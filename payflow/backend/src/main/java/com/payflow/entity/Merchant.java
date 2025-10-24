package com.payflow.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Merchant {
  @Id
  private String id = UUID.randomUUID().toString();

  private String name;

  @Column(unique = true)
  private String email;

  private String passwordHash;

  @Column(unique = true)
  private String apiKey;

  private String webhookUrl;

  private Instant createdAt = Instant.now();

  // --- getters & setters ---

  public String getId() { return id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

  public String getApiKey() { return apiKey; }
  public void setApiKey(String apiKey) { this.apiKey = apiKey; }

  public String getWebhookUrl() { return webhookUrl; }
  public void setWebhookUrl(String webhookUrl) { this.webhookUrl = webhookUrl; }

  public Instant getCreatedAt() { return createdAt; }
}
