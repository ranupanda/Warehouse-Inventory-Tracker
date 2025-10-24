package com.koorier.service;

import com.koorier.core.Product;

public interface StockObserver {
	// Notify on low stock
	void onLowStock(Product product);
}
