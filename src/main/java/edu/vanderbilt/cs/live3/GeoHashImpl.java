package edu.vanderbilt.cs.live3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * The original version of GeoHash failed to properly encapsulate the representation
 * of the GeoHash from consumers of the hashes. This class shields users from the
 * underlying representation by properly encapsulating the underlying representation.
 * Please use your original GeoHash work to complete this class.
 *
 * After you complete this class, go and complete the GeoDBFactory, GeoHashFactory,
 * and DefaultGeoDBConfiguration.
 *
 * You will need to update your implementation of GeoDB to use a GeoHashFactory to
 * produce geohash objects when the insert, delete, nearby, contains, etc. methods
 * are called. You cannot directly call your old GeoHash class or assume that your
 * implementation of the GeoHash will be provided to your GeoDB.
 *
 * There are some items marked with @Bonus that are not required. However, if you
 * want an additional challenge, knock these items out AFTER completing everything
 * else.
 *
 */
public class GeoHashImpl extends GeoHashOriginal implements GeoHash {
	 
		private String geohashString;
		private boolean[] geohashArray; 
		private int bitsOfPrecision;
		
		public GeoHashImpl(String geohash) {
			this.geohashString = geohash;
			this.geohashArray = this.bitStringToBoolArray(geohash);
			this.bitsOfPrecision = geohash.length();
		}
		public GeoHashImpl(boolean[] geohash) {
			this.geohashArray = geohash;
			this.geohashString  = this.boolArrayToString(geohash);
			this.bitsOfPrecision = geohash.length;
		}
		
        public GeoHashImpl(double lat, double lon, int bitsOfPrecision) {
            // @ToDo, fill this in
            // You are free to change the constructor parameters. 
        	this.bitsOfPrecision = bitsOfPrecision;
        	this.geohashArray = this.geohash(lat, lon, bitsOfPrecision);
        	this.geohashString = this.toHashString(this.geohashArray); 
        }

        public int bitsOfPrecision() {
        	return this.bitsOfPrecision;
            // return -1;
        }
        
        public String getGeoHashString() {
        	return this.geohashString;
        }
        /**
         * Similar to "substring" on Strings. This method should
         * return the first n bits of the GeoHash as a new
         * GeoHash.
         *
         * @param n
         * @return
         */
        public GeoHashImpl prefix(int n){
        	return new GeoHashImpl(
        			this.geohashString.substring(0, n));
            // return null;
        }

        public GeoHashImpl northNeighbor() {
            // @Bonus, this is not required, but is a nice challenge
            // for bonus points
            return null;
        }

        public GeoHashImpl southNeighbor() {
            // @Bonus, this is not required, but is a nice challenge
            // for bonus points
            return null;
        }

        public GeoHashImpl westNeighbor() {
            // @Bonus, this is not required, but is a nice challenge
            // for bonus points
            return null;
        }

        public GeoHashImpl eastNeighbor() {
            // @Bonus, this is not required, but is a nice challenge
            // for bonus points
            return null;
        }

        @Override
        public Iterator<Boolean> iterator() {
           // @ToDo, create an iterator for the bits in the GeoHash
        	// convert geohash array to arraylist 
        	List<Boolean> list = new ArrayList<Boolean>();
        	for (boolean b:this.geohashArray) {
        		list.add(b);
        	}		
        	Iterator<Boolean> iterator = list.iterator();
        	return iterator;
        }

        @Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			GeoHashImpl other = (GeoHashImpl) obj;
			if (bitsOfPrecision != other.bitsOfPrecision)
				return false;
			if (!Arrays.equals(geohashArray, other.geohashArray))
				return false;
			if (geohashString == null) {
				if (other.geohashString != null)
					return false;
			} else if (!geohashString.equals(other.geohashString))
				return false;
			return true;
		}

        @Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + bitsOfPrecision;
			result = prime * result + Arrays.hashCode(geohashArray);
			result = prime * result + ((geohashString == null) ? 0 : geohashString.hashCode());
			return result;
		}

        @Override
		public String toString() {
			return "GeoHashImpl [geohashString=" + geohashString + ", geohashArray=" + Arrays.toString(geohashArray)
					+ ", bitsOfPrecision=" + bitsOfPrecision + "]";
		}



    }
