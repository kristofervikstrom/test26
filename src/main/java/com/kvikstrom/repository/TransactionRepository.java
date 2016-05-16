package com.kvikstrom.repository;

import java.util.List;

import com.kvikstrom.dto.SumDTO;
import com.kvikstrom.dto.TransactionDTO;
import com.kvikstrom.model.Transaction;

public interface TransactionRepository {
	
	public Transaction insert(long transaction_id, TransactionDTO request);
	public Transaction findById(long transaction_id);
	public List<Long> findByType(String type);
	public SumDTO sum(long transaction_id);

}
