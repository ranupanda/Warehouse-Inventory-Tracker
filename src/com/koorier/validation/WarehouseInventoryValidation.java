package com.koorier.validation;

import java.util.Map;

import com.koorier.core.Product;
import com.koorier.customException.WarehouseInventoryException;

public class WarehouseInventoryValidation {

	// To validate Received Units
	public static void validateReceivedUnits(int receivedUnits) throws WarehouseInventoryException {
		if (receivedUnits <= 0) {
			throw new WarehouseInventoryException("Received Units must be positive.");
		}

	}
	
	//to validate product existence
	
	public static void validateProductID(String productId, Map<String,Product> products) throws  WarehouseInventoryException{
		if (!products.containsKey(productId)) {
			throw new WarehouseInventoryException("Product " + productId + " not found");
		}

	}
}
