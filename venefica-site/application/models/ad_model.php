<?php

/**
 * Description of Ad DTO
 *
 * @author gyuszi
 */
class Ad_model extends CI_Model {
    
    var $id; //long
    var $categoryId; //long
    var $category; //string
    var $title; //string
    var $description; //string
    var $price; //float
    var $latitude; //float
    var $longitude; //float
    var $image; //Image_model
    var $imageThumbnail; //Image_model
    var $images; //array of Image_model
    var $createdAt; //long - timestamp
    var $owner; //boolean
    var $inBookmars; //boolean
    var $wanted; //boolean
    var $expired; //boolean
    var $expiresAt; //long - timestamp
    var $numAvailProlongations; //int
    var $numViews; //int
    var $rating; //float
    var $canRate; //boolean
    var $creator; //User_model
    var $canMarkAsSpam; //boolean
    
    var $comments; //array of Comment_model
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing Ad_model");
        
        if ( $obj != null ) {
            $this->id = getField($obj, 'id');
            $this->categoryId = getField($obj, 'categoryId');
            $this->category = getField($obj, 'category');
            $this->title = getField($obj, 'title');
            $this->description = getField($obj, 'description');
            $this->price = getField($obj, 'price');
            $this->latitude = getField($obj, 'latitude');
            $this->longitude = getField($obj, 'longitude');
            $this->image = hasField($obj, 'image') ? new Image_model($obj->image) : null;
            $this->imageThumbnail = hasField($obj, 'imageThumbnail') ? new Image_model($obj->imageThumbnail) : null;
            if ( hasField($obj, 'images') && hasField($obj->images, 'item') && $obj->images->item != null ) {
                $images = array();
                if ( is_array($obj->images->item) && count($obj->images->item) > 0 ) {
                    foreach ( $obj->images->item as $image ) {
                        array_push($images, new Image_model($image));
                    }
                } else {
                    $image = $obj->images->item;
                    array_push($images, new Image_model($image));
                }
                $this->images = $images;
            }
            $this->createdAt = getField($obj, 'createdAt');
            $this->owner = getField($obj, 'owner');
            $this->inBookmars = getField($obj, 'inBookmars');
            $this->wanted = getField($obj, 'wanted');
            $this->expired = getField($obj, 'expired');
            $this->expiresAt = getField($obj, 'expiresAt');
            $this->numAvailProlongations = getField($obj, 'numAvailProlongations');
            $this->numViews = getField($obj, 'numViews');
            $this->rating = getField($obj, 'rating');
            $this->canRate = getField($obj, 'canRate');
            $this->creator = hasField($obj, 'creator') ? new User_model($obj->creator) : null;
            $this->canMarkAsSpam = getField($obj, 'canMarkAsSpam');
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Ad";
        return parent::__get($key);
    }
    
    public function hasImage() {
        if ( $this->image != null ) {
            return TRUE;
        }
        return FALSE;
    }
    
    public function getImageUrl() {
        if ( !$this->hasImage() ) {
            return '';
        }
        return SERVER_URL.$this->image->url;
    }
    
    // creator related
    
    public function getCreatorFullName() {
        if ( $this->creator == null ) {
            return '';
        }
        return $this->creator->getFullName();
    }
    
    public function getCreatorAvatarUrl() {
        if ( $this->creator == null ) {
            return '';
        }
        return $this->creator->getAvatarUrl();
    }
    
    public function getCreatorJoinDate() {
        if ( $this->creator == null ) {
            return '';
        }
        return $this->creator->getJoinDate();
    }
    
    public function getCreatorLocation() {
        if ( $this->creator == null ) {
            return '';
        }
        return $this->creator->getLocation();
    }
}
