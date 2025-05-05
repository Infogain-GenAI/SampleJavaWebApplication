package com.vaadin.starter.bakery.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vaadin.starter.bakery.backend.data.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	


	User findByEmailIgnoreCase(String email);
	
	@Query("select c from UserInfo c " +
		      "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
		      "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))") 
	    List<User> search(@Param("searchTerm") String searchTerm); 
	
//	@Query("select c from UserInfo c " +
//		      "where lower(c.email) like lower(concat('%', :searchTerm, '%'))") 
//		    User findByEmail(@Param("searchTerm") String searchTerm); 

}
