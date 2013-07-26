<?php

class UserSetting_model extends CI_Model {
    
    const NOTIFICATION_FOLLOWER_ADDED = 'FOLLOWER_ADDED';
    const NOTIFICATION_AD_COMMENTED = 'AD_COMMENTED';
    const NOTIFICATION_AD_REQUESTED = 'AD_REQUESTED';
    const NOTIFICATION_REQUEST_MESSAGED = 'REQUEST_MESSAGED';
    const NOTIFICATION_REQUEST_ACCEPTED = 'REQUEST_ACCEPTED';
    const NOTIFICATION_REQUEST_SENT = 'REQUEST_SENT';
    const NOTIFICATION_REQUEST_CANCELED = 'REQUEST_CANCELED';
    const NOTIFICATION_REQUEST_DECLINED = 'REQUEST_DECLINED';
    const NOTIFICATION_FOLLOWER_AD_CREATED = 'FOLLOWER_AD_CREATED';
    
    static $NOTIFICATIONS = array(
        UserSetting_model::NOTIFICATION_FOLLOWER_ADDED,
        UserSetting_model::NOTIFICATION_AD_COMMENTED,
        UserSetting_model::NOTIFICATION_AD_REQUESTED,
        UserSetting_model::NOTIFICATION_REQUEST_MESSAGED,
        UserSetting_model::NOTIFICATION_REQUEST_ACCEPTED,
        UserSetting_model::NOTIFICATION_REQUEST_SENT,
        UserSetting_model::NOTIFICATION_REQUEST_CANCELED,
        UserSetting_model::NOTIFICATION_REQUEST_DECLINED,
        UserSetting_model::NOTIFICATION_FOLLOWER_AD_CREATED,
    );
    
    var $userId; //long
    var $notifiableTypes; //array of enum: FOLLOWER_ADDED, AD_COMMENTED, AD_REQUESTED, REQUEST_MESSAGED, REQUEST_ACCEPTED, REQUEST_SENT, REQUEST_CANCELED, REQUEST_DECLINED, FOLLOWER_AD_CREATED
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing UserSetting_model");
        
        if ( $obj != null ) {
            $this->userId = getField($obj, 'userId');
            $this->notifiableTypes = getField($obj, 'notifiableTypes');
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "UserSetting";
        return parent::__get($key);
    }
    
    // static helpers
    
    public static function convertUserSetting($userSetting) {
        return new UserSetting_model($userSetting);
    }
}
