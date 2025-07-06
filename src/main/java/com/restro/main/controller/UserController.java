package com.restro.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restro.main.dto.ApiResponse;
import com.restro.main.dto.BillDto;
import com.restro.main.dto.OrderDto;
import com.restro.main.dto.OrderItemDto;
import com.restro.main.entity.ItemsEntity;
import com.restro.main.service.ItemService;
import com.restro.main.service.OrderService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5174")
public class UserController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ItemService itemService;

	@PostMapping("/give-order")
	public ResponseEntity<ApiResponse<String>> orderPrompt(@RequestBody OrderDto req) {

		orderService.getOrder(req);
		return null;

	}

	@PostMapping("/get-order")
	public ResponseEntity<ApiResponse<List<BillDto>>> getOrder(@RequestBody List<OrderItemDto> req) {
		List<BillDto> res = orderService.findOrder(req);

		ApiResponse<List<BillDto>> response = new ApiResponse<>();
		response.setStatusCode(HttpStatus.OK.value());
		response.setStatusmessage("Order Generated");
		response.setData(res);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-all-items")
	public ResponseEntity<ApiResponse<List<ItemsEntity>>> getAllItems() {
		ApiResponse<List<ItemsEntity>> response = new ApiResponse<>();
		response.setStatusCode(HttpStatus.OK.value());
		response.setStatusmessage("");
		response.setData(itemService.getAllItems());
		return ResponseEntity.ok(response);
	}

}
