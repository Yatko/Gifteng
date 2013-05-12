<?php

/**
 * Description of Category DTO
 */
class Category_model extends CI_Model {
    
    var $id; //long
    var $parentId; //long
    var $subcategories; //array of Category_model
    var $name; //string
    
    public function __construct() {
        log_message(DEBUG, "Initializing Category_model");
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Category";
        return parent::__get($key);
    }
}

?>
