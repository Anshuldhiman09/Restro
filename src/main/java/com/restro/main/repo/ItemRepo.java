package com.restro.main.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.restro.main.entity.ItemsEntity;

public interface ItemRepo extends JpaRepository<ItemsEntity, Long> {
	Boolean existsByItemNameIgnoreCase(String itemName);

	Optional<ItemsEntity> findByItemName(String itemName);

	@Modifying
	@Query("UPDATE ItemsEntity i SET i.status = :status WHERE i.id = :id")
	int updateStatusById(@Param("id") Long id, @Param("status") Boolean status);

	@Query("SELECT i.status FROM ItemsEntity i WHERE i.id = :id")
	Boolean findStatusById(@Param("id") Long id);
	

}
