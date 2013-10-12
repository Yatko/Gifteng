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
    
    const STATUS_OFLINE = 'OFFLINE'; //unapproved ad status (status will be changed by admin user approval)
    const STATUS_ACTIVE = 'ACTIVE'; //there is no active request for this ad
    const STATUS_IN_PROGRESS = 'IN_PROGRESS'; //there is an (one or more) active request for this ad
    const STATUS_FINALIZED = 'FINALIZED';
    const STATUS_EXPIRED = 'EXPIRED';
    
    var $id; //long
    var $revision; //integer
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
    var $inBookmarks; //boolean
    var $sold; //boolean
    var $expired; //boolean
    var $requested; //boolean
    var $online; //boolean
    var $approved; //boolean
    var $expires; //boolean
    var $availableAt; //long - timestamp
    var $soldAt; //long - timestamp
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
    var $status; //enum: OFFLINE, ACTIVE, IN_PROGRESS, FINALIZED, EXPIRED
    var $requests; //array of Request_model
    var $canRequest; //boolean
    var $canRelist; //boolean
    var $statistics; //AdStatistics_model
    var $approval; //Approval_model
    
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
            $this->revision = getField($obj, 'revision');
            $this->categoryId = getField($obj, 'categoryId');
            $this->category = getField($obj, 'category');
            $this->title = getField($obj, 'title');
            $this->subtitle = getField($obj, 'subtitle');
            $this->description = getField($obj, 'description');
            $this->price = getField($obj, 'price');
            $this->quantity = getField($obj, 'quantity');
            $this->createdAt = getField($obj, 'createdAt');
            $this->owner = getField($obj, 'owner');
            $this->inBookmarks = getField($obj, 'inBookmarks');
            $this->sold = getField($obj, 'sold');
            $this->expired = getField($obj, 'expired');
            $this->requested = getField($obj, 'requested');
            $this->online = getField($obj, 'online');
            $this->approved = getField($obj, 'approved');
            $this->expires = getField($obj, 'expires');
            $this->availableAt = getField($obj, 'availableAt');
            $this->soldAt = getField($obj, 'soldAt');
            $this->expiresAt = getField($obj, 'expiresAt');
            $this->canRate = getField($obj, 'canRate');
            $this->canMarkAsSpam = getField($obj, 'canMarkAsSpam');
            $this->canRequest = getField($obj, 'canRequest');
            $this->canRelist = getField($obj, 'canRelist');
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
            if ( hasField($obj, 'approval') ) {
                $this->approval = Approval_model::convertApproval($obj->approval);
            }
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Ad";
        return parent::__get($key);
    }
    
    // helper urls
    
    public function getImageUrl($size) {
        $url = '';
        if ( $this->hasImage() ) {
            $url = get_image_url($this->image->url, IMAGE_TYPE_AD, $size);
        }
        if ( trim($url) == '' ) {
            $url = DEFAULT_AD_URL;
        }
        return $url;
    }
    
//    public function getCreatorAvatarUrl() {
//        if ( $this->creator == null ) {
//            return DEFAULT_USER_URL;
//        }
//        return $this->creator->getAvatarUrl();
//    }
    
    public function getCreatorProfileUrl() {
        if ( $this->creator == null ) {
            return '';
        }
        return $this->creator->getProfileUrl();
    }
    
    public function getViewUrl() {
        return base_url() . 'view/' . $this->id;
    }
    
    // image related
    
    public function hasImage() {
        if ( $this->image != null ) {
            return TRUE;
        }
        return FALSE;
    }
    
    // creator related
    
    public function getCreatorFullName() {
        if ( $this->creator == null ) {
            return '&nbsp;';
        }
        return $this->creator->getFullName();
    }
    
//    public function getCreatorJoinDate() {
//        if ( $this->creator == null ) {
//            return '';
//        }
//        return $this->creator->getJoinDate();
//    }
    
//    public function getCreatorJoinDateHumanTiming() {
//        if ( $this->creator == null ) {
//            return '';
//        }
//        return $this->creator->getJoinDateHumanTiming();
//    }
    
    public function getCreatorLocation() {
        if ( $this->creator == null ) {
            return '';
        }
        return $this->creator->getLocation();
    }
    
    public function getCreatorPoints() {
        if ( $this->creator == null ) {
            return '';
        }
        return $this->creator->getPoints();
    }
    
    // ad related
    
    public function getPickUpForFormElement() {
        return $this->pickUp ? '1' : '0';
    }
    
    public function getFreeShippingForFormElement() {
        return $this->freeShipping ? '1' : '0';
    }
    
    public function isBusiness() {
        return $this->type == Ad_model::ADTYPE_BUSINESS;
    }
    
    public function isOnline() {
        return $this->place == Ad_model::PLACE_ONLINE;
    }
    
    public function getCreateDate() {
        if ( $this->createdAt == null ) {
            return '';
        }
        return convertTimestampToDate($this->createdAt / 1000);
    }
    
    public function getExpireDate() {
        if ( $this->expiresAt == null ) {
            return '';
        }
        return convertTimestampToDate($this->expiresAt / 1000);
    }
    
    public function getLocation() {
        if ( $this->address == null ) {
            return '';
        }
        return $this->address->getLocation();
    }
    
    public function imageSafeLoadData() {
        if ( $this->image != null ) {
            $this->image->safeLoadData();
        }
    }
    
    public function getSafeTitle() {
        return safe_content($this->title);
    }
    
    public function getSafeSubtitle() {
        return safe_content($this->subtitle);
    }
    
    public function getSafeDescription($convertLinks = false) {
        $ret = safe_content($this->description);
        if ( $convertLinks ) {
            $ret = auto_link($ret, 'both', true);
        }
        return $ret;
    }
    
    // requests related
    
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
        foreach ( $this->requests as $request ) {
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
    
    public function isMaxAllowedRequestsReached() {
        if ( !$this->hasRequest() ) {
            return false;
        }
        if ( count($this->requests) >= MAX_ALLOWED_REQUESTS ) {
            return true;
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
