package com.kvikstrom.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Duplicate id")
public class DuplicateIdException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7584169645412788287L;

}
