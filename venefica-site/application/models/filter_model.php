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
    var $wanted; //boolean
    
    public function __construct() {
        log_message(DEBUG, "Initializing Filter_model");
    }
}
