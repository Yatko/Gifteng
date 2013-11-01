package com.venefica.common;

import com.venefica.dao.DBType;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class GeoUtils {
    
    private static final int SRID = 4326;
    private static final GeometryFactory GEOM_FACTORY = new GeometryFactory(new PrecisionModel(), SRID);
    
    //public static final double METERS_IN_ONE_DEGREE = 111319.9;
    private static final double R_KM = 6371; // in kilometers
    private static final double R_MI = 3959; // in miles
    private static final boolean USE_MI = true; //use miles or kilometers
    
    /**
     * y - latitude
     * x - longitude
     * 
     * @param latitude
     * @param longitude
     * @return 
     */
    public static Point createPoint(Double latitude, Double longitude) {
        return GEOM_FACTORY.createPoint(new Coordinate(longitude, latitude));
    }
    
    public static double getHarvesineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        
        BigDecimal decimal = new BigDecimal((USE_MI ? R_MI : R_KM) * c);
        decimal = decimal.setScale(4, RoundingMode.DOWN);
        return decimal.doubleValue();
    }
    
    public static String getHarvesineSQL(DBType dbType) {
        String funxc_x = "x";
        String funxc_y = "y";
                
        if ( dbType == DBType.MYSQL ) {
            funxc_x = "x";
            funxc_y = "y";
        } else if ( dbType == DBType.POSTGRESQL ) {
            funxc_x = "st_x";
            funxc_y = "st_y";
        }
        
        String curPosLat = "radians(" + funxc_y + "(:curPos))"; //latitude
        String curPosLng = "radians(" + funxc_x + "(:curPos))"; //longitude
        String lat = "radians(" + funxc_y + "(a.adData.location))";
        String lng = "radians(" + funxc_x + "(a.adData.location))";
        double R = (USE_MI ? R_MI : R_KM);
        
        return "(" + R + " * acos(cos(" + curPosLat + ") * cos(" + lat + ") * cos(" + lng + " - " + curPosLng + ") + sin(" + curPosLat + ") * sin(" + lat + ")))";
    }
}
