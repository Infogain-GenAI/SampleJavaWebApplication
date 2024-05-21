package com.vaadin.starter.bakery.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vaadin.starter.bakery.backend.data.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("select c from Product c " +
		      "where lower(c.name) like lower(concat('%', :searchTerm, '%')) ") 
		    List<Product> search(@Param("searchTerm") String searchTerm); 
	
	Page<Product> findBy(Pageable page);

	Page<Product> findByNameLikeIgnoreCase(String name, Pageable page);

	int countByNameLikeIgnoreCase(String name);

}
