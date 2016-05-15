package com.kvikstrom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kvikstrom.dto.SumDTO;
import com.kvikstrom.dto.TransactionDTO;
import com.kvikstrom.exceptions.BadDataException;
import com.kvikstrom.exceptions.EntityNotFoundException;
import com.kvikstrom.model.Transaction;
import com.kvikstrom.service.TransactionService;


@RestController
@RequestMapping(value = "/transactionservice")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@RequestMapping(value = "/transaction/{transaction_id:[0-9]+}", method = RequestMethod.PUT)
	public HttpEntity<Void> insertTransaction(
			@PathVariable Long transaction_id, 
			@RequestBody TransactionDTO request) {
		if (request.getAmount() <= 0 
				|| request.getType() == null 
				|| request.getType().isEmpty()) {
			throw new BadDataException();
		}
		this.transactionService.insertTransaction(transaction_id, request);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/transaction/{transaction_id:[0-9]+}", method = RequestMethod.GET)
	@ResponseBody
	public Transaction getTransaction(@PathVariable Long transaction_id) {
		Transaction transaction = this.transactionService.getTransaction(transaction_id);
		if (transaction == null) {
			throw new EntityNotFoundException();
		}
		return transaction;
	}
	
	@RequestMapping(value = "/types/{type}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Long> getTransactionsByType(@PathVariable String type) {
		return this.transactionService.getTransactionByType(type);
		
	}
	
	@RequestMapping(value = "/sum/{transaction_id:[0-9]+}", method = RequestMethod.GET)
	@ResponseBody
	public SumDTO getSum(@PathVariable Long transaction_id) {
		return this.transactionService.getSum(transaction_id);
	}
	

}