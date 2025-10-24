package com.koorier.service;

import com.koorier.customException.WarehouseInventoryException;

//Interface for warehouse operations
public interface Warehouse {

	// Add new product
	String addProduct(String productId, String name, int quantity, int reorderThreshold)
			throws WarehouseInventoryException;

	// Updates product quantity when a new shipment is received
	void receiveShipment(String productId, int receivedUnits) throws WarehouseInventoryException;

	//
	void fulfillOrder(String productId, int quantity) throws WarehouseInventoryException;

	// Display all products
	void viewAllProduct();

}
