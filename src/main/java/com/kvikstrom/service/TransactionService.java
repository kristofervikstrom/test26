package com.kvikstrom.service;

import java.util.List;

import org.springframework.http.HttpEntity;

import com.kvikstrom.dto.SumDTO;
import com.kvikstrom.dto.TransactionDTO;
import com.kvikstrom.model.Transaction;

public interface TransactionService {
	
	public Transaction getTransaction(long transaction_id);
	public HttpEntity<Void> insertTransaction(long transaction_id, TransactionDTO request);
	public List<Long> getTransactionByType(String type);
	public SumDTO getSum(long transaction_id);

}
