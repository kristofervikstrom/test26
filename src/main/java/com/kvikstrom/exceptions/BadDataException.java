package com.kvikstrom.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid data")
public class BadDataException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8445150380581026920L;

}
