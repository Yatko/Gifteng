<?php

/**
 * Description of Request DTO
 */
class Request_model extends CI_Model {
    
    const STATUS_PENDING = 'PENDING';
    const STATUS_EXPIRED = 'EXPIRED';
    const STATUS_ACCEPTED = 'ACCEPTED';
    
    var $id; //long
    var $adId; //long
    var $user; //User_model
    var $requestedAt; //long - timestamp
    var $status; //enum: PENDING, EXPIRED, ACCEPTED
    var $adStatus; //enum: ACTIVE, EXPIRED, SELECTED, SENT, RECEIVED (see Ad_model)
    var $adExpiresAt; //long - timestamp
    var $type; //enum: MEMBER, BUSINESS (see Ad_model)
    var $image; //Image_model
    var $imageThumbnail; //Image_model
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing Request_model");
        
        if ( $obj != null ) {
            $this->id = getField($obj, 'id');
            $this->adId = getField($obj, 'adId');
            $this->requestedAt = getField($obj, 'requestedAt');
            $this->status = getField($obj, 'status');
            $this->adStatus = getField($obj, 'adStatus');
            $this->adExpiresAt = getField($obj, 'adExpiresAt');
            if ( hasField($obj, 'user') ) {
                $this->user = User_model::convertUser($obj->user);
            }
            if ( hasField($obj, 'image') ) {
                $this->image = Image_model::convertImage($obj->image);
            }
            if ( hasField($obj, 'imageThumbnail') ) {
                $this->imageThumbnail = Image_model::convertImage($obj->imageThumbnail);
            }
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Request";
        return parent::__get($key);
    }
    
    // helper urls
    
    public function getUserAvatarUrl() {
        if ( $this->user == null ) {
            return '';
        }
        return $this->user->getAvatarUrl();
    }
    
    public function getUserProfileUrl() {
        if ( $this->user == null ) {
            return '';
        }
        return $this->user->getProfileUrl();
    }
    
    //
    
    public function getRequestDate() {
        return date(DATE_FORMAT, $this->requestedAt / 1000);
    }
    
    public function getUserFullName() {
        if ( $this->user == null ) {
            return '';
        }
        return $this->user->getFullName();
    }
    
    // static helpers
    
    public static function convertRequests($requestsResult) {
        $requests = array();
        if ( is_array($requestsResult) && count($requestsResult) > 0 ) {
            foreach ( $requestsResult as $request ) {
                array_push($requests, Request_model::convertRequest($request));
            }
        } elseif ( !is_empty($requestsResult) ) {
            $requests = $requestsResult;
            array_push($requests, Request_model::convertRequest($requests));
        }
        return $requests;
    }
    
    public static function convertRequest($request) {
        return new Request_model($request);
    }
}

?>
