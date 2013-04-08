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
        // Call the Model constructor
        parent::__construct();
        
        if ( $obj != null ) {
            $this->id = $obj->id;
            $this->categoryId = $obj->categoryId;
            $this->category = $obj->category;
            $this->title = $obj->title;
            $this->description = $obj->description;
            $this->price = $obj->price;
            $this->latitude = getField($obj, 'latitude');
            $this->longitude = getField($obj, 'longitude');
            if ( property_exists($obj, 'image') ) {
                $this->image = new Image_model($obj->image);
            }
            if ( property_exists($obj, 'imageThumbnail') ) {
                $this->imageThumbnail = new Image_model($obj->imageThumbnail);
            }
            if ( property_exists($obj, 'images') ) {
                if ( $obj->images != null && property_exists($obj->images, 'item') && $obj->images->item != null ) {
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
            }
            $this->createdAt = $obj->createdAt;
            $this->owner = $obj->owner;
            $this->inBookmars = $obj->inBookmars;
            $this->wanted = $obj->wanted;
            $this->expired = $obj->expired;
            $this->expiresAt = $obj->expiresAt;
            $this->numAvailProlongations = $obj->numAvailProlongations;
            $this->numViews = $obj->numViews;
            $this->rating = $obj->rating;
            $this->canRate = getField($obj, 'canRate');
            if ( property_exists($obj, 'creator') ) {
                $this->creator = new User_model($obj->creator);
            }
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
}
