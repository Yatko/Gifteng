<?php

/**
 * Description of Request DTO
 */
class Request_model extends CI_Model {
    
    var $id; //long
    var $adId; //long
    var $user; //User_model
    var $requestedAt; //long - timestamp
    var $status; //string: PENDING, EXPIRED, ACCEPTED
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing Request_model");
        
        if ( $obj != null ) {
            $this->id = getField($obj, 'id');
            $this->adId = getField($obj, 'adId');
            $this->requestedAt = getField($obj, 'requestedAt');
            $this->status = getField($obj, 'status');
            if ( hasField($obj, 'user') ) {
                $this->user = User_model::convertUser($obj->user);
            }
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Request";
        return parent::__get($key);
    }
    
    public function getRequestDate() {
        return date(DATE_FORMAT, $this->requestedAt / 1000);
    }
    
    public function getUserAvatarUrl() {
        if ( $this->user == null ) {
            return '';
        }
        return $this->user->getAvatarUrl();
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
        } else {
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
