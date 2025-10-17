package com.koorier.service;

import com.koorier.customException.WarehouseInventoryException;

public interface Warehouse {

	String addProduct(String productId, String name, int quantity, int reorderThreshold)
			throws WarehouseInventoryException;

	String receiveShipment(String productId, int amount) throws WarehouseInventoryException;

}
