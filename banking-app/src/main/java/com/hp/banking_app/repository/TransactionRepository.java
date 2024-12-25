package com.hp.banking_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hp.banking_app.entity.Transaction;

public interface TransactionRepository  extends JpaRepository<Transaction, Long>{
	

	List<Transaction> findByAccountIdOrderByTimestampDesc(Long accountId);
}
