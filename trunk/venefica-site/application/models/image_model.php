<?php

/**
 * Description of Image DTO
 *
 * @author gyuszi
 */
class Image_model extends CI_Model {
    
    var $id; //long
    var $url; //string
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing Image_model");
        
        if ( $obj != null ) {
            $this->id = getField($obj, 'id');
            $this->url = getField($obj, 'url');
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Image";
        return parent::__get($key);
    }
    
    public function toString() {
        return "Image ["
            ."id=".$this->id.", "
            ."url=".$this->url.""
            ."]";
    }
    
    // static helpers
    
    public static function convertImages($imagesResult) {
        $images = array();
        if ( is_array($imagesResult) && count($imagesResult) > 0 ) {
            foreach ( $imagesResult as $image ) {
                array_push($images, Image_model::convertImage($image));
            }
        } elseif ( !is_empty($imagesResult) ) {
            $image = $imagesResult;
            array_push($images, Image_model::convertImage($image));
        }
        return $images;
    }
    
    public static function convertImage($image) {
        return new Image_model($image);
    }
}
