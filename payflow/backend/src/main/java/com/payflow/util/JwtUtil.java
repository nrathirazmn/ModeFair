package com.payflow.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
  private final Key key;
  private final long expiry;

  public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expirySeconds}") long expirySeconds) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
    this.expiry = expirySeconds * 1000;
  }

  public String issue(String subject) {
    long now = System.currentTimeMillis();
    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(new Date(now))
        .setExpiration(new Date(now + expiry))
        .signWith(key)
        .compact();
  }

  public String subject(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
  }
}
