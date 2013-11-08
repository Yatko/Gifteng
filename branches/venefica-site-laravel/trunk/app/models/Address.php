<?php

class Address {
    
    public $id; //long
    public $name; //string
    public $address1; //string
    public $address2; //string
    public $city; //string
    public $county; //string
    public $country; //string
    public $stateAbbreviation; //string (2 chars)
    public $state; //string
    public $area; //string
    public $zipCode; //string
    public $longitude; //double
    public $latitude; //double
    
    
    public function __construct($obj = null) {
        
        if ( $obj != null ) {
        	foreach(get_class_vars(__CLASS__) as $k=>$v) {
        		if(isset($obj->$k))
					$this->$k = $obj->$k;
				elseif(is_array($obj) && isset($obj[$k]))
					$this->$k = $obj[$k];
        	}
        }
    }
	
}