package com.koorier.ui;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.koorier.core.Product;
import com.koorier.service.StockObserver;
import com.koorier.service.WarehouseImplementation;
import com.koorier.service.WarehouseManager;
import com.koorier.customException.WarehouseInventoryException;

public class WarehouseInventoryTracker {

	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in)) {
			WarehouseManager manager = new WarehouseManager();
			manager.loadWarehouseList();
			WarehouseImplementation currentWarehouse = null;

			StockObserver alertObserver = new StockObserver() {
				@Override
				public void onLowStock(Product product) {
					System.out.println("Restock Alert: Low stock for " + product.getName() + " - only "
							+ product.getQuantity() + " left!");
				}
			};
			boolean exit = false;
			while (!exit) {
				if (currentWarehouse == null) {
					System.out.println("Warehouse Management System\n" + "Option\n" + "1.View all warehouse\n"
							+ "2. Select warehouse\n" + "3 Create Warehouse\n" + "0. Exit");
					System.out.println("Enter option :");

					try {
						int choice = sc.nextInt();
						sc.nextLine(); 
						switch (choice) {
						case 1: {
							System.out.println("Existing Warehouses:");
							manager.getAllWarehouseIds().forEach(id -> System.out.println("Warehouse ID: " + id));
							if (manager.getAllWarehouseIds().isEmpty()) {
								System.out.println("No warehouses found.");
							}
							break;
						}
						case 2: {
							System.out.print("Enter Warehouse ID to select: ");
							String warehouseId = sc.nextLine().trim();
							try {
								currentWarehouse = manager.getWarehouse(warehouseId);
								System.out.println("Warehouse '" + warehouseId + "' selected.");
								currentWarehouse.addObserver(alertObserver);
							} catch (WarehouseInventoryException e) {
								System.out.println(e.getMessage());
							}
							break;
						}
						case 3: {
							System.out.print("Enter Warehouse ID to create: ");
							String warehouseId = sc.nextLine().trim();
							try {
								currentWarehouse = manager.createWarehouse(warehouseId);
								System.out.println("Warehouse '" + warehouseId + "' created and selected.");
								currentWarehouse.addObserver(alertObserver);
							} catch (WarehouseInventoryException e) {
								System.out.println(e.getMessage());
							}
							break;
						}
						case 0: {
							manager.saveAllWarehouses();
							manager.shutdownAllWarehouses();
							exit = true;
							System.out.println("Exiting...");
							break;
						}
						default: {
							System.out.println("Invalid option! Please enter a valid choice.");
							break;
						}
						}
					} catch (InputMismatchException e) {
						System.out.println("Invalid input type! Please enter a number.");
						sc.nextLine();
					}
				} else {
					System.out.println("\nWarehouse: " + currentWarehouse.getWarehouseId());
					System.out.println("Option\n" + "1.Add an new product\n" + "2.Recieve Shipment \n"
							+ "3.Fulfill order \n" + "4.View all product\n" + "5. Switch Warehouse\n" + "0.Exit");
					try {
						int choice = sc.nextInt();
						sc.nextLine(); 
						switch (choice) {
						case 1: {
							System.out.println("Enter product details:");

							System.out.print("Product ID: ");
							String productId = sc.next();

							sc.nextLine();

							System.out.print("Product Name: ");
							String name = sc.nextLine();

							System.out.print("Quantity:");
							int quantity = sc.nextInt();

							System.out.print("Reorder Threshold: ");
							int reorderThreshold = sc.nextInt();

							System.out
									.println(currentWarehouse.addProduct(productId, name, quantity, reorderThreshold));
							break;
						}
						case 2: {
							System.out.println("Enter productId and receivedUnits");

							currentWarehouse.receiveShipment(sc.next(), sc.nextInt());
							break;
						}
						case 3: {
							System.out.println("Enter productId and quantity");

							currentWarehouse.fulfillOrder(sc.next(), sc.nextInt());
							break;
						}
						case 4: {
							currentWarehouse.viewAllProduct();
							break;
						}

						case 5: {
							currentWarehouse = null;
							break;
						}
						case 0: {
							manager.saveAllWarehouses();
							manager.shutdownAllWarehouses();
							exit = true;
							System.out.println("Exiting...");
							break;
						}
						default: {
							System.out.println("Invalid option! Please enter a valid choice from the menu.");
							break;
						}
						}
					} catch (InputMismatchException e) {
						System.out.println("Invalid input type! Please enter valid input.");
						sc.nextLine();
					} catch (WarehouseInventoryException e) {
						System.out.println(e.getMessage());
					} catch (Exception e) {
						System.out.println(e.getMessage());
						sc.nextLine();
					}
				}
			}
		}

	}
}
