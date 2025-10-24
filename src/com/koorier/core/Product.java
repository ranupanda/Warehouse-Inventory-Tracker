package com.koorier.core;

//Core entity for products in inventory
public class Product {

	private String productId;
	private String name;
	private int quantity;
	private int reorderThreshold;

	public Product(String productId, String name, int quantity, int reorderThreshold) {
		super();
		this.productId = productId;
		this.name = name;
		this.quantity = quantity;
		this.reorderThreshold = reorderThreshold;
	}

	// Getter and setter methods
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getReorderThreshold() {
		return reorderThreshold;
	}

	public void setReorderThreshold(int reorderThreshold) {
		this.reorderThreshold = reorderThreshold;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", name=" + name + ", quantity=" + quantity + ", reorderThreshold="
				+ reorderThreshold + "]";
	}

}
