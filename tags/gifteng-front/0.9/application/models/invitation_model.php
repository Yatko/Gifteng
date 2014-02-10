<?php

/**
 * Description of Invitation DTO
 *
 * @author gyuszi
 */
class Invitation_model extends CI_Model {
    
    const USERTYPE_GIVER = 'GIVER';
    const USERTYPE_RECEIVER = 'RECEIVER';
    
    var $email; //string
    var $ipAddress; //string
    var $country; //string
    var $zipCode; //string
    var $source; //string
    var $otherSource; //string
    var $userType; //enum: GIVER, RECEIVER
    
    public function __construct() {
        log_message(DEBUG, "Initializing Invitation_model");
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Invitation";
        return parent::__get($key);
    }
    
    public function toString() {
        return "Invitation ["
            ."email=".$this->email.", "
            ."ipAddress=".$this->ipAddress.", "
            ."country=".$this->country.", "
            ."zipCode=".$this->zipCode.", "
            ."source=".$this->source.", "
            ."otherSource=".$this->otherSource.", "
            ."userType=".$this->userType.""
            ."]";
    }
}

?>
