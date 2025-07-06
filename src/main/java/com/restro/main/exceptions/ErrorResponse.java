package com.restro.main.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {
	
	private Integer statusCode;
	private Object statusMsg;

}
