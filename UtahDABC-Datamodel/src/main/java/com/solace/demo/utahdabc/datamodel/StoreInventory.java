package com.solace.demo.utahdabc.datamodel;

public class StoreInventory {
	private String storeID;
	private String storeName;
	private int productQty;
	private String storeAddress;
	private double storeGeoLat;
	private double storeGeoLng;
	
	private String storeCity;
	private String storePhone;
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
	public double getStoreGeoLat() {
		return storeGeoLat;
	}
	public void setStoreGeoLat(double storeGeoLat) {
		this.storeGeoLat = storeGeoLat;
	}
	public double getStoreGeoLng() {
		return storeGeoLng;
	}
	public void setStoreGeoLng(double storeGeoLng) {
		this.storeGeoLng = storeGeoLng;
	}

}
