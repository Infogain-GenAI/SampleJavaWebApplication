package com.vaadin.starter.bakery.ui.views.admin.products;



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
import com.vaadin.starter.bakery.backend.data.entity.Product;

public class ProductForm extends FormLayout {
  TextField name = new TextField("Product Name");
  TextField price = new TextField("Price");

  Button save = new Button("Save");
  Button delete = new Button("Delete");
  Button close = new Button("Cancel");
  // Other fields omitted
  Binder<Product> binder = new BeanValidationBinder<>(Product.class);

  public ProductForm() {
    addClassName("product-form");
    binder.bindInstanceFields(this);
    
    add(name,
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


  public void setProduct(Product product) {
    binder.setBean(product); // <1>
  }

  // Events
  public static abstract class ProductFormEvent extends ComponentEvent<ProductForm> {
    private Product product;

    protected ProductFormEvent(ProductForm source, Product product) {
      super(source, false);
      this.product = product;
    }

    public Product getProduct() {
      return product;
    }
  }

  public static class SaveEvent extends ProductFormEvent {
    SaveEvent(ProductForm source, Product product) {
      super(source, product);
    }
  }

  public static class DeleteEvent extends ProductFormEvent {
    DeleteEvent(ProductForm source, Product product) {
      super(source, product);
    }

  }
  
  public static class CloseEvent extends ProductFormEvent {
    CloseEvent(ProductForm source) {
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
