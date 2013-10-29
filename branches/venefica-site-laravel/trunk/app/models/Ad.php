<?php

class Ad {
    
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
    
    const STATUS_OFLINE = 'OFFLINE'; //unapproved ad status (status will be changed by admin user approval)
    const STATUS_ACTIVE = 'ACTIVE'; //there is no active request for this ad
    const STATUS_IN_PROGRESS = 'IN_PROGRESS'; //there is an (one or more) active request for this ad
    const STATUS_FINALIZED = 'FINALIZED';
    const STATUS_EXPIRED = 'EXPIRED';
    
    public $id; //long
    public $categoryId; //long
    public $category; //string
    public $title; //string
    public $subtitle; //string
    public $description; //string
    public $price; //float
    public $quantity; //int
    public $image; //Image_model
    public $imageThumbnail; //Image_model
    public $images; //array of Image_model
    public $createdAt; //long - timestamp
    public $owner; //boolean
    public $inBookmarks; //boolean
    public $sold; //boolean
    public $expired; //boolean
    public $requested; //boolean
    public $online; //boolean
    public $approved; //boolean
    public $expires; //boolean
    public $availableAt; //long - timestamp
    public $soldAt; //long - timestamp
    public $expiresAt; //long - timestamp
    public $canRate; //boolean
    public $creator; //User_model
    public $canMarkAsSpam; //boolean
    public $freeShipping; //boolean
    public $pickUp; //boolean
    public $place; //enum: ONLINE, LOCATION
    public $type; //enum: MEMBER, BUSINESS
    public $comments; //array of Comment_model
    public $address; //Address_model
    public $status; //enum: OFFLINE, ACTIVE, IN_PROGRESS, FINALIZED, EXPIRED
    public $requests; //array of Request_model
    public $canRequest; //boolean
    public $canRelist; //boolean
    public $statistics; //AdStatistics_model
    public $canEdit;
	public $canDelete;
	public $user_request;
	public $user_requested;
    
    // business ad data
    public $promoCode; //string
    public $website; //string
    public $needsReservation; //boolean
    public $availableFromTime; //long - time
    public $availableToTime; //long - time
    public $availableAllDay; //boolean
    public $availableDays; //array of enum: MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    public $imageBarcode; //Image_model
    
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
        if ( $key == "type" ) return "Ad";
        return parent::__get($key);
    }
    
    // ad related
    
    public function isBusiness() {
        return $this->type == Ad::ADTYPE_BUSINESS;
    }
    
    public function isOnline() {
        return $this->place == Ad::PLACE_ONLINE;
    }
	
    /**
     * Gets the first request made by the given user for this ad.
     * 
     * @param long $userId
     * @return Request_model
     */
    public function getRequestByUser($userId) {
        if ( !$this->hasRequest() ) {
            return null;
        }
		if(isset($this->requests->item->type)) {
			$requests=$this->requests;
		}
		else {
			$requests=$this->requests->item;
		}
		
        foreach ( $requests as $request ) {
            if ( $request->user->id == $userId ) {
                return $request;
            }
        }
        return null;
    }
    
    /**
     * Returns the first accepted request.
     * 
     * @return Request_model
     */
    public function getAcceptedRequest() {
        if ( !$this->hasRequest() ) {
            return null;
        }
        foreach ( $this->requests as $request ) {
            if ( $request->accepted ) {
                return $request;
            }
        }
        return null;
    }
    
    /**
     * Returns the first sent request.
     * 
     * @return Request_model
     */
    public function getSentRequest() {
        if ( !$this->hasRequest() ) {
            return null;
        }
        foreach ( $this->requests as $request ) {
            if ( $request->sent ) {
                return $request;
            }
        }
        return null;
    }
    
    public function hasRequest() {
        if ( $this->requests != null && count($this->requests) > 0 ) {
            return true;
        }
        return false;
    }
    
    /**
     * Verifies if there is at least one active request. Active requests are not
     * having EXPIRED states, but they are PENDING or ACCEPTED.
     * 
     * @return boolean
     */
    public function hasActiveRequest() {
        if ( !$this->hasRequest() ) {
            return false;
        }
        foreach ( $this->requests as $request ) {
            if ( $request->isActive() ) {
                return true;
            }
        }
        return false;
    }
    
    public function hasAcceptedRequest() {
        if ( $this->requests != null && count($this->requests) > 0 ) {
            foreach ( $this->requests as $request ) {
                if ( $request->accepted ) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public function hasSentRequest() {
        if ( $this->requests != null && count($this->requests) > 0 ) {
            foreach ( $this->requests as $request ) {
                if ( $request->sent ) {
                    return true;
                }
            }
        }
        return false;
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
