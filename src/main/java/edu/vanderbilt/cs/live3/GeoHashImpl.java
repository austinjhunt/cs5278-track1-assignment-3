package edu.vanderbilt.cs.live3;

import java.util.Iterator;

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
public class GeoHashImpl implements GeoHash {


        public GeoHashImpl() {
            // @ToDo, fill this in
            // You are free to change the constructor parameters.
        }

        public int bitsOfPrecision() {
            return -1;
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
            return null;
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
            return null;
        }

        @Override
        public boolean equals(Object o) {
            // @ToDo, fill this in
            // Always override equals and hashcode together
            // Your IDE can probably do this part for you
            return false;
        }

        @Override
        public int hashCode() {
            // @ToDo, fill this in
            // Always override equals and hashcode together
            // Your IDE can probably do this part for you
            return 0;
        }

        public String toString() {
            // @ToDo, fill this in

            return null;
        }



    }
