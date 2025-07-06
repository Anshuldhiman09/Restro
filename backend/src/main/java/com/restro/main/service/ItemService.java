package com.restro.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restro.main.dto.ItemDto;
import com.restro.main.dto.UpdateItemNameDto;
import com.restro.main.entity.ItemsEntity;
import com.restro.main.repo.ItemRepo;

import jakarta.transaction.Transactional;

@Service
public class ItemService {

	@Autowired
	private ItemRepo itemRepo;

	public String saveItems(ItemDto req) {
		Boolean exists = itemRepo.existsByItemNameIgnoreCase(req.getItemName());
		if (exists) {
			return "Item is already in Menu!";
		}

		ItemsEntity entity = ItemsEntity.builder().itemName(req.getItemName()).itemPrice(req.getItemPrice())
				.status(true).build();

		itemRepo.save(entity);
		return "Item added to menu!";
	}

	public String updateItemPrice(UpdateItemNameDto reqDto) {
		ItemsEntity entity = itemRepo.findByItemName(reqDto.getItemName())
				.orElseThrow(() -> new RuntimeException("Item not found "));
		entity.setItemPrice(reqDto.getNewPrice());
		itemRepo.save(entity);
		return "Item price updated!!";
	}

	@Transactional
	public String statusChange(Long id) {

		Boolean currentStatus = itemRepo.findStatusById(id);

		if (currentStatus == null) {
			return "Item not found!!";
		}
		itemRepo.updateStatusById(id, !currentStatus);

		return "Status updated !!";
	}
	
	public List<ItemsEntity> getAllItems() {
	    return itemRepo.findAll();
	}
}
