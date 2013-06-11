<?php

/**
 * Description of Image DTO
 *
 * @author gyuszi
 */
class Image_model extends CI_Model {
    
    const IMGTYPE_JPEG = 'JPEG';
    const IMGTYPE_PNG = 'PNG';
    
    var $id; //long
    var $imgType; //enum: JPEG, PNG
    var $data; //byte[]
    var $url; //string
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing Image_model");
        
        if ( $obj != null ) {
            $this->id = getField($obj, 'id');
            $this->imgType = getField($obj, 'imgType');
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
    
    public static function getImgType($image_file_name) {
        if ( endsWith(strtolower($image_file_name), "png") ) {
            return Image_model::IMGTYPE_PNG;
        }
        return Image_model::IMGTYPE_JPEG;
    }
    
    public static function createImageModel($image_file_name) {
        if ( is_empty($image_file_name) ) {
            return null;
        }
        $image = new Image_model();
        $image->imgType = Image_model::getImgType($image_file_name);
        $image->data = readFileAsString(TEMP_FOLDER .'/'. $image_file_name);
        return $image;
    }
}
