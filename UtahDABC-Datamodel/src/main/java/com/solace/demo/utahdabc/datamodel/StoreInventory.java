package com.solace.demo.utahdabc.datamodel;

import java.io.Serializable;

public class StoreInventory implements Serializable {
	private static final long serialVersionUID = -401096416595565465L;
	private String storeID;
	private String storeName;
	private int productQty;
	private String storeAddress;
	
	private Location location;
	
	private String storeCity;
	private String storePhone;
	
	public StoreInventory() {
		location = new Location();
	}
	
	public String getStoreID() {
		return storeID;
	}
	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public int getProductQty() {
		return productQty;
	}
	public void setProductQty(int productQty) {
		this.productQty = productQty;
	}
	public String getStoreAddress() {
		return storeAddress;
	}
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	public String getStoreCity() {
		return storeCity;
	}
	public void setStoreCity(String storeCity) {
		this.storeCity = storeCity;
	}
	public String getStorePhone() {
		return storePhone;
	}
	public void setStorePhone(String storePhone) {
		this.storePhone = storePhone;
	}
	public Location getLocation() {
		return location;
	}
}
