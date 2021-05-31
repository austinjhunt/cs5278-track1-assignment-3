/* 
 * GeoHash - CS 5278 Track 1 Assignment 2
 * May 26, 2021
 * Austin Hunt
 */

package edu.vanderbilt.cs.live3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

import org.apache.commons.lang3.ArrayUtils; // To merge two lists with addAll()

public class GeoDBImpl extends GeoHashOriginal implements GeoDB {

	public static final double[] LATITUDE_RANGE = { -90, 90 };
	public static final double[] LONGITUDE_RANGE = { -180, 180 };
	
	private HashTree hashy = new HashTree();
	
	// store bits of precision as instance variable 
	int bitsOfPrecision; 
	GeoHashFactory factory;
	/* Begin GeoDB implementation */ 
	
	public GeoDBImpl(GeoHashFactory factory, int bitsOfPrecision) {
		/* Use the provided factory to create the GeoHash
		 * (GeoHashImpl) objects.
		 */
		this.factory = factory; 
		this.bitsOfPrecision = bitsOfPrecision;
	}
	
	/* Constructor with number of bits of precision as a constructor parameter */ 
	public GeoDBImpl (int bitsOfPrecision) {
		this.bitsOfPrecision = bitsOfPrecision;
	}
	
	public int size() {
		// returns total number of geohash entries in database
		return hashy.size();
	}
	
	/**
    *
    * Your GeoDB implementation should take the maximum
    * number of bits of precision as a constructor parameter.
    * When you call this insert method, it should use the
    * maximum bits of precision when calculating the geohash
    * for the inserted data.
    *
    * Inserts a location into the database. No
    * duplicates are stored. If the position is already
    * present, it should be overwritten.
    *
    * @param lat
    * @param lon
    */
   public void insert(double lat, double lon) { 
	   // String geoHash = toHashString(geohash(lat, lon, bitsOfPrecision));
	   // System.out.println("Inserting GeoHash = " + geoHash);
	   
	   GeoHash geoHash = this.factory.with(lat, lon, this.bitsOfPrecision);
	   System.out.println("Inserting Geohash: " + geoHash);
	   hashy.insertGeoHash(geoHash.iterator(), lat, lon);
   }

   /**
    * Deletes the specified location from the database.
    *
    * Your GeoDB implementation should take the maximum
    * number of bits of precision as a constructor parameter.
    * When you call this insert method, it should use the
    * maximum bits of precision when calculating the geohash
    * to search for to delete the associated location(s).
    *
    * Returns true if an item was deleted.
    *
    *
    * @param lat
    * @param lon
    */
   public boolean delete(double lat, double lon) {
	   
	   GeoHash geoHash = this.factory.with(lat, lon, this.bitsOfPrecision);
	   System.out.println("Removing GeoHash: " + geoHash);
	   return hashy.removeGeoHash(geoHash.iterator(), lat, lon);
   }

   /**
    * Deletes all locations from the database that
    * match the provided latitude and longitude
    * up to the specified number of bits of precision
    * in their geohashes.
    *
    * For example, if you are using 3 bits of precision,
    * then the following two geohashes match:
    *
    * 0100001 => 010
    * 0101111 => 010
    *
    * With 4 bits of precision, they don't match:
    *
    * 0100001 => 0100
    * 0101111 => 0101
    *
    * Returns the list of deleted locations.
    *
    * If bitsOfPrecision == 0, then this method should delete everything.
    *
    * @param lat
    * @param lon
    */
   public List<double[]> deleteAll(double lat, double lon, int bitsOfPrecision){
	   //String geoHash = toHashString(geohash(lat, lon, bitsOfPrecision));
	   //System.out.println("Removing all entries that match geohash " + geoHash + 
		// 	   " up to " + bitsOfPrecision + " bits");
	   
	   GeoHash geoHash = this.factory.with(lat, lon, bitsOfPrecision);
	   System.out.println("Removing all entries that match geohash " + geoHash + 
			   " up to " + bitsOfPrecision + " bits");
	   return hashy.deleteAll(geoHash.iterator(), bitsOfPrecision); 
   }

   /**
    * Returns true if the database contains at least one location that
    * matches the provided latitude and longitude
    * up to the specified number of bits of precision
    * in its geohash.
    *
    * For example, if you are using 3 bits of precision,
    * then the following two geohashes match:
    *
    * 0100001 => 010
    * 0101111 => 010
    *
    * With 4 bits of precision, they don't match:
    *
    * 0100001 => 0100
    * 0101111 => 0101
    *
    * If bitsOfPrecision == 0, then this method should always return true.
    *
    * @param lat
    * @param lon
    */
   public boolean contains(double lat, double lon, int bitsOfPrecision) {
	   // String geoHash = toHashString(geohash(lat, lon, bitsOfPrecision));
	   // System.out.println("Checking if tree contains (" + lat + "," + lon + ")->" + 
		//	   geoHash + " matching up to " + bitsOfPrecision + " characters");
	   
	   GeoHash geoHash = this.factory.with(lat, lon, bitsOfPrecision);
	   System.out.println("Checking if tree contains (" + lat + "," + lon + ")->" + 
			  geoHash + " matching up to " + bitsOfPrecision + " characters");
	   return hashy.contains(geoHash.iterator(), lat, lon, bitsOfPrecision);
   }

   /**
    * Returns all locations in the database that
    * match the provided latitude and longitude
    * up to the specified number of bits of precision
    * in their geohashes.
    *
    * For example, if you are using 3 bits of precision,
    * then the following two geohashes match:
    *
    * 0100001 => 010
    * 0101111 => 010
    *
    * With 4 bits of precision, they don't match:
    *
    * 0100001 => 0100
    * 0101111 => 0101
    *
    * If bitsOfPrecision == 0, then this method should always return everything
    * in the database.
    *
    * @param lat
    * @param lon
    */
   public List<double[]> nearby(double lat, double lon, int bitsOfPrecision){
	   // String geoHash = toHashString(geohash(lat, lon, bitsOfPrecision));
	   
	   GeoHash geoHash = this.factory.with(lat, lon, bitsOfPrecision);
	   List<double[]> neighbors = hashy.neighbors(geoHash.iterator(), bitsOfPrecision);
	   return neighbors;
   }
	/* End GeoDB implementation */ 
	  
	
	public HashTree getTree() {
		return this.hashy;
	}
	
	public static void main(String[] args) {

		
	}
}
