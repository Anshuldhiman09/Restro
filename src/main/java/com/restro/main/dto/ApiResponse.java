package com.restro.main.dto;

import lombok.Data;

@Data
public class ApiResponse<T>{
    
	 private Integer statusCode ;
	 
	 private String statusmessage;
	 
	 private T data ;
	 
}
