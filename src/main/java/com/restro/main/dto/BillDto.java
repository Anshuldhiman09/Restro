package com.restro.main.dto;

import lombok.Data;

@Data
public class BillDto {

	private String itemName;
	private Long itemQuantity;
	private Double itemPrice;
	private Double totalItemPrice;

}
