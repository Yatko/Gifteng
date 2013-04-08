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
        // Call the Model constructor
        parent::__construct();
        
        if ( $obj != null ) {
            $this->id = $obj->id;
            $this->text = $obj->text;
            $this->owner = $obj->owner;
            $this->publisherName = $obj->publisherName;
            $this->publisherFullName = $obj->publisherFullName;
            $this->publisherAvatarUrl = $obj->publisherAvatarUrl;
            $this->createdAt = $obj->createdAt;
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Comment";
        return parent::__get($key);
    }
}
