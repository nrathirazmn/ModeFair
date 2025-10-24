package com.payflow.controller;

import com.payflow.entity.Merchant;
import com.payflow.repo.MerchantRepository;
import com.payflow.repo.TransactionRepository;
import com.payflow.util.JwtUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchants/me")
public class MerchantController {
  private final MerchantRepository merchants;
  private final TransactionRepository txs;
  private final JwtUtil jwt;

  public MerchantController(MerchantRepository merchants, TransactionRepository txs, JwtUtil jwt) {
    this.merchants = merchants; this.txs = txs; this.jwt = jwt;
  }

  private Merchant me(String bearer) {
    String token = bearer.replace("Bearer ", "");
    String id = jwt.subject(token);
    return merchants.findById(id).orElseThrow();
  }

  @GetMapping
  public ResponseEntity<?> profile(@RequestHeader("Authorization") String bearer) {
    var me = me(bearer);
    record Profile(String id, String name, String email, String apiKey, String webhookUrl) {}
    return ResponseEntity.ok(new Profile(me.getId(), me.getName(), me.getEmail(), me.getApiKey(), me.getWebhookUrl()));
  }

  @PutMapping("/webhook")
  public ResponseEntity<?> setWebhook(@RequestHeader("Authorization") String bearer, @RequestBody String url) {
    var me = me(bearer);
    me.setWebhookUrl(url.replace("\"", ""));
    merchants.save(me);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/transactions")
  public ResponseEntity<?> transactions(@RequestHeader("Authorization") String bearer,
                                        @RequestParam(defaultValue="0") int page,
                                        @RequestParam(defaultValue="20") int size) {
    var me = me(bearer);
    return ResponseEntity.ok(txs.findByMerchantIdOrderByCreatedAtDesc(me.getId(), PageRequest.of(page, size)));
  }
}
