package com.koorier.customException;

//Custom exception for inventory errors
public class WarehouseInventoryException extends Exception {
	public WarehouseInventoryException(String errorMesg) {
		super(errorMesg);
	}

}
