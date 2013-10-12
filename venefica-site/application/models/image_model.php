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
    
    /**
     * Detects and returns the url of the image. This should be used at file
     * upload.
     * 
     * @return string
     */
    public function getDetectedImageUrl($type, $size) {
        $url = null;
        if ( $this->url != null ) {
            if ( $this->isUploaded() ) {
                $url = base_url() . 'get_photo/' . $this->url;
            } else {
                $url = get_image_url($this->url, $type, $size);
            }
        }
        return $url;
    }
    
    /**
     * Load the data if this was an uploded file.
     */
    public function safeLoadData() {
        if ( $this->isUploaded() ) {
            $image_file_name = $this->url;
            $this->imgType = Image_model::getImgTypeByExtension($image_file_name);
            $this->data = readFileAsString(TEMP_FOLDER .'/'. $image_file_name);
        }
    }
    
    /**
     * Checks if the present url reflects an uploaded file name.
     * 
     * @return boolean
     */
    private function isUploaded() {
        if ( $this->url == null ) {
            return false;
        }
        return startsWith($this->url, UPLOAD_FILE_PREFIX);
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
    
    public static function getImgTypeByExtension($image_file_name) {
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
        $image->imgType = Image_model::getImgTypeByExtension($image_file_name);
        $image->data = readFileAsString(TEMP_FOLDER .'/'. $image_file_name);
        return $image;
    }
}
