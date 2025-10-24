package com.koorier.validation;

import java.io.File;
import java.util.Map;

import com.koorier.core.Product;
import com.koorier.customException.WarehouseInventoryException;

public class WarehouseInventoryValidation {

	// validate ProductId format
	public static void validateProductIdFormat(String productId) throws WarehouseInventoryException {
		if (!productId.matches("^\\d+[A-Za-z]*$")) {
			throw new WarehouseInventoryException("Enter valid ProductId");
		}
	}
	//validate Product name
	public static void validateProductName(String name) throws WarehouseInventoryException {
		if (!name.matches("^[A-Za-z\\s]+(?:\\s?[A-Za-z0-9]+)*$")) {
			throw new WarehouseInventoryException("Enter valid product name");
		}
	}

	// validate for non negative input
	public static void validateValue(int value, String fieldName) throws WarehouseInventoryException {
		if (value <= 0) {
			throw new WarehouseInventoryException(fieldName + " must be positive.");
		}

	}

	// validate product existence
	public static void validateProductID(String productId, Map<String, Product> products)
			throws WarehouseInventoryException {
		if (!products.containsKey(productId)) {
			throw new WarehouseInventoryException("Product " + productId + " not found");
		}

	}

	// validate file exist or not
	public static void validateFileExists(String fileName) throws WarehouseInventoryException {
		File file = new File(fileName);
		if (!file.exists()) {
			return;
		}
	}

}
