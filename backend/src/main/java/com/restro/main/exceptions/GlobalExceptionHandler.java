package com.restro.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateEntryExc.class)
	public ResponseEntity<ErrorResponse> handleDuplicateEntry(DuplicateEntryExc ex) {
	    ErrorResponse response = new ErrorResponse();
	    response.setStatusCode(HttpStatus.CONFLICT.value()); 
	    response.setStatusMsg(ex.getMessage());  

	    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}


}
