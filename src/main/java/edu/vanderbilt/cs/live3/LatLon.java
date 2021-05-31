package edu.vanderbilt.cs.live3;

import java.util.Objects;

public class LatLon {
	private double lat; 
	private double lon;
	public LatLon(double lat, double lon) {
		this.lat = lat; 
		this.lon = lon;
	}
	public double getLat() {
		return this.lat;
	}
	public double getLon() {
		return this.lon;
	} 
	@Override
	public boolean equals(Object o) { 
		LatLon ll = (LatLon) o; 
		return (lat == ll.getLat() && lon == ll.getLon()) || (this == o);
	}
	
	public String toString() {
		return "(" + getLat() + "," + getLon() + ")";
	}
	 
	

}
