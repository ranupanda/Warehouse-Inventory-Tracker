package com.koorier.service;

import com.koorier.customException.WarehouseInventoryException;

public interface Warehouse {

	String addProduct(String productId, String name, int quantity, int reorderThreshold)
			throws WarehouseInventoryException;

	void receiveShipment(String productId, int receivedUnits) throws WarehouseInventoryException;
	
	void fulfillOrder(String productId , int quantity) throws WarehouseInventoryException;
	
	void viewAllProduct();

}
