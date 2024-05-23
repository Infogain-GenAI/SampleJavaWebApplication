package com.vaadin.starter.bakery.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vaadin.starter.bakery.backend.data.entity.Inventory;

import jakarta.transaction.Transactional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

	
	@Query("select c from Inventory c " +
		      "where lower(c.name) like lower(concat('%', :searchTerm, '%')) ") 
		    List<Inventory> search(@Param("searchTerm") String searchTerm); 
	
	
    Page<Inventory> findBy(Pageable page);

    Page<Inventory> findByNameLikeIgnoreCase(String name, Pageable page);
    Inventory findByName(String name);

    int countByNameLikeIgnoreCase(String name);


    @Modifying
	@Transactional
	@Query("update Inventory i set i.inProcess=:quantity, i.inStock=:stockValue " +
		      "where lower(i.name) like lower(concat('%', :name, '%')) ") 
	void update(@Param("name") String name,@Param("quantity") Integer quantity,@Param("stockValue") int stockValue);

}