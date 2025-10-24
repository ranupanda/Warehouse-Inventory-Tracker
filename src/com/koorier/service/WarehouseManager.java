package com.koorier.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.koorier.customException.WarehouseInventoryException;

// Manages multiple warehouses
public class WarehouseManager {
	
	private static final String WAREHOUSES_FILE = "warehouses.txt";
	
	private Map<String, WarehouseImplementation> warehouses = new HashMap<>();

	// method to get and load warehouse
	public WarehouseImplementation getWarehouse(String warehouseId) throws WarehouseInventoryException {
		if (!warehouses.containsKey(warehouseId)) {
			throw new WarehouseInventoryException("Warehouse '" + warehouseId + "' not found.");
		}
		WarehouseImplementation warehouse = warehouses.get(warehouseId);
		try {
			warehouse.loadFromFile();
		} catch (WarehouseInventoryException e) {
			System.out.println("Warning: " + e.getMessage());
		}
		return warehouse;
	}

	// method to create a new warehouse
	public WarehouseImplementation createWarehouse(String warehouseId) throws WarehouseInventoryException {
		if (warehouses.containsKey(warehouseId)) {
			throw new WarehouseInventoryException("Warehouse '" + warehouseId + "' already exists.");
		}
		WarehouseImplementation newWarehouse = new WarehouseImplementation(warehouseId);
		warehouses.put(warehouseId, newWarehouse);

		return newWarehouse;
	}
	
	// method to save warehouse list
	public void saveWarehouseList() {
		try (PrintWriter writer = new PrintWriter(new FileWriter(WAREHOUSES_FILE))) {
			warehouses.keySet().forEach(writer::println);
		} catch (IOException e) {
			System.out.println("Error saving warehouses: " + e.getMessage());
		}
	}

	// method to load warehouse list
	public void loadWarehouseList() {
		File file = new File(WAREHOUSES_FILE);
		if (!file.exists()) {
			return;
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String id;
			while ((id = reader.readLine()) != null) {
				try {
					WarehouseImplementation wh = new WarehouseImplementation(id);
					wh.loadFromFile();
					warehouses.put(id, wh);
				} catch (Exception e) {
					System.out.println("Warning: Could not load warehouse " + id);
				}
			}
		} catch (IOException e) {
			System.out.println("Error loading warehouses: " + e.getMessage());
		}
	}

	public void saveAllWarehouses() {
		for (WarehouseImplementation wh : warehouses.values()) {
			try {
				wh.saveToFile();
			} catch (Exception e) {
				System.out.println("Error saving " + wh.getWarehouseId());
			}
		}
		saveWarehouseList();
	}

	public void shutdownAllWarehouses() {
		for (WarehouseImplementation warehouse : warehouses.values()) {
			warehouse.shutdown();
		}
	}

	public List<String> getAllWarehouseIds() {
		return new ArrayList<>(warehouses.keySet());
	}

}