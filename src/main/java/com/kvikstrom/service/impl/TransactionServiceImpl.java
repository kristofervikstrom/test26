package com.kvikstrom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import com.kvikstrom.dto.SumDTO;
import com.kvikstrom.dto.TransactionDTO;
import com.kvikstrom.model.Transaction;
import com.kvikstrom.repository.TransactionRepository;
import com.kvikstrom.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository repository;
	
	@Override
	public HttpEntity<Void> insertTransaction(long transaction_id, TransactionDTO request) {
		return this.repository.insert(transaction_id, request);
	}
	
	@Override
	public Transaction getTransaction(long transaction_id) {
		return this.repository.findById(transaction_id);
	}

	@Override
	public List<Long> getTransactionByType(String type) {
		return this.repository.findByType(type);
	}

	@Override
	public SumDTO getSum(long transaction_id) {
		return this.repository.sum(transaction_id);
		
	}


	
	

}
