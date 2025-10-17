package com.koorier.customException;

public class WarehouseInventoryException extends Exception {
	public WarehouseInventoryException(String errorMesg) {
		super(errorMesg);
	}

}
