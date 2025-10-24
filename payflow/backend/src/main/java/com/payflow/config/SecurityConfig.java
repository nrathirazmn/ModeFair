package com.payflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll() // Explicit POST allowance
        .requestMatchers("/api/auth/**", "/api/payments", "/api/health").permitAll()
        .requestMatchers("/api/merchants/**").permitAll()   
        .anyRequest().authenticated()
      )

      .cors(c -> c.configurationSource(corsConfigurationSource()));
    return http.build();
  }

  @Bean
CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration cfg = new CorsConfiguration();
    cfg.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000")); 
    cfg.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-PAYFLOW-KEY")); 
    cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
    
    // 🌟 CRITICAL FIX: Change to TRUE 🌟
    // Resolves issues with complex headers (like X-PAYFLOW-KEY) not being allowed
    cfg.setAllowCredentials(true); 
    
    UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
    src.registerCorsConfiguration("/**", cfg);
    return src;
}
}
