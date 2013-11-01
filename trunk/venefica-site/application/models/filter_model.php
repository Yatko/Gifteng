<?php

/**
 * Description of Filter DTO
 *
 * @author gyuszi
 */
class Filter_model extends CI_Model {
    
    const TYPE_MEMBER = "MEMBER";
    const TYPE_BUSINESS = "BUSINESS";
    
    const FILTER_TYPE_ACTIVE = "ACTIVE";
    const FILTER_TYPE_GIFTED = "GIFTED";
    
    var $searchString; //string
    var $categories; //array of long
    var $distance; //long
    var $longitude; //double
    var $latitude; //double
    var $minPrice; //float
    var $maxPrice; //float
    var $hasPhoto; //boolean
    var $type; //enum: MEMBER, BUSINESS
//    var $includeOwned; //boolean
    var $orderAsc; //boolean
    var $orderClosest; //boolean
    var $filterType; //enum: ACTIVE, GIFTED
//    var $includeInactive; //boolean
//    var $includeShipped; //boolean
//    var $includeRequested; //boolean
//    var $includeCanRequest; //boolean
    
    public function __construct() {
        log_message(DEBUG, "Initializing Filter_model");
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Filter";
        return parent::__get($key);
    }
}
