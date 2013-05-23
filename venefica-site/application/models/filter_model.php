<?php

/**
 * Description of Filter DTO
 *
 * @author gyuszi
 */
class Filter_model extends CI_Model {
    
    var $searchString; //string
    var $categories; //array of long
    var $distance; //long
    var $latitude; //double
    var $longitude; //double
    var $minPrice; //float
    var $maxPrice; //float
    var $hasPhoto; //boolean
    var $type; //enum: MEMBER, BUSINESS
    var $includeOwned; //boolean
    
    public function __construct() {
        log_message(DEBUG, "Initializing Filter_model");
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Filter";
        return parent::__get($key);
    }
}
