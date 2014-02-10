<?php

/**
 * Description of BusinessCategory DTO
 */
class BusinessCategory_model extends CI_Model {
    
    var $id; //long
    var $name; //string
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing BusinessCategory_model");
        
        if ( $obj != null ) {
            $this->id = getField($obj, 'id');
            $this->name = getField($obj, 'name');
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "BusinessCategory";
        return parent::__get($key);
    }
    
    // static helpers
    
    public static function convertBusinessCategories($categoriesResult) {
        $categories = array();
        if ( is_array($categoriesResult) && count($categoriesResult) > 0 ) {
            foreach ( $categoriesResult as $category ) {
                array_push($categories, BusinessCategory_model::convertBusinessCategory($category));
            }
        } elseif ( !is_empty($categoriesResult) ) {
            $category = $categoriesResult;
            array_push($categories, BusinessCategory_model::convertBusinessCategory($category));
        }
        return $categories;
    }
    
    public static function convertBusinessCategory($category) {
        return new BusinessCategory_model($category);
    }
}

?>
