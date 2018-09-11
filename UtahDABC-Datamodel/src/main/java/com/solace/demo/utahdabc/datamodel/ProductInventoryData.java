package com.solace.demo.utahdabc.datamodel;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class ProductInventoryData implements Serializable {
	private static final long serialVersionUID = 8413198557641545313L;
	
	private int warehouseInventoryQty;
	private int warehouseOnOrderQty;
	private String productStatus;
	private Product product;
	private StoreInventory storeInventory;
	private ZonedDateTime creationTimestamp;
	
	public ProductInventoryData() {
		creationTimestamp = ZonedDateTime.now();
	}
	
	public int getWarehouseInventoryQty() {
		return warehouseInventoryQty;
	}
	public void setWarehouseInventoryQty(int warehouseInventoryQty) {
		this.warehouseInventoryQty = warehouseInventoryQty;
	}
	public int getWarehouseOnOrderQty() {
		return warehouseOnOrderQty;
	}
	public void setWarehouseOnOrderQty(int warehouseOnOrderQty) {
		this.warehouseOnOrderQty = warehouseOnOrderQty;
	}
	public String getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}
	public StoreInventory getStoreInventory() {
		return storeInventory;
	}
	public void setStoreInventory(StoreInventory storeInventory) {
		this.storeInventory = storeInventory;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public ZonedDateTime getCreationTimestamp() {
		return creationTimestamp;
	}
}
