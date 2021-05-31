package edu.vanderbilt.cs.live3;

public class GeoHashFactoryImpl implements GeoHashFactory {

    /** 
     *
     * @param lat
     * @param lon
     * @param bitsOfPrecision
     * @return
     */
    public GeoHash with(double lat, double lon, int bitsOfPrecision) {
    	return new GeoHashImpl(lat, lon, bitsOfPrecision); 
    }

}
