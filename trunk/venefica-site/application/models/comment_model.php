<?php

/**
 * Description of Comment DTO
 *
 * @author gyuszi
 */
class Comment_model extends CI_Model {
    
    var $id; //long
    var $text; //string
    var $owner; //boolean
    var $publisherName; //string
    var $publisherFullName; //string
    var $publisherAvatarUrl; //string
    var $createdAt; //long - timestamp
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing Comment_model");
        
        if ( $obj != null ) {
            $this->id = getField($obj, 'id');
            $this->text = getField($obj, 'text');
            $this->owner = getField($obj, 'owner');
            $this->publisherName = getField($obj, 'publisherName');
            $this->publisherFullName = getField($obj, 'publisherFullName');
            $this->publisherAvatarUrl = getField($obj, 'publisherAvatarUrl');
            $this->createdAt = getField($obj, 'createdAt');
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Comment";
        return parent::__get($key);
    }
    
    public function getPublisherAvatarUrl() {
        if ( $this->publisherAvatarUrl == null ) {
            return '';
        }
        return SERVER_URL.$this->publisherAvatarUrl;
    }
}
