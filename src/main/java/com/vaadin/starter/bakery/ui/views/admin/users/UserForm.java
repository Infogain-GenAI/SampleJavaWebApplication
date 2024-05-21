package com.vaadin.starter.bakery.ui.views.admin.users;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.User;

public class UserForm extends FormLayout {
  TextField firstName = new TextField("First name");
  TextField lastName = new TextField("Last name");
  EmailField email = new EmailField("Email");
  TextField password = new TextField("Password");
  TextField role = new TextField("Role");

  Button save = new Button("Save");
  Button delete = new Button("Delete");
  Button close = new Button("Cancel");
  // Other fields omitted
  Binder<User> binder = new BeanValidationBinder<>(User.class);

  public UserForm() {
    addClassName("user-form");
    binder.bindInstanceFields(this);
    
    add(firstName,
        lastName,
        email,
        password,
        role,
        createButtonsLayout());
  }

  private Component createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    save.addClickShortcut(Key.ENTER);
    close.addClickShortcut(Key.ESCAPE);

    save.addClickListener(event -> validateAndSave()); // <1>
    delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean()))); // <2>
    close.addClickListener(event -> fireEvent(new CloseEvent(this))); // <3>

    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid())); // <4>
    return new HorizontalLayout(save, delete, close);
  }

  private void validateAndSave() {
    if(binder.isValid()) {
      fireEvent(new SaveEvent(this, binder.getBean())); // <6>
    }
  }


  public void setUser(User user) {
    binder.setBean(user); // <1>
  }

  // Events
  public static abstract class UserFormEvent extends ComponentEvent<UserForm> {
    private User user;

    protected UserFormEvent(UserForm source, User user) {
      super(source, false);
      this.user = user;
    }

    public User getUser() {
      return user;
    }
  }

  public static class SaveEvent extends UserFormEvent {
    SaveEvent(UserForm source, User user) {
      super(source, user);
    }
  }

  public static class DeleteEvent extends UserFormEvent {
    DeleteEvent(UserForm source, User user) {
      super(source, user);
    }

  }
  
  public static class CloseEvent extends UserFormEvent {
    CloseEvent(UserForm source) {
      super(source, null);
    }
  }

  public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
	    return addListener(DeleteEvent.class, listener);
	  }

	  public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
	    return addListener(SaveEvent.class, listener);
	  }
	  public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
	    return addListener(CloseEvent.class, listener);
	  }


}
