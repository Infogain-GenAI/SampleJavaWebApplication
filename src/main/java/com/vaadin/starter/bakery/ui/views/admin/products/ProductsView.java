package com.vaadin.starter.bakery.ui.views.admin.products;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.ui.MainView;

import jakarta.annotation.security.PermitAll;


@PermitAll
@Route(value = "Products", layout = MainView.class)
@PageTitle("Products | Vaadin CRM")
public class ProductsView extends VerticalLayout {

	Grid<Product> grid = new Grid<>(Product.class);
	TextField filterText = new TextField();
	ProductForm form;
	ProductService productService;
	
	public ProductsView(ProductService productService) {
		this.productService = productService;
		addClassName("product-view");
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
        form = new ProductForm(); 
        form.setWidth("25em");
        form.addSaveListener(this::saveProduct); 
        form.addDeleteListener(this::deleteProduct); 
        form.addCloseListener(e -> closeEditor());
    }

	private void updateList() { 
		//System.out.println("in View FilterText----"+filterText.getValue());
        grid.setItems(productService.findAllProduct(filterText.getValue()));
    }
	
	private void configureGrid() {
		grid.addClassNames("product-view");
		grid.setSizeFull();
		grid.setColumns("name", "price");
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		grid.asSingleSelect().addValueChangeListener(event ->
	        editProduct(event.getValue()));
	}

	private HorizontalLayout getToolbar() {
		filterText.setPlaceholder("Filter by name...");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e -> updateList()); 
		
		Button addProductButton = new Button("Add Product");
		addProductButton.addClickListener(click -> addProduct());
		
		var toolbar = new HorizontalLayout(filterText, addProductButton);
		toolbar.addClassName("toolbar");
		return toolbar;
	}
	
	private void addProduct() { 
        grid.asSingleSelect().clear();
        editProduct(new Product());
    }
	
	 private void saveProduct(ProductForm.SaveEvent event) {
	        productService.saveProduct(event.getProduct());
	        updateList();
	        closeEditor();
	    }

	   public void editProduct(Product product) { 
	        if (product == null) {
	            closeEditor();
	        } else {
	            form.setProduct(product);
	            form.setVisible(true);
	            addClassName("editing");
	        }
	    }
	   
	    private void deleteProduct(ProductForm.DeleteEvent event) {
	    	productService.deleteProduct(event.getProduct());
	        updateList();
	        closeEditor();
	    }
	    
	    private void closeEditor() {
	        form.setProduct(null);
	        form.setVisible(false);
	        removeClassName("editing");
	    }
}
