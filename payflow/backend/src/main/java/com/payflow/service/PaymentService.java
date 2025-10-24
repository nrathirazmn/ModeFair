package com.payflow.service;

import com.payflow.dto.PaymentRequest;
import com.payflow.dto.PaymentResponse;
import com.payflow.entity.Transaction;
import com.payflow.repo.MerchantRepository;
import com.payflow.repo.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final MerchantRepository merchants;
    private final TransactionRepository txs;
    private final WebhookService webhook;

    public PaymentService(MerchantRepository merchants, TransactionRepository txs, WebhookService webhook) {
        this.merchants = merchants; this.txs = txs; this.webhook = webhook;
    }

    public PaymentResponse process(String apiKey, PaymentRequest req) {
        var merchant = merchants.findByApiKey(apiKey)
            .orElseThrow(() -> new RuntimeException("Invalid API key"));

        var tx = new Transaction();
        tx.setMerchantId(merchant.getId());
        tx.setAmount(req.amount());
        tx.setCurrency(req.currency() == null ? "MYR" : req.currency());
        tx.setCustomerEmail(req.customerEmail());
        tx.setCustomerName(req.customerName());
        tx.setCardLast4(req.cardNumber().substring(12));
        tx.setStatus(Transaction.Status.PENDING);
        
        var savedTx = txs.save(tx); 

        String cleanedCard = req.cardNumber() == null ? "" : req.cardNumber().replaceAll("\\s+", "");
        boolean success = cleanedCard.equals("4242424242424242");

        if (success) {
            savedTx.setStatus(Transaction.Status.SUCCEEDED);
        } else {
            savedTx.setStatus(Transaction.Status.FAILED);
            savedTx.setFailureReason("Card declined (test simulation)");
        }

        txs.save(savedTx);
        webhook.emitPaymentCompleted(merchant, savedTx);

        return new PaymentResponse(
                savedTx.getId(),
                savedTx.getStatus().name(),
                success ? "Payment successful" : "Payment failed"
        );
    }

    }
