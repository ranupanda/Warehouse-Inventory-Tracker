package com.koorier.service;

import com.koorier.core.Product;

public interface StockObserver {
	
  void onLowStock(Product product);
}
