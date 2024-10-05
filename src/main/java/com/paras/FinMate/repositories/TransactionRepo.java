package com.paras.FinMate.repositories;

import com.paras.FinMate.entities.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepo extends JpaRepository<Transaction, String> {
    Optional<List<Transaction>> findAllByCustomer_Id (Integer customer_id, Pageable pageable);
}
