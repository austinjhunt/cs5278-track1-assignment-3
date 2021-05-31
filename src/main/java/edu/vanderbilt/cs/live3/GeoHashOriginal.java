/* 
 * GeoHash - CS 5278 Assignment 1
 * May 13, 2021
 * Austin Hunt
 */

package edu.vanderbilt.cs.live3;
// To merge two lists with addAll()
import org.apache.commons.lang3.ArrayUtils;

public class GeoHashOriginal {

	/**
	 * This live session will focus on basic Java and some concepts important to
	 * functional programming, such as recursion.
	 *
	 * This class uses a main() method where we can write our own simple "experiments" to
	 * test how our code works. You are encouraged to modify the main method to play around
	 * with your code and understand it. When you have working code, you can extract it
	 * into a method. When you have working examples with assertions, you can extract them
	 * into tests.
	 *
	 * I have left some sample experiments in main() to help you understand the geohash
	 * algorithm.
	 *
	 *
	 * This class will provide an implementation of GeoHashes:
	 *
	 * https://www.mapzen.com/blog/geohashes-and-you/
	 * https://en.wikipedia.org/wiki/Geohash
	 *
	 *
	 * GeoHash Spatial Precision:
	 *
	 * https://releases.dataone.org/online/api-documentation-v2.0.1/design/geohash.html
	 */


	public static final double[] LATITUDE_RANGE = { -90, 90 };
	public static final double[] LONGITUDE_RANGE = { -180, 180 };
	
	public static double[] copyDoubleArray(double [] arrToCopy) {
		// Copy an array of doubles to a new array 
		double [] b = new double[arrToCopy.length];
		for (int i = 0 ; i < arrToCopy.length; i++){
			b[i] = arrToCopy[i];
		}
        return b; 
	}

	public static boolean[] geohash1D(double valueToHash, double[] valueRange, int bitsOfPrecision) {

		// @ToDo:
		//
		// Implement GeoHashing here for a single value (NOT! latitude, longitude pair).
		//
		// For those seeking an added challenge, use a recursive algorithm that uses a
		// base case and does not pass around a result array.
		//
		// For now, this method only needs to "geohash" either latitude or longitude
		// separately.
		//
		// You will be passed a valueToHash and a valueRange (e.g., the range of
		// longitudes or latitudes).
		//
		// The bits of precision is the number of bits that should be in your output
		// hash.
		//
		// We are approximating "bits" with a boolean array to make things simpler.
		
		if ( bitsOfPrecision == 0 ){
			// base case. 
			return null;
		}
		else {
			// keep recursing and decrementing bitsOfPrecision
			// Copy the valueRange array to prevent changes to valueRange array used for testing
			// (the constant was modified without copying; this fixes that)
			double[] valueRangeCopy = copyDoubleArray(valueRange);

			// get range midpoint
			double midpoint = ( valueRangeCopy[0] + valueRangeCopy[1] ) / 2;
			
			// determine which half the <long/lat> value belongs to based on midpoint comparison
			// if larger half, bit is 1, if lower half, bit is 0; initialize to false
			boolean bit = false;
			if ( valueToHash >= midpoint ){
				// if greater, set bit to 1 and set new lower limit to be midpoint
				bit = true;
				valueRangeCopy[0] = midpoint;
			}
			else {
				// if less, set bit to 0 and set new upper limit to be midpoint
				// bit already false
				valueRangeCopy[1] = midpoint;
			}
			// decrement bitsOfPrecision for next recursive call (closer to base case)
			bitsOfPrecision -= 1;
			
			// return one array combining this bit and the next recursive list of bits, i.e. 
			// [<bit from this call>, <list of bits from next recursive call>] 
			return ArrayUtils.addAll(new boolean[] {bit}, geohash1D(valueToHash, valueRangeCopy, bitsOfPrecision));
		}
	}

	public static boolean[] geohash2D(double v1, double[] v1range, double v2, double[] v2range, int bitsOfPrecision) {

		// @ToDo:
		//
		// Separately compute indvidual 1D geohashes for v1 and v2 and then interleave them together
		// into a final combined geohash.
		//
		// The resulting geohash should have the number of bits specified by bitsOfPrecision.
		
		// Each of the individual lat / long hashes should be half the size of combined
		// account for odd length (where length=bitsOfPrecision) with + (bitsOfPrecision % 2) 
		boolean[] latHash = geohash1D(v1, v1range, (bitsOfPrecision / 2) + (bitsOfPrecision % 2)); 
		boolean[] longHash = geohash1D(v2, v2range, (bitsOfPrecision / 2) + (bitsOfPrecision % 2));
		
		// Create the combined bit array
		boolean[] combined = new boolean[bitsOfPrecision];  
		int i = 0; 
		while (i < bitsOfPrecision) {
			// interleave -> { latHash[0], longHash[0], latHash[1], longHash[1], ... latHash[(bitsOfPrecision-1)/2], longHash[(bitsOfPrecision-1)/2]}
			// dividing by 2 in the indexing truncates the decimal, e.g. 0/2,1/2,3/2,4/2,5/2 -> 0,0,1,1,2,2,..
			if (i % 2 == 0) {
				combined[i] = latHash[i/2]; 
			}
			else {
				combined[i] = longHash[i/2];
			}
			i ++; 
		} 
		return combined; 
	}
	
	public String boolArrayToString(boolean[] arr) {
		String out = "";
		for (int i = 0; i < arr.length; i ++) {
			out += arr[i] ? "1" : "0";
		}
		return out;
	}
	public boolean[] bitStringToBoolArray(String bitString) {
		boolean[] out = new boolean[bitString.length()];
		for (int i = 0; i < bitString.length(); i ++) {
			out[i] = (bitString.charAt(i) == '1');
		}
		return out;
	}

	public static boolean[] geohash(double lat, double lon, int bitsOfPrecision) {
		return geohash2D(lat, LATITUDE_RANGE, lon, LONGITUDE_RANGE, bitsOfPrecision);
	}

	// This is a helper method that will make printing out
	// geohashes easier
	public static String toHashString(boolean[] geohash) {
		String hashString = "";
		for (boolean b : geohash) {
			hashString += (b ? "1" : "0");
		}
		return hashString;
	}

	// This is a convenience method to make it easy to get a string of 1s and 0s for a
	// geohash
	public static String geohashString(double valueToHash, double[] valueRange, int bitsOfPrecision) {
		return toHashString(geohash1D(valueToHash,valueRange,bitsOfPrecision));
	}

	// Faux testing for now
	public static void assertEquals(String v1, String v2) {
		if(!v1.contentEquals(v2)) {
			throw new RuntimeException(v1 + " != " + v2);
		}
	}

	public static void main(String[] args) {
		// Example of hand-coding a 3-bit geohash

		// 1st bit of the geohash
		double longitude = 0.0;
		double[] bounds = {LONGITUDE_RANGE[0], LONGITUDE_RANGE[1]};
		double midpoint = (bounds[0] + bounds[1]) / 2;
		boolean bit = false;

		if (longitude >= midpoint) {
			bit = true;
			bounds[0] = midpoint;
		}
		else {
			bit = false;
			bounds[1] = midpoint;
		}

		// 2nd bit of the geohash
		boolean bit2 = false;
		midpoint = (bounds[0] + bounds[1]) / 2;
		if (longitude >= midpoint) {
			bit2 = true;
			bounds[0] = midpoint;
		}
		else {
			bit2 = false;
			bounds[1] = midpoint;
		}

		// 3rd bit of the geohash
		boolean bit3 = false;
		midpoint = (bounds[0] + bounds[1]) / 2;
		if (longitude >= midpoint) {
			bit3 = true;
			bounds[0] = midpoint;
		}
		else {
			bit3 = false;
			bounds[1] = midpoint;
		}
		// Continue this process for however many bits of precision we need...


		// Faux testing for now
		assertEquals("100", toHashString(new boolean[] {bit, bit2, bit3}));

		// If you can get the 1D geohash to pass all of these faux tests, you should be in
		// good shape to complete the 2D version.
		assertEquals("00000", geohashString(LONGITUDE_RANGE[0], LONGITUDE_RANGE, 5));
		assertEquals("00000", geohashString(LATITUDE_RANGE[0], LATITUDE_RANGE, 5));
		assertEquals("11111", geohashString(LONGITUDE_RANGE[1], LONGITUDE_RANGE, 5));
		assertEquals("11111", geohashString(LATITUDE_RANGE[1], LATITUDE_RANGE, 5));
		assertEquals("10000", geohashString(0, LONGITUDE_RANGE, 5));
		assertEquals("11000", geohashString(90.0, LONGITUDE_RANGE, 5));
		assertEquals("11100", geohashString(135.0, LONGITUDE_RANGE, 5));
		assertEquals("11110", geohashString(157.5, LONGITUDE_RANGE, 5));
		assertEquals("11111", geohashString(168.75, LONGITUDE_RANGE, 5));
		assertEquals("01111", geohashString(-1, LONGITUDE_RANGE, 5));
		assertEquals("00111", geohashString(-91.0, LONGITUDE_RANGE, 5));
		assertEquals("00011", geohashString(-136.0, LONGITUDE_RANGE, 5));
		assertEquals("00001", geohashString(-158.5, LONGITUDE_RANGE, 5));
		assertEquals("00000", geohashString(-169.75, LONGITUDE_RANGE, 5));
	}
}
