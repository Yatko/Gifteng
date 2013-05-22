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
    var $subtitle; //string
    var $description; //string
    var $price; //float
    var $quantity; //int
    var $image; //Image_model
    var $imageThumbnail; //Image_model
    var $images; //array of Image_model
    var $createdAt; //long - timestamp
    var $owner; //boolean
    var $inBookmars; //boolean
    var $expired; //boolean
    var $sent; //boolean
    var $received; //boolean
    var $requested; //boolean
    var $expiresAt; //long - timestamp
    var $numAvailProlongations; //int
    var $numViews; //int
    var $rating; //float
    var $canRate; //boolean
    var $creator; //User_model
    var $canMarkAsSpam; //boolean
    var $freeShipping; //boolean
    var $pickUp; //boolean
    var $comments; //array of Comment_model
    var $address; //Address_model
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing Ad_model");
        
        if ( $obj != null ) {
            $this->id = getField($obj, 'id');
            $this->categoryId = getField($obj, 'categoryId');
            $this->category = getField($obj, 'category');
            $this->title = getField($obj, 'title');
            $this->subtitle = getField($obj, 'subtitle');
            $this->description = getField($obj, 'description');
            $this->price = getField($obj, 'price');
            $this->quantity = getField($obj, 'quantity');
            $this->createdAt = getField($obj, 'createdAt');
            $this->owner = getField($obj, 'owner');
            $this->inBookmars = getField($obj, 'inBookmars');
            $this->expired = getField($obj, 'expired');
            $this->sent = getField($obj, 'sent');
            $this->received = getField($obj, 'received');
            $this->requested = getField($obj, 'requested');
            $this->expiresAt = getField($obj, 'expiresAt');
            $this->numAvailProlongations = getField($obj, 'numAvailProlongations');
            $this->numViews = getField($obj, 'numViews');
            $this->rating = getField($obj, 'rating');
            $this->canRate = getField($obj, 'canRate');
            $this->canMarkAsSpam = getField($obj, 'canMarkAsSpam');
            $this->freeShipping = getField($obj, 'freeShipping');
            $this->pickUp = getField($obj, 'pickUp');
            if ( hasField($obj, 'creator') ) {
                $this->creator = User_model::convertUser($obj->creator);
            }
            if ( hasField($obj, 'image') ) {
                $this->image = Image_model::convertImage($obj->image);
            }
            if ( hasField($obj, 'imageThumbnail') ) {
                $this->imageThumbnail = Image_model::convertImage($obj->imageThumbnail);
            }
            if ( hasField($obj, 'images') && hasField($obj->images, 'item') && $obj->images->item != null ) {
                $this->images = Image_model::convertImages($obj->images->item);
            }
            if ( hasField($obj, 'comments') && hasField($obj->comments, 'item') && $obj->comments->item != null ) {
                $this->comments = Comment_model::convertComments($obj->comments->item);
            }
            if ( hasField($obj, 'address') ) {
                $this->address = Address_model::convertAddress($obj->address);
            }
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Ad";
        return parent::__get($key);
    }
    
    // image related
    
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
    
    // ad related
    
    public function getCreateDate() {
        if ( $this->createdAt == null ) {
            return '';
        }
        return date(DATE_FORMAT, $this->createdAt / 1000);
    }
    
    public function getExpireDate() {
        if ( $this->expiresAt == null ) {
            return '';
        }
        return date(DATE_FORMAT, $this->expiresAt / 1000);
    }
    
    // static helpers
    
    public static function convertAds($adsResult) {
        $ads = array();
        if ( is_array($adsResult) && count($adsResult) > 0 ) {
            foreach ( $adsResult as $ad ) {
                array_push($ads, Ad_model::convertAd($ad));
            }
        } else {
            $ad = $adsResult;
            array_push($ads, Ad_model::convertAd($ad));
        }
        return $ads;
    }
    
    public static function convertAd($ad) {
        return new Ad_model($ad);
    }
}
