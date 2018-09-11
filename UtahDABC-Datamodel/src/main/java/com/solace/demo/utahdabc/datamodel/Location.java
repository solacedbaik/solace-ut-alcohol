package com.solace.demo.utahdabc.datamodel;

import java.io.Serializable;

public class Location implements Serializable {
	private static final long serialVersionUID = 7765691818807459002L;
	private double lat;
	private double lon;
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
}
