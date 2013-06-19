<?php

/**
 * Description of Ad DTO
 *
 * @author gyuszi
 */
class Ad_model extends CI_Model {
    
    const PLACE_ONLINE = 'ONLINE';
    const PLACE_LOCATION = 'LOCATION';
    
    const ADTYPE_MEMBER = 'MEMBER';
    const ADTYPE_BUSINESS = 'BUSINESS';
    
    const WEEKDAY_ALL_WEEK = '*';
    const WEEKDAY_MONDAY = 'MONDAY';
    const WEEKDAY_TUESDAY = 'TUESDAY';
    const WEEKDAY_WEDNESDAY = 'WEDNESDAY';
    const WEEKDAY_THURSDAY = 'THURSDAY';
    const WEEKDAY_FRIDAY = 'FRIDAY';
    const WEEKDAY_SATURDAY = 'SATURDAY';
    const WEEKDAY_SUNDAY = 'SUNDAY';
    
    const STATUS_ACTIVE = 'ACTIVE';
    const STATUS_EXPIRED = 'EXPIRED';
    const STATUS_SELECTED = 'SELECTED';
    const STATUS_SENT = 'SENT';
    const STATUS_RECEIVED = 'RECEIVED';
    
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
    var $expires; //boolean
    var $availableAt; //long - timestamp
    var $expiresAt; //long - timestamp
    var $canRate; //boolean
    var $creator; //User_model
    var $canMarkAsSpam; //boolean
    var $freeShipping; //boolean
    var $pickUp; //boolean
    var $place; //enum: ONLINE, LOCATION
    var $type; //enum: MEMBER, BUSINESS
    var $comments; //array of Comment_model
    var $address; //Address_model
    var $status; //enum: ACTIVE, EXPIRED, SELECTED, SENT, RECEIVED
    var $requests; //array of Request_model
    var $statistics; //AdStatistics_model
    
    // business ad data
    var $promoCode; //string
    var $website; //string
    var $needsReservation; //boolean
    var $availableFromTime; //long - time
    var $availableToTime; //long - time
    var $availableAllDay; //boolean
    var $availableDays; //array of enum: MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    var $imageBarcode; //Image_model
    
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
            $this->expires = getField($obj, 'expires');
            $this->availableAt = getField($obj, 'availableAt');
            $this->expiresAt = getField($obj, 'expiresAt');
            $this->canRate = getField($obj, 'canRate');
            $this->canMarkAsSpam = getField($obj, 'canMarkAsSpam');
            $this->freeShipping = getField($obj, 'freeShipping');
            $this->pickUp = getField($obj, 'pickUp');
            $this->place = getField($obj, 'place');
            $this->type = getField($obj, 'type');
            $this->status = getField($obj, 'status');
            
            //business ad data
            $this->promoCode = getField($obj, 'promoCode');
            $this->website = getField($obj, 'website');
            $this->needsReservation = getField($obj, 'needsReservation');
            $this->availableFromTime = getField($obj, 'availableFromTime');
            $this->availableToTime = getField($obj, 'availableToTime');
            $this->availableAllDay = getField($obj, 'availableAllDay');
            $this->availableDays = getField($obj, 'availableDays');
            
            if ( hasField($obj, 'creator') ) {
                $this->creator = User_model::convertUser($obj->creator);
            }
            if ( hasField($obj, 'image') ) {
                $this->image = Image_model::convertImage($obj->image);
            }
            if ( hasField($obj, 'imageThumbnail') ) {
                $this->imageThumbnail = Image_model::convertImage($obj->imageThumbnail);
            }
            if ( hasField($obj, 'imageBarcode') ) {
                $this->imageBarcode = Image_model::convertImage($obj->imageBarcode);
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
            if ( hasField($obj, 'requests') && hasField($obj->requests, 'item') && $obj->requests->item != null ) {
                $this->requests = Request_model::convertRequests($obj->requests->item);
            }
            if ( hasField($obj, 'statistics') ) {
                $this->statistics = AdStatistics_model::convertAdStatistics($obj->statistics);
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
    
    public function getLocation() {
        if ( $this->address == null ) {
            return '';
        }
        return $this->address->getLocation();
    }
    
    // static helpers
    
    public static function convertAds($adsResult) {
        $ads = array();
        if ( is_array($adsResult) && count($adsResult) > 0 ) {
            foreach ( $adsResult as $ad ) {
                array_push($ads, Ad_model::convertAd($ad));
            }
        } elseif ( !is_empty($adsResult) ) {
            $ad = $adsResult;
            array_push($ads, Ad_model::convertAd($ad));
        }
        return $ads;
    }
    
    public static function convertAd($ad) {
        return new Ad_model($ad);
    }
}
