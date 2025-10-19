package com.koorier.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.koorier.core.Product;
import com.koorier.customException.WarehouseInventoryException;
import com.koorier.validation.WarehouseInventoryValidation;

public class WarehouseImplementation implements Warehouse {

	private Map<String, Product> products = new HashMap<>();

	private List<StockObserver> observers = new ArrayList<>();

	// method to add products
	@Override
	public String addProduct(String productId, String name, int quantity, int reorderThreshold)
			throws WarehouseInventoryException {
		if (products.containsKey(productId)) {
			throw new WarehouseInventoryException("Error: Product ID '" + productId + "' already exists.");
		} else {
			Product product = new Product(productId, name, quantity, reorderThreshold);
			products.put(product.getProductId(), product);
			return "product added successfully";
		}

	}

	// method to receiveShipments
	@Override
	public String receiveShipment(String productId, int receivedUnits) throws WarehouseInventoryException {

		WarehouseInventoryValidation.validateProductID(productId, products);
		WarehouseInventoryValidation.validateReceivedUnits(receivedUnits);

		Product product = products.get(productId);
		int newQuantity = product.getQuantity() + receivedUnits;
		product.setQuantity(newQuantity);
		return "Shipment received: " + product.getName() + " quantity updated to " + newQuantity;

	}

	// method to fulfillOrder
	@Override
	public String fulfillOrder(String productId, int quantity) throws WarehouseInventoryException {

		WarehouseInventoryValidation.validateProductID(productId, products);
		WarehouseInventoryValidation.validateReceivedUnits(quantity);

		Product product = products.get(productId);
		if (product.getQuantity() < quantity) {
			throw new WarehouseInventoryException(
					"Insufficient stock for " + product.getName() + ". Available: " + product.getQuantity());
		}

		int updatedQuantity = product.getQuantity() - quantity;
		product.setQuantity(updatedQuantity);

		if (updatedQuantity < product.getReorderThreshold()) {
			notifyObservers(product);
		}

		return "Order fulfilled for : " + product.getName() + " quantity : " + quantity;
	}

	public void addObserver(StockObserver observer) {
		observers.add(observer);
	}

	private void notifyObservers(Product product) {
		for (StockObserver observer : observers) {
			observer.onLowStock(product);
		}
	}

}
