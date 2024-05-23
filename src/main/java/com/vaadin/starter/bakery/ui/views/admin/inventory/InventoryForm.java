package com.vaadin.starter.bakery.ui.views.admin.inventory;



import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.Inventory;

public class InventoryForm extends FormLayout {
  TextField name = new TextField("Product Name");
  TextField inStock = new TextField("InStock");
  TextField inProcess = new TextField("InProcess");
  TextField quantity = new TextField("Quantity");
  TextField price = new TextField("Price");

  Button save = new Button("Save");
  Button delete = new Button("Delete");
  Button close = new Button("Cancel");
  // Other fields omitted
  Binder<Inventory> binder = new BeanValidationBinder<>(Inventory.class);

  public InventoryForm() {
    addClassName("inventory-form");
    binder.bindInstanceFields(this);
    
    add(name,
    	quantity,
    	inStock,
    	inProcess,
        price,
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


  public void setInventory(Inventory inventory) {
    binder.setBean(inventory); // <1>
  }

  // Events
  public static abstract class InventoryFormEvent extends ComponentEvent<InventoryForm> {
    private Inventory inventory;

    protected InventoryFormEvent(InventoryForm source, Inventory inventory) {
      super(source, false);
      this.inventory = inventory;
    }

    public Inventory getInventory() {
      return inventory;
    }
  }

  public static class SaveEvent extends InventoryFormEvent {
    SaveEvent(InventoryForm source, Inventory inventory) {
      super(source, inventory);
    }
  }

  public static class DeleteEvent extends InventoryFormEvent {
    DeleteEvent(InventoryForm source, Inventory inventory) {
      super(source, inventory);
    }

  }
  
  public static class CloseEvent extends InventoryFormEvent {
    CloseEvent(InventoryForm source) {
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
