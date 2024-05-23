package com.vaadin.starter.bakery.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.ProductRepository;

@Service
public class ProductService implements FilterableCrudService<Product> {

	private final ProductRepository productRepository;

	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	public List<Product> findAllProduct(String stringFilter) {
		  
		   if (stringFilter == null || stringFilter.isEmpty()) { 
			   return productRepository.findAll();
	        } else {
	        	//System.out.println("IN Filter.........."); 
	            return productRepository.search(stringFilter);
	        }
	  }
	  
	public Page<Product> find(Pageable pageable) {
		return productRepository.findBy(pageable);
	}

	@Override
	public JpaRepository<Product, Long> getRepository() {
		return productRepository;
	}

	@Override
	public Product createNew(User currentUser) {
		return new Product();
	}

	public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    public void saveProduct(Product product) {
        if (product == null) { 
            System.err.println("Product is null. Are you sure you have connected your form to the application?");
            return;
        }
        productRepository.save(product);
   
    }
    
    
    @Override
	public Page<Product> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return productRepository.findByNameLikeIgnoreCase(repositoryFilter, pageable);
		} else {
			return find(pageable);
		}
	}
	
    @Override
	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return productRepository.countByNameLikeIgnoreCase(repositoryFilter);
		} else {
			return count();
		}
	}

}
