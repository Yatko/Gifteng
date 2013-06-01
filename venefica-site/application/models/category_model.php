<?php

/**
 * Description of Category DTO
 */
class Category_model extends CI_Model {
    
    var $id; //long
    var $parentId; //long
    var $subcategories; //array of Category_model
    var $name; //string
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing Category_model");
        
        if ( $obj != null ) {
            $this->id = getField($obj, 'id');
            $this->parentId = getField($obj, 'parentId');
            $this->name = getField($obj, 'name');
            if ( hasField($obj, 'subcategories') ) {
                $this->subcategories = Category_model::convertCategories($obj->subcategories);
            }
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Category";
        return parent::__get($key);
    }
    
    // static helpers
    
    public static function convertCategories($categoriesResult) {
        $categories = array();
        if ( is_array($categoriesResult) && count($categoriesResult) > 0 ) {
            foreach ( $categoriesResult as $category ) {
                array_push($categories, Category_model::convertCategory($category));
            }
        } elseif ( !is_empty($categoriesResult) ) {
            $category = $categoriesResult;
            array_push($categories, Category_model::convertCategory($category));
        }
        return $categories;
    }
    
    public static function convertCategory($category) {
        return new Category_model($category);
    }
}

?>
