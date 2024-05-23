package com.vaadin.starter.bakery.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.backend.data.entity.Inventory;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.InventoryRepository;

@Service
public class InventoryService  implements FilterableCrudService<Inventory>{

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> findAllInventory(String stringFilter) {
		  
		   if (stringFilter == null || stringFilter.isEmpty()) { 
			   return inventoryRepository.findAll();
	        } else {
	        	//System.out.println("IN Filter..........");
	            return inventoryRepository.search(stringFilter);
	        }
	  }
	  
    public void deleteInventory(Inventory inventory) {
    	inventoryRepository.delete(inventory);
    }

    public void saveInventory(Inventory inventory) {
        if (inventory == null) { 
            System.err.println("Product is null. Are you sure you have connected your form to the application?");
            return;
        }
        inventoryRepository.save(inventory);
    }
    
    @Override
    public Page<Inventory> findAnyMatching(Optional<String> filter, Pageable pageable) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return inventoryRepository.findByNameLikeIgnoreCase(repositoryFilter, pageable);
        } else {
            return find(pageable);
        }
    }

	private Page<Inventory> find(Pageable pageable) {
		// TODO Auto-generated method stub
		return inventoryRepository.findBy(pageable);
	}

	@Override
	public JpaRepository<Inventory, Long> getRepository() {
		// TODO Auto-generated method stub
		return inventoryRepository;
	}

	@Override
	public Inventory createNew(User currentUser) {
		// TODO Auto-generated method stub
		return new Inventory();
	}

	@Override
	public long countAnyMatching(Optional<String> filter) {
		// TODO Auto-generated method stub
		return 0;
	}
}