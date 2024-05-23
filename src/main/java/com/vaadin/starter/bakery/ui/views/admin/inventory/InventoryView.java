package com.vaadin.starter.bakery.ui.views.admin.inventory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.bakery.backend.data.entity.Inventory;
import com.vaadin.starter.bakery.backend.service.InventoryService;
import com.vaadin.starter.bakery.ui.MainView;

import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "Inventory", layout = MainView.class)
@PageTitle("Inventory | Vaadin CRM")
public class InventoryView extends VerticalLayout {

	Grid<Inventory> grid = new Grid<>(Inventory.class);
	TextField filterText = new TextField();
	InventoryForm form;
	InventoryService inventoryService;
	
	public InventoryView(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
		addClassName("inventory-view");
		setSizeFull();
		configureGrid();
		configureForm();
		
		add(getToolbar(), getContent());
		updateList();
		closeEditor();
	}
	  
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new InventoryForm(); 
        form.setWidth("25em");
        form.addSaveListener(this::saveInventory); 
        form.addDeleteListener(this::deleteInventory); 
        form.addCloseListener(e -> closeEditor());
    }

	private void updateList() { 
        grid.setItems(inventoryService.findAllInventory(filterText.getValue()));
    }
	
	private void configureGrid() {
		grid.addClassNames("inventory-view");
		grid.setSizeFull();
		grid.setColumns("name", "inStock", "inProcess", "price");
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
//		grid.asSingleSelect().addValueChangeListener(event ->
//	        editInventory(event.getValue()));
	}

	private HorizontalLayout getToolbar() {
		filterText.setPlaceholder("Filter by name...");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e -> updateList()); 

		Button addInventoryButton = new Button("Add Inventory");
		addInventoryButton.addClickListener(click -> addInventory());
		
	//	var toolbar = new HorizontalLayout(filterText, addInventoryButton);
		var toolbar = new HorizontalLayout(filterText);
		toolbar.addClassName("toolbar");
		return toolbar;
	}
	
	private void addInventory() { 
        grid.asSingleSelect().clear();
        editInventory(new Inventory());
    }
	
	   private void saveInventory(InventoryForm.SaveEvent event) {
	        inventoryService.saveInventory(event.getInventory());
	        updateList();
	        closeEditor();
	    }

	   public void editInventory(Inventory inventory) { 
	        if (inventory == null) {
	            closeEditor();
	        } else {
	            form.setInventory(inventory);
	            form.setVisible(true);
	            addClassName("editing");
	        }
	    }
	   
	    private void deleteInventory(InventoryForm.DeleteEvent event) {
	    	inventoryService.deleteInventory(event.getInventory());
	        updateList();
	        closeEditor();
	    }
	    
	    private void closeEditor() {
	        form.setInventory(null);
	        form.setVisible(false);
	        removeClassName("editing");
	    }
}
