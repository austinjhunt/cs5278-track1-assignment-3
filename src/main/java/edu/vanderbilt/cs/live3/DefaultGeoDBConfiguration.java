package edu.vanderbilt.cs.live3;

public class DefaultGeoDBConfiguration {

    /**
     * @ToDo
     *
     * Update to return instances of your factory
     *
     * @return
     */
    public GeoDBFactory getDBFactory(){
    	return new GeoDBFactory();
        // return null;
    }

    /**
     * @ToDo
     *
     * Update to return instances of your factory
     *
     * @return
     */
    public GeoHashFactory getHashFactory(){
        return new GeoHashFactoryImpl();
    	// return null;
        
    }

}
