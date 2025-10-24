package com.payflow;

import com.payflow.entity.Merchant;
import com.payflow.repo.MerchantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PayflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayflowApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(MerchantRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                Merchant m = new Merchant();
                m.setName("Test Merchant");
                m.setEmail("merchant@example.com");
                m.setPasswordHash("password123");
                m.setApiKey("TEST-1234567890"); 
                repo.save(m);
                System.out.println("✅ Created default test merchant with API key: " + m.getApiKey());
            }
        };
    }
}
