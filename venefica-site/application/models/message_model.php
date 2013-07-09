<?php

/**
 * Description of Message DTO
 */
class Message_model extends CI_Model {
    
    var $id; //long
    var $adId; //long
    var $text; //string
    var $owner; //boolean
    var $toId; //long
    var $toName; //string
    var $toFullName; //string
    var $toAvatarUrl; //string
    var $fromId; //long
    var $fromName; //string
    var $fromFullName; //string
    var $fromAvatarUrl; //string
    var $read; //boolean
    var $createdAt; //long - timestamp
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing Message_model");
        
        if ( $obj != null ) {
            $this->id = getField($obj, 'id');
            $this->adId = getField($obj, 'adId');
            $this->text = getField($obj, 'text');
            $this->owner = getField($obj, 'owner');
            $this->toName = getField($obj, 'toName');
            $this->toId = getField($obj, 'toId');
            $this->toFullName = getField($obj, 'toFullName');
            $this->toAvatarUrl = getField($obj, 'toAvatarUrl');
            $this->fromId = getField($obj, 'fromId');
            $this->fromName = getField($obj, 'fromName');
            $this->fromFullName = getField($obj, 'fromFullName');
            $this->fromAvatarUrl = getField($obj, 'fromAvatarUrl');
            $this->read = getField($obj, 'read');
            $this->createdAt = getField($obj, 'createdAt');
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Message";
        return parent::__get($key);
    }
    
    // helper urls
    
    public function getToAvatarUrl() {
        $url = $this->toAvatarUrl;
        if ( $url == null || trim($url) == '' ) {
            return DEFAULT_USER_URL;
        }
        return SERVER_URL.$url;
    }
    
    public function getFromAvatarUrl() {
        $url = $this->fromAvatarUrl;
        if ( $url == null || trim($url) == '' ) {
            return DEFAULT_USER_URL;
        }
        return SERVER_URL.$url;
    }
    
    public function getToProfileUrl() {
        $name = $this->toName;
        if ( $name == null || is_empty($name) ) $name = $this->toId;
        return base_url() . 'profile/' . $name;
    }
    
    public function getFromProfileUrl() {
        $name = $this->fromName;
        if ( $name == null || is_empty($name) ) $name = $this->fromId;
        return base_url() . 'profile/' . $name;
    }
    
    //
    
    public function getCreateDate() {
        if ( $this->createdAt == null ) {
            return '';
        }
        return date(DATE_FORMAT, $this->createdAt / 1000);
    }
    
    public function getCreateDateHumanTiming() {
        if ( $this->createdAt == null ) {
            return '';
        }
        return humanTiming($this->createdAt / 1000);
    }
    
    // static helpers
    
    public static function convertMessages($messagesResult) {
        $messages = array();
        if ( is_array($messagesResult) && count($messagesResult) > 0 ) {
            foreach ( $messagesResult as $message ) {
                array_push($messages, Message_model::convertMessage($message));
            }
        } elseif ( !is_empty($messagesResult) ) {
            $message = $messagesResult;
            array_push($messages, Message_model::convertMessage($message));
        }
        return $messages;
    }
    
    public static function convertMessage($message) {
        return new Message_model($message);
    }
}

?>
