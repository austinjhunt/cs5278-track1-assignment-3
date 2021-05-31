/* 
 * HashTree
 * CS 5278 Track 1 Assignment 2
 * May 19, 2021
 * Austin Hunt
 */

package edu.vanderbilt.cs.live3;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class HashTree { 
	
	private HashNode root;
	
	public HashTree() {
		root = new HashNode(0); // level 0 for root
	}
	 
	public void insertGeoHash(String geoHash, double lat, double lon) {
		LatLon coords = new LatLon(lat, lon); 
		root.insertGeoHash(geoHash, coords);
	}
	/*
	 * Separate insert method that accepts a boolean iterable as the geohash 
	 * to insert instead of a string of characters. 
	 */
	public void insertGeoHash(Iterator<Boolean> iter, double lat, double lon) {
		LatLon coords = new LatLon(lat, lon);
		String geoHash = "";
		for (int i = 0; iter.hasNext(); i ++) {
			boolean b = iter.next(); 
			geoHash += b ? "1": "0";
		}
		root.insertGeoHash(geoHash, coords);
	}
	
	public boolean removeGeoHash(String geoHash, double lat, double lon) {
		LatLon coords = new LatLon(lat, lon); 
		return root.removeGeoHash(geoHash, coords);
	}
	public boolean removeGeoHash(Iterator<Boolean> iter, double lat, double lon) {
		LatLon coords = new LatLon(lat, lon); 
		String geoHash = "";
		for (int i = 0; iter.hasNext(); i ++) {
			boolean b = iter.next(); 
			geoHash += b ? "1": "0";
		}
		return root.removeGeoHash(geoHash, coords);
	}
	
	public List<double[]> deleteAll(String geoHash, int bitsOfPrecision){
		return root.removeAllMatchingUpToNChars(
				geoHash, bitsOfPrecision);
		 
	}
	public List<double[]> deleteAll(Iterator<Boolean> iter, int bitsOfPrecision){
		String geoHash = "";
		for (int i = 0; iter.hasNext(); i ++) {
			boolean b = iter.next(); 
			geoHash += b ? "1": "0";
		}
		return root.removeAllMatchingUpToNChars(
				geoHash, bitsOfPrecision);
		 
	}
	
	public boolean contains(String geoHash, double lat, double lon, int bitsOfPrecision) {
		LatLon coords = new LatLon(lat, lon);
		return root.containsUpToNChars(geoHash, coords, bitsOfPrecision);
	}
	
	public boolean contains(Iterator<Boolean> iter, double lat, double lon, int bitsOfPrecision) {
		if (this.size() == 0) {
			return false;
		} //FIXME: update the tree to have a .empty flag set to true when empty, that's the only time size matters
		LatLon coords = new LatLon(lat, lon);
		String geoHash = "";
		for (int i = 0; iter.hasNext(); i ++) {
			boolean b = iter.next(); 
			geoHash += b ? "1": "0";
		}
		return root.containsUpToNChars(geoHash, coords, bitsOfPrecision);
	}
	
	public List<double[]> neighbors(String geoHash, int bitsOfPrecision) {
		return root.neighborsUpToNChars(geoHash, bitsOfPrecision);
	}
	
	public List<double[]> neighbors(Iterator<Boolean> iter, int bitsOfPrecision) {
		String geoHash = "";
		for (int i = 0; iter.hasNext(); i ++) {
			boolean b = iter.next(); 
			geoHash += b ? "1": "0";
		}
		return root.neighborsUpToNChars(geoHash, bitsOfPrecision);
	}
	
	
	public int size() {
		// returns total number of geohashes currently stored in tree 
		return root.count();
	}
	
	public static void main(String[] args) { 
	}
	
	
	public String toString() {
		return root.toString();
	}
}
 
	
