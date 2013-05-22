<?php

/**
 * Description of Address DTO
 *
 * @author gyuszi
 */
class Address_model extends CI_Model {
    
    var $address1; //string
    var $address2; //string
    var $city; //string
    var $county; //string
    var $country; //string
    var $state; //string
    var $area; //string
    var $zipCode; //string
    var $latitude; //double
    var $longitude; //double
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing Address_model");
        
        if ( $obj != null ) {
            $this->address1 = getField($obj, 'address1');
            $this->address2 = getField($obj, 'address2');
            $this->city = getField($obj, 'city');
            $this->county = getField($obj, 'county');
            $this->country = getField($obj, 'country');
            $this->state = getField($obj, 'state');
            $this->area = getField($obj, 'area');
            $this->zipCode = getField($obj, 'zipCode');
            $this->latitude = getField($obj, 'latitude');
            $this->longitude = getField($obj, 'longitude');
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Address";
        return parent::__get($key);
    }
    
    public function getLocation() {
        $separator = ', ';
        $ret = $this->city . $separator . $this->country;
        if ( trim($ret) == trim($separator) ) {
            $ret = '';
        }
        return $ret;
    }
    
    // static helpers
    
    public static function convertAddress($address) {
        return new Address_model($address);
    }
}
