/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.venefica.common;

import com.vividsolutions.jts.geom.Point;
import org.junit.Test;

/**
 *
 * @author gyuszi
 */
public class GeoUtilsTest {
    
    @Test
    public void testWKT() {
        Point positon = GeoUtils.createPoint(new Double("20.7118088"), new Double("77.0256938"));
        System.out.println("position WKT: " + positon.toText());
        System.out.println("position geometry type: " + positon.getGeometryType());
    }
}
