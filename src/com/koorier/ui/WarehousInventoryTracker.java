package com.koorier.ui;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.koorier.core.Product;
import com.koorier.customException.WarehouseInventoryException;
import com.koorier.service.StockObserver;
import com.koorier.service.WarehouseImplementation;

public class WarehousInventoryTracker {
	public static final String INVENTORY_FILE = "inventory.txt";

	public static void main(String[] args) {

		try (Scanner sc = new Scanner(System.in)) {
			
			WarehouseImplementation wit = new WarehouseImplementation();

			// Load inventory from file
			try {
				wit.loadFromFile(INVENTORY_FILE);
			} catch (WarehouseInventoryException e) {
				System.out.println("Warning:" + e.getMessage());
			}

			StockObserver alertObserver = new StockObserver() {
				@Override
				public void onLowStock(Product product) {
					System.out.println("Restock Alert: Low stock for " + product.getName() + " - only "
							+ product.getQuantity() + " left!");
				}
			};

			wit.addObserver(alertObserver);

			boolean exit = false;
			while (!exit) {
				System.out.println("Option\n" 
						+ "1.Add an new product\n"
						+ "2.Recieve Shipment \n"
						+ "3.Fulfill order \n" 
						+ "4.View all product\n"
						+ "0.Exit");
				System.out.println("Enter option :");
				try {
					switch (sc.nextInt()) {
					case 1: {
						System.out.println("Enter product details:productId,name,quantity,reorderThreshold");
						
						System.out.print("Enter Product ID: ");
						String productId = sc.next();
						
						sc.nextLine();
						
						System.out.print("Enter Product Name: ");
						String name = sc.nextLine();  // works fine with spaces

						System.out.print("Enter Quantity: ");
						int quantity = Integer.parseInt(sc.nextLine());

						System.out.print("Enter Reorder Threshold: ");
						int reorderThreshold = Integer.parseInt(sc.nextLine());

						System.out.println(wit.addProduct(productId, name, quantity, reorderThreshold));
						break;
					}
					case 2: {
						System.out.println("Enter productId and receivedUnits");

						wit.receiveShipment(sc.next(), sc.nextInt());
						break;
					}
					case 3: {
						System.out.println("Enter productId and quantity");

						wit.fulfillOrder(sc.next(), sc.nextInt());
						break;
					}
					case 4: {
						wit.viewAllProduct();
						break;
					}

					case 0: {

						// Save inventory to file
						try {
							wit.saveToFile(INVENTORY_FILE);
						} catch (WarehouseInventoryException e) {
							System.out.println("Error saving inventory: " + e.getMessage());
						}
						exit = true;
						System.out.println("Exiting....");
						break;
					}
					default: {
				        System.out.println("Invalid option! Please enter a valid choice from the menu.");
				        break;
				    }
					}
				}catch (InputMismatchException e) {
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
