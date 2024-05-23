package com.vaadin.starter.bakery.backend.data.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Inventory extends AbstractEntity {

    @NotBlank(message = "{bakery.name.required}")
    @Size(max = 255)
    //@Column(unique = true)
    private String name;

    @Min(value = 0, message = "{bakery.inStock.limits}")
    @Max(value = 100000, message = "{bakery.inStock.limits}")
    private Integer inStock;

    @Min(value = 0, message = "{bakery.inProcess.limits}")
    @Max(value = 100000, message = "{bakery.inProcess.limits}")
    private Integer inProcess;

    // Real price * 100 as an int to avoid rounding errors
    @Min(value = 0, message = "{bakery.price.limits}")
    @Max(value = 100000, message = "{bakery.price.limits}")
    private Integer price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getInStock() {
        return inStock;
    }

    public void setInStock(Integer inStock) {
        this.inStock = inStock;
    }

    public Integer getInProcess() {
        return inProcess;
    }

    public void setInProcess(Integer inProcess) {
        this.inProcess = inProcess;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
    
 

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), name, inStock, inProcess, price);
	}
}