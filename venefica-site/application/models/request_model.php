<?php

/**
 * Description of Request DTO
 */
class Request_model extends CI_Model {
    
    const STATUS_PENDING = 'PENDING'; //REQUESTED - giver didn't make a decision (the starter status of a request)
    const STATUS_ACCEPTED = 'ACCEPTED'; //SELECTED - the receiver/owner acceped the request
    const STATUS_UNACCEPTED = 'UNACCEPTED'; //someone else selected by the owner and the available quantity is 0 (auto status)
    const STATUS_CANCELED = 'CANCELED'; //the receiver/requestor cancelled the request (it's own request)
    const STATUS_DECLINED = 'DECLINED'; //REJECTED - the giver/owner declined the request
    const STATUS_SENT = 'SENT'; //the giver/owner clicked on 'Mark as shipped'
    const STATUS_RECEIVED = 'RECEIVED'; //the receiver/requestor selected 'Received'
    
    var $id; //long
    var $adId; //long
    var $user; //User_model
    var $requestedAt; //long - timestamp
    var $status; //enum: PENDING, ACCEPTED, UNACCEPTED, CANCELED, DECLINED, SENT, RECEIVED
    var $adStatus; //enum: ACTIVE, IN_PROGRESS, FINALIZED, EXPIRED (see Ad_model)
    var $adExpiresAt; //long - timestamp
    var $type; //enum: MEMBER, BUSINESS (see Ad_model)
    var $image; //Image_model
    var $imageThumbnail; //Image_model
    var $accepted; //boolean
    var $sent; //boolean
    var $received; //boolean
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing Request_model");
        
        if ( $obj != null ) {
            $this->id = getField($obj, 'id');
            $this->adId = getField($obj, 'adId');
            $this->requestedAt = getField($obj, 'requestedAt');
            $this->status = getField($obj, 'status');
            $this->adStatus = getField($obj, 'adStatus');
            $this->adExpiresAt = getField($obj, 'adExpiresAt');
            $this->accepted = getField($obj, 'accepted');
            $this->sent = getField($obj, 'sent');
            $this->received = getField($obj, 'received');
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
            return DEFAULT_USER_URL;
        }
        return $this->user->getAvatarUrl(LIST_USER_IMAGE_SIZE);
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
            return '&nbsp;';
        }
        return $this->user->getFullName();
    }
    
    public function isActive() {
        switch ( $this->status ) {
            case Request_model::STATUS_PENDING:
            case Request_model::STATUS_ACCEPTED:
            case Request_model::STATUS_UNACCEPTED:
            case Request_model::STATUS_SENT:
            case Request_model::STATUS_RECEIVED:
                return true;
        }
        return false;
    }
    
    public function isDeclined() {
        if ( $this->status == Request_model::STATUS_DECLINED ) {
            return true;
        }
        return false;
    }
    
    public function isCanceled() {
        if ( $this->status == Request_model::STATUS_CANCELED ) {
            return true;
        }
        return false;
    }
    
    public function isPending() {
        if ( $this->status == Request_model::STATUS_PENDING ) {
            return true;
        }
        return false;
    }
    
    public function isExpired() {
        if (
            $this->status == Request_model::STATUS_UNACCEPTED ||
            $this->status == Request_model::STATUS_DECLINED ||
            $this->status == Request_model::STATUS_CANCELED
        ) {
            return true;
        }
        return false;
    }
    
    // static helpers
    
    public static function convertRequests($requestsResult) {
        $requests = array();
        if ( is_array($requestsResult) && count($requestsResult) > 0 ) {
            foreach ( $requestsResult as $request ) {
                array_push($requests, Request_model::convertRequest($request));
            }
        } elseif ( !is_empty($requestsResult) ) {
            $request = $requestsResult;
            array_push($requests, Request_model::convertRequest($request));
        }
        return $requests;
    }
    
    public static function convertRequest($request) {
        return new Request_model($request);
    }
}

?>
