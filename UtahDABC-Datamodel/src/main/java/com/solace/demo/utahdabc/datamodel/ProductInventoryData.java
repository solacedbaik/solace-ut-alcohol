package com.solace.demo.utahdabc.datamodel;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("ProductInventoryData")
public class ProductInventoryData implements Serializable {
	private static final long serialVersionUID = -4917080369962626760L;
	
	private int warehouseInventoryQty;
	private int warehouseOnOrderQty;
	private String productStatus;
	private Product product;
	private StoreInventory storeInventory;
	private ZonedDateTime _timestamp;
	
	public ProductInventoryData() {
		_timestamp = ZonedDateTime.now();
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
	public ZonedDateTime get_timestamp() {
		return _timestamp;
	}
}
