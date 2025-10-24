package com.payflow.repo;

import com.payflow.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
  Page<Transaction> findByMerchantIdOrderByCreatedAtDesc(String merchantId, Pageable pageable);
}
