package com.vaadin.starter.bakery.ui.views.admin.users;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.MainView;

import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "Users", layout = MainView.class)
@PageTitle("Users | Vaadin CRM")
public class UsersView extends VerticalLayout {

	Grid<User> grid = new Grid<>(User.class);
	TextField filterText = new TextField();
	UserForm form;
	UserService userService;
	
	public UsersView(UserService userService) {
		this.userService = userService;
		addClassName("user-view");
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
        form = new UserForm(); 
        form.setWidth("25em");
        form.addSaveListener(this::saveUser); 
        form.addDeleteListener(this::deleteUser); 
        form.addCloseListener(e -> closeEditor());
    }

	private void updateList() { 
        grid.setItems(userService.findAllUsers(filterText.getValue()));
    }
	
	private void configureGrid() {
		grid.addClassNames("user-view");
		grid.setSizeFull();
		grid.setColumns("email", "firstName", "lastName", "role");
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		 grid.asSingleSelect().addValueChangeListener(event ->
	        editUser(event.getValue()));
	}

	private HorizontalLayout getToolbar() {
		filterText.setPlaceholder("Filter by name...");
		filterText.setClearButtonVisible(true);
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.addValueChangeListener(e -> updateList()); 

		Button addUserButton = new Button("Add User");
		addUserButton.addClickListener(click -> addUser());
		
		var toolbar = new HorizontalLayout(filterText, addUserButton);
		toolbar.addClassName("toolbar");
		return toolbar;
	}
	
	private void addUser() { 
        grid.asSingleSelect().clear();
        editUser(new User());
    }
	
	   private void saveUser(UserForm.SaveEvent event) {
	        userService.saveUser(event.getUser());
	        updateList();
	        closeEditor();
	    }

	   public void editUser(User user) { 
	        if (user == null) {
	            closeEditor();
	        } else {
	            form.setUser(user);
	            form.setVisible(true);
	            addClassName("editing");
	        }
	    }
	   
	    private void deleteUser(UserForm.DeleteEvent event) {
	    	userService.deleteUser(event.getUser());
	        updateList();
	        closeEditor();
	    }
	    
	    private void closeEditor() {
	        form.setUser(null);
	        form.setVisible(false);
	        removeClassName("editing");
	    }
}
