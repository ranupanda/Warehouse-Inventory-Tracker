package com.koorier.ui;

import java.util.Scanner;

import com.koorier.core.Product;
import com.koorier.customException.WarehouseInventoryException;
import com.koorier.service.StockObserver;
import com.koorier.service.WarehouseImplementation;

public class WarehousInventoryTracker {
	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in)) {

			WarehouseImplementation wit = new WarehouseImplementation();
			
			 StockObserver alertObserver = new StockObserver() {
	                @Override
	                public void onLowStock(Product product) {
	                    System.out.println("Restock Alert: Low stock for " + product.getName() + " - only " + product.getQuantity() + " left!");
	                }
	            };
	            
	            wit.addObserver(alertObserver);
			boolean exit = false;
			while (!exit) {
				System.out.println("Option\n" + "1.Add an new product\n" + "2.Recieve Shipment \n" +"3.Fulfill order \n"+ "0.Exit");
				System.out.println("Enter option :");
				try {
					switch (sc.nextInt()) {
					case 1: {
						System.out.println("Enter product details:productId,name,quantity,reorderThreshold");

						System.out.println(wit.addProduct(sc.next(), sc.next(), sc.nextInt(), sc.nextInt()));
						break;
					}
					case 2: {
						System.out.println("Enter productId and receivedUnits");

						System.out.println(wit.receiveShipment(sc.next(), sc.nextInt()));
						break;
					}
					case 3:{
						System.out.println("Enter productId and quantity");
						
						System.out.println(wit.fulfillOrder(sc.next(), sc.nextInt()));
						break;
					}
					
					case 0:
						exit = true;
						break;
					}
				} catch (WarehouseInventoryException e) {
					System.out.println(e.getMessage());
				}
				catch(Exception e) {
					System.out.println(e.getMessage());
					sc.nextLine();
				}
			}
		}
	}
}
