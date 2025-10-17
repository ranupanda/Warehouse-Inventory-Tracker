package com.koorier.service;

import java.util.HashMap;
import java.util.Map;

import com.koorier.core.Product;
import com.koorier.customException.WarehouseInventoryException;
import com.koorier.validation.WarehouseInventoryValidation;

public class WarehouseImplementation implements Warehouse {

	private Map<String, Product> products = new HashMap<>();

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

	@Override
	public String receiveShipment(String productId, int receivedUnits) throws WarehouseInventoryException {

		if (!products.containsKey(productId)) {
			throw new WarehouseInventoryException("Product " + productId + " not found");
		}

		WarehouseInventoryValidation.validateAmount(receivedUnits);

		Product product = products.get(productId);
		int newQuantity = product.getQuantity() + receivedUnits;
		product.setQuantity(newQuantity);
		return "Shipment received: " + product.getName() + " quantity updated to " + newQuantity;

	}

}
