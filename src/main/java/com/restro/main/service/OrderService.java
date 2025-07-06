package com.restro.main.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restro.main.dto.BillDto;
import com.restro.main.dto.OrderDto;
import com.restro.main.dto.OrderItemDto;
import com.restro.main.entity.ItemsEntity;
import com.restro.main.repo.ItemRepo;

@Service
public class OrderService {

	@Autowired
	private ItemRepo itemRepo;

	@Value("${gemini.api.key}")
	private String apiKey;
	@Value("${gemini.api.url}")
	private String GEMINI_URL;

	@Autowired
	private RestTemplate restTemplate;

	public List<BillDto> getOrder(OrderDto req) {

		List<ItemsEntity> items = itemRepo.findAll();

		String prompt = "Extract item names and quantities from this sentence: \"" + req.getUserOrder()
				+ "\".And match in list: \"" + items + "And make sure match fuzzy spelling with relatable items.Respond ONLY with raw JSON array like this: "
				+ "[{\"itemName\": \"apple\", \"quantity\": 2}]. No markdown, no explanation. and make sure the spelling can be wrong...give right spelling and give right spelling.";

		// Build Gemini request JSON
		Map<String, Object> part = new HashMap<>();
		part.put("text", prompt);

		Map<String, Object> content = new HashMap<>();
		content.put("parts", List.of(part));

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("contents", List.of(content));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(List.of(MediaType.APPLICATION_JSON)); // ensure JSON expected

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

		String url = GEMINI_URL + apiKey;

		try {
			ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
			String body = response.getBody();

			ObjectMapper mapper = new ObjectMapper();

			// Parse top-level Gemini response
			Map<String, Object> full = mapper.readValue(body, new TypeReference<>() {
			});
			List<Map<String, Object>> candidates = (List<Map<String, Object>>) full.get("candidates");
			Map<String, Object> contentMap = (Map<String, Object>) candidates.get(0).get("content");
			List<Map<String, Object>> partsList = (List<Map<String, Object>>) contentMap.get("parts");

			String json = (String) partsList.get(0).get("text");

			json = json.trim();
			if (json.startsWith("```")) {
				json = json.replaceAll("(?i)```json", "").replaceAll("```", "").trim();
			}

			// Parse the actual JSON array string
			System.out.println(mapper.readValue(json, new TypeReference<List<OrderItemDto>>() {
			}));
			List<BillDto> res = findOrder(mapper.readValue(json, new TypeReference<List<OrderItemDto>>() {
			}));
			System.out.println(res);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public List<BillDto> findOrder(List<OrderItemDto> list) {

		List<BillDto> res = new ArrayList<>();

		for (OrderItemDto item : list) {
			Optional<ItemsEntity> itemOptional = itemRepo.findByItemName(item.getItemName());

			BillDto dto = new BillDto();

			if (itemOptional.isPresent()) {
				ItemsEntity entity = itemOptional.get();

				dto.setItemName(entity.getItemName());
				dto.setItemPrice(entity.getItemPrice());
				dto.setItemQuantity(item.getQuantity());

				dto.setTotalItemPrice(entity.getItemPrice() * item.getQuantity());

				res.add(dto);

			} else {
				continue;
			}
		}
		return res;

	}

}
