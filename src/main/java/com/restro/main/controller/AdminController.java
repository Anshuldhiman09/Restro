package com.restro.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restro.main.dto.ApiResponse;
import com.restro.main.dto.ItemDto;
import com.restro.main.dto.UpdateItemNameDto;
import com.restro.main.service.ItemService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private ItemService itemService;

	@PostMapping("/add-item")
	public ResponseEntity<ApiResponse<String>> addItem(@RequestBody ItemDto req) {
		String result = itemService.saveItems(req);

		ApiResponse<String> response = new ApiResponse<>();
		response.setStatusCode(HttpStatus.OK.value());
		response.setStatusmessage("SUCCESS");
		response.setData(result);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/update-price")
	public ResponseEntity<ApiResponse<String>> updateItemPrice(@RequestBody UpdateItemNameDto reqDto) {
		try {
			String result = itemService.updateItemPrice(reqDto);
			ApiResponse<String> response = new ApiResponse<>();
			response.setStatusCode(HttpStatus.OK.value());
			response.setStatusmessage("Success");
			response.setData(result);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			ApiResponse<String> response = new ApiResponse<>();
			response.setStatusCode(HttpStatus.NOT_FOUND.value());
			response.setStatusmessage("Item Not Found");
			response.setData(e.getMessage());

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

	}

	@PatchMapping("/update-status/{id}")
	public ResponseEntity<ApiResponse<String>> updateStatusByid(@PathVariable Long id) {

		try {
			String result = itemService.statusChange(id);
			ApiResponse<String> response = new ApiResponse<>();
			response.setStatusCode(HttpStatus.OK.value());
			response.setStatusmessage("Success");
			response.setData(result);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			ApiResponse<String> response = new ApiResponse<>();
			response.setStatusCode(HttpStatus.NOT_FOUND.value());
			response.setStatusmessage("Item Not Found");
			response.setData(e.getMessage());

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

	}

}
