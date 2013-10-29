<?php

class ImageModel {
    
    const IMGTYPE_JPEG = 'JPEG';
    const IMGTYPE_PNG = 'PNG';
    
    public $id; //long
    public $imgType; //enum: JPEG, PNG
    public $data; //byte[]
    public $url; //string
    
    public function __construct($obj = null) {
        if ( $obj != null ) {
        	foreach(get_class_vars(__CLASS__) as $k=>$v) {
        		if(isset($obj->$k))
					$this->$k = $obj->$k;
        	}
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Image";
        return parent::__get($key);
    }
    
    public static function convertImage($image) {
        return new ImageModel($image);
    }
    
    public static function getImgTypeByExtension($image_file_name) {
        if ( pathinfo($image_file_name, PATHINFO_EXTENSION) == "png" ) {
            return ImageModel::IMGTYPE_PNG;
        }
        return ImageModel::IMGTYPE_JPEG;
    }
    
    public static function createImageModel($image_file_name) {
        if ( empty($image_file_name) ) {
            return null;
        }
        $image = new ImageModel();
        $image->imgType = ImageModel::getImgTypeByExtension($image_file_name);
        $image->data = file_get_contents(app_path().'/storage/uploads/'. $image_file_name);
        return $image;
    }
}
