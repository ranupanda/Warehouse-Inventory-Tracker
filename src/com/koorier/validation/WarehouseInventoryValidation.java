package com.koorier.validation;

import com.koorier.customException.WarehouseInventoryException;

public class WarehouseInventoryValidation {

	// To validate Received Units
	public static void validateAmount(int receivedUnits) throws WarehouseInventoryException {
		if (receivedUnits <= 0) {
			throw new WarehouseInventoryException("Received Units must be positive.");
		}

	}
}
