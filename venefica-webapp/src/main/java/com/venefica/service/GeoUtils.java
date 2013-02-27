package com.venefica.service;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

public class GeoUtils {
    
    // @formatter:off
    private static final int SRID = 4326;
    private static final GeometryFactory GEOM_FACTORY = new GeometryFactory(new PrecisionModel(), SRID);
    // @formatter:on

    public static Point createPoint(Coordinate coordinate) {
        return GEOM_FACTORY.createPoint(coordinate);
    }

    public static Point createPoint(Double latitude, Double longitude) {
        return createPoint(new Coordinate(longitude, latitude));
    }
}