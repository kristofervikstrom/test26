package com.kvikstrom.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kvikstrom.dto.SumDTO;
import com.kvikstrom.dto.TransactionDTO;
import com.kvikstrom.exceptions.DuplicateIdException;
import com.kvikstrom.model.Transaction;
import com.kvikstrom.repository.TransactionRepository;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {
	
	private static final Map<Long, Transaction> dataStore = new HashMap<Long, Transaction>();
	private static final Map<Long, List<Transaction>> parentStore = new HashMap<Long, List<Transaction>>();

	@Override
	public Transaction insert(long transaction_id, TransactionDTO request) {
		if (findById(transaction_id) != null) {
			throw new DuplicateIdException();
		}
		Transaction transaction = new Transaction();
		transaction.setTransaction_id(transaction_id);
		transaction.setAmount(request.getAmount());
		transaction.setParent_id(request.getParent_id() != null ? request.getParent_id() : null);
		transaction.setType(request.getType());
		
		if (request.getParent_id() != null) {
			List<Transaction> transactions = parentStore.get(request.getParent_id());
			if (transactions == null) {
				transactions = new ArrayList<>();
			}
			transactions.add(transaction);
			parentStore.put(request.getParent_id(), transactions);
		}
		
		dataStore.put(transaction_id, transaction);
		
		return transaction;
	}

	@Override
	public Transaction findById(long transaction_id) {
		return dataStore.get(transaction_id);
	}
	
	@Override
	public List<Long> findByType(String type) {
		List<Long> transactionsByType = new ArrayList<>();
		for (Transaction transaction : dataStore.values()) {
			if (transaction.getType().equals(type)) {
				transactionsByType.add(transaction.getTransaction_id());
			}
		}
		return transactionsByType;
	}

	@Override
	public SumDTO sum(long transaction_id) {
		List<Transaction> transactions = parentStore.get(transaction_id);
		SumDTO sumDTO = new SumDTO();
		double sum = findById(transaction_id).getAmount();
		if (transactions != null) {
			for (Transaction transaction : transactions) {
				sum = sum + transaction.getAmount();
			}
		}
		sumDTO.setSum(sum);
		return sumDTO;
	}

}
