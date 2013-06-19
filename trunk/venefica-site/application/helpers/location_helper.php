<?php

// geo location related helper methods

if (!function_exists('distance_haversine')) {
    
    //
    // algorythm taken from: http://sgowtham.net/ramblings/2009/08/04/php-calculating-distance-between-two-locations-given-their-gps-coordinates/
    //
    function distance_haversine($lat1, $lon1, $lat2, $lon2) {
        $precision = 1; //4
        $earth_radius = 3960.00; # in miles
        $delta_lat = $lat2 - $lat1 ;
        $delta_lon = $lon2 - $lon1 ;
        $alpha = $delta_lat / 2;
        $beta = $delta_lon / 2;
        $a = sin(deg2rad($alpha)) * sin(deg2rad($alpha)) + cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * sin(deg2rad($beta)) * sin(deg2rad($beta));
        $c = asin(min(1, sqrt($a)));
        $distance = 2 * $earth_radius * $c;
        $distance = round($distance, $precision);

        return $distance;
    }
}

if (!function_exists('getLocationByZipCode')) {
    
    /**
     * Returns an assiciative array as:
     * 
     * array(
     *      'longitude' => xxx,
     *      'latitude' => yyy
     * )
     * 
     * If the given zip code is empty  a (0, 0) coordinated location is returned
     * which is invalid.
     * 
     * @param type $zipCode
     * @return array
     */
    function getLocationByZipCode($zipCode) {
        if ( empty($zipCode) ) {
            return array(
                'longitude' => '0',
                'latitude' => '0'
            );
        }
        
        /**
        Samples: http://maps.huge.info/

        Simple result for: http://www.usnaviguide.com/zip.pl?ZIP=08904

        <polylines>
            <marker1 lat="40.50316" lng="-74.42666"/>
            <info hitrem="134" zipname="Highland Park" zipcode="08904" county="23" ctyname="Middlesex" uspsst="NJ" stname="New Jersey" complex="732/848" pointzip="0"/>
        </polylines>
        /**/

        $url = 'http://www.usnaviguide.com/zip.pl?ZIP=' . $zipCode;
        $fileContents = file_get_contents($url);
        $fileContents = str_replace(array("\n", "\r", "\t"), '', $fileContents);
        $fileContents = trim(str_replace('"', "'", $fileContents));
        $simpleXml = simplexml_load_string($fileContents);
        
        $longitude = $simpleXml->marker1['lng'];
        $latitude = $simpleXml->marker1['lat'];
        
        return array(
            'longitude' => $longitude,
            'latitude' => $latitude
        );
    }
}
