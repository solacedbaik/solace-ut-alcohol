package com.solace.demo.utahdabc.datamodel;

import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class StoreTransaction {
	private static AtomicInteger _nextID = new AtomicInteger(0);
	private int transactionID;
	
	private ZonedDateTime creationTimestamp;

	private String storeID;
	private String storeAddress;
	private Location location;
	private PurchaseLineItem[] productsPurchased;	
	private float totalTransactionAmount;

	public StoreTransaction() {
		creationTimestamp = ZonedDateTime.now();
		setLocation(new Location());
		productsPurchased = new PurchaseLineItem[0];
		
		this.transactionID = _nextID.getAndIncrement();
	}
	
	public ZonedDateTime getCreationTimestamp() {
		return creationTimestamp;
	}

	public int getTransactionID() {
		return transactionID;
	}
	
	public String getStoreID() {
		return storeID;
	}

	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public PurchaseLineItem[] getProductsPurchased() {
		return productsPurchased;
	}

	public void setProductsPurchased(PurchaseLineItem[] productsPurchased) {
		this.productsPurchased = productsPurchased;
	}

	public float getTotalTransactionAmount() {
		return totalTransactionAmount;
	}

	public void setTotalTransactionAmount(float totalTransactionAmount) {
		this.totalTransactionAmount = totalTransactionAmount;
	}
	
	public static PurchaseLineItem createLineItem(String productName, int csc, int quantity, float unitPrice) {
		PurchaseLineItem li = new PurchaseLineItem();
		li.setProductName(productName);
		li.setCsc(csc);
		li.setQuantity(quantity);
		li.setUnitPrice(unitPrice);
		return li;
	}

	public static class PurchaseLineItem {
		private String productName;
		private int csc;
		private int quantity;
		private float unitPrice;
				
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		public float getTotalLineAmount() {
			return unitPrice * quantity;
		}

		public float getUnitPrice() {
			return unitPrice;
		}
		
		public void setUnitPrice(float unitPrice) {
			this.unitPrice = unitPrice;
		}
		public int getCsc() {
			return csc;
		}
		public void setCsc(int csc) {
			this.csc = csc;
		}
	}	
}