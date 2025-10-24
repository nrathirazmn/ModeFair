package com.payflow.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID) 
    @Column(updatable = false, nullable = false)
    private UUID id;

  private String merchantId;

  private long amount; // cents

  private String currency = "MYR";

  private String customerEmail;

  private String customerName;

  private String cardLast4;

  @Enumerated(EnumType.STRING)
  private Status status;

  private String failureReason;

  private Instant createdAt = Instant.now();

  public enum Status { PENDING, SUCCEEDED, FAILED }

  // --- getters & setters ---

  public String getId() { 
        // Convert UUID to String for your PaymentService response
        return id != null ? id.toString() : null; 
    }

  public String getMerchantId() { return merchantId; }
  public void setMerchantId(String merchantId) { this.merchantId = merchantId; }

  public long getAmount() { return amount; }
  public void setAmount(long amount) { this.amount = amount; }

  public String getCurrency() { return currency; }
  public void setCurrency(String currency) { this.currency = currency; }

  public String getCustomerEmail() { return customerEmail; }
  public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

  public String getCustomerName() { return customerName; }
  public void setCustomerName(String customerName) { this.customerName = customerName; }

  public String getCardLast4() { return cardLast4; }
  public void setCardLast4(String cardLast4) { this.cardLast4 = cardLast4; }

  public Status getStatus() { return status; }
  public void setStatus(Status status) { this.status = status; }

  public String getFailureReason() { return failureReason; }
  public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

  public Instant getCreatedAt() { return createdAt; }
}
