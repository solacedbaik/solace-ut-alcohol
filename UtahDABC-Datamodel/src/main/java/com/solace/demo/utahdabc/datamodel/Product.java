package com.solace.demo.utahdabc.datamodel;

import java.time.ZonedDateTime;

public class Product {
	private String name;
	private String div_code;
	private String dept_code;
	private String class_code;
	private int size;
	private int csc;
	private double price;
	private double lcboPrice;
	
	private String status;
	private String SPA;
	
	private String tags;
	
	private ZonedDateTime creationTimestamp;
	
	public Product() {
		creationTimestamp = ZonedDateTime.now();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDiv_code() {
		return div_code;
	}
	public void setDiv_code(String div_code) {
		this.div_code = div_code;
	}
	public String getDept_code() {
		return dept_code;
	}
	public void setDept_code(String dept_code) {
		this.dept_code = dept_code;
	}
	public String getClass_code() {
		return class_code;
	}
	public void setClass_code(String class_code) {
		this.class_code = class_code;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getCsc() {
		return csc;
	}
	public void setCsc(int csc) {
		this.csc = csc;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSPA() {
		return SPA;
	}
	public void setSPA(String sPA) {
		SPA = sPA;
	}
	public double getLcboPrice() {
		return lcboPrice;
	}
	public void setLcboPrice(double lcboPrice) {
		this.lcboPrice = lcboPrice;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public ZonedDateTime getCreationTimestamp() {
		return creationTimestamp;
	}
}
