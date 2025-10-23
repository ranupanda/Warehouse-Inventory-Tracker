package com.koorier.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.koorier.core.Product;
import com.koorier.customException.WarehouseInventoryException;
import com.koorier.validation.WarehouseInventoryValidation;

public class WarehouseImplementation implements Warehouse {

	private String warehouseId;

	private Map<String, Product> products = new HashMap<>();

	private List<StockObserver> observers = new ArrayList<>();

	private ExecutorService executor = Executors.newFixedThreadPool(5);

	public WarehouseImplementation(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	// method to add products
	@Override
	public String addProduct(String productId, String name, int quantity, int reorderThreshold)
			throws WarehouseInventoryException {
		WarehouseInventoryValidation.validateProductIdFormat(productId);
		WarehouseInventoryValidation.validateProductName(name);
		WarehouseInventoryValidation.validateValue(quantity, "Quantity");
		WarehouseInventoryValidation.validateValue(reorderThreshold, "Reorder Threshold");

		if (products.containsKey(productId)) {
			throw new WarehouseInventoryException("Error: Product ID '" + productId + "' already exists.");
		} else {

			Product product = new Product(productId, name, quantity, reorderThreshold);
			products.put(product.getProductId(), product);
			return "product added successfully";
		}

	}

	// method to receiveShipments
	@Override
	public void receiveShipment(String productId, int receivedUnits) throws WarehouseInventoryException {
		executor.submit(() -> {
			synchronized (WarehouseImplementation.this) {
				try {
					WarehouseInventoryValidation.validateProductID(productId, products);
					WarehouseInventoryValidation.validateValue(receivedUnits, "Received unit");

					Product product = products.get(productId);
					int newQuantity = product.getQuantity() + receivedUnits;
					product.setQuantity(newQuantity);
					System.out
							.println("Shipment received: " + product.getName() + " quantity updated to " + newQuantity);
				} catch (WarehouseInventoryException e) {
					System.out.println(e.getMessage());
				}
			}
		});

	}

	// method to fulfillOrder
	@Override
	public void fulfillOrder(String productId, int quantity) throws WarehouseInventoryException {
		executor.submit(() -> {
			synchronized (WarehouseImplementation.this) {
				try {
					WarehouseInventoryValidation.validateProductID(productId, products);
					WarehouseInventoryValidation.validateValue(quantity, "Order Quantity");

					Product product = products.get(productId);
					if (product.getQuantity() < quantity) {
						throw new WarehouseInventoryException("Insufficient stock for " + product.getName()
								+ ". Available: " + product.getQuantity());
					}

					int updatedQuantity = product.getQuantity() - quantity;
					product.setQuantity(updatedQuantity);

					if (updatedQuantity < product.getReorderThreshold()) {
						notifyObservers(product);
					}

					System.out.println("Order fulfilled for: " + product.getName() + " quantity: " + quantity);
				} catch (WarehouseInventoryException e) {
					System.out.println(e.getMessage());
				}
			}
		});
	}

	public void addObserver(StockObserver observer) {
		observers.add(observer);
	}

	private void notifyObservers(Product product) {
		observers.forEach(observer -> observer.onLowStock(product));
	}

	// method to view all the product
	@Override
	public void viewAllProduct() {
		if (products.isEmpty()) {
			System.out.println("No products in inventory");
		}
		System.out.println("Current Inventory");
		products.forEach((p, pro) -> System.out.println(pro));
	}

	// method to add inventory state to text file

	public void saveToFile() throws WarehouseInventoryException {
		String fileName = "inventory_" + warehouseId + ".txt";
		try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
			products.values().stream().map(
					p -> p.getProductId() + "," + p.getName() + "," + p.getQuantity() + "," + p.getReorderThreshold())
					.forEach(line -> writer.println(line));
			System.out.println("Invetory state save to :" + fileName);
		} catch (IOException e) {
			throw new WarehouseInventoryException("Error saving inventory to file: " + e.getMessage());
		}
	}

	// method to load from the file
	public void loadFromFile() throws WarehouseInventoryException {
		String fileName = "inventory_" + warehouseId + ".txt";
		//WarehouseInventoryValidation.validateFileExists(fileName);
		File file = new File(fileName);
		if (!file.exists()) {
			//System.out.println("No existing inventory file found. Starting with an empty inventory.");
			return;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

			br.lines().map(line -> line.split(",")).filter(parts -> parts.length == 4).forEach(part -> {
				try {
					String productId = part[0].trim();
					String productName = part[1].trim();
					int quantity = Integer.parseInt(part[2].trim());
					int reorderThreshold = Integer.parseInt(part[3].trim());
					products.put(productId, new Product(productId, productName, quantity, reorderThreshold));
				} catch (NumberFormatException e) {
					System.out.println("Invalid number format in line: " + Arrays.toString(part));
				}
			});
			System.out.println("Inventory loaded from " + fileName);
		} catch (IOException e) {
			throw new WarehouseInventoryException("Error loading inventory from file");
		}
	}

	public void shutdown() {
		executor.shutdown();
		System.out.println("Warehouse operations shut down.");
	}

	public String getWarehouseId() {
		return warehouseId;
	}

}
