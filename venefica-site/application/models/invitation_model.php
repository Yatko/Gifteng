<?php

/**
 * Description of Invitation DTO
 *
 * @author gyuszi
 */
class Invitation_model extends CI_Model {
    
    var $email; //string
    var $zipCode; //string
    var $source; //string
    var $otherSource; //string
    var $userType; //string: GIVER, RECEIVER
    
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
            ."zipCode=".$this->zipCode.", "
            ."source=".$this->source.", "
            ."otherSource=".$this->otherSource.", "
            ."userType=".$this->userType.""
            ."]";
    }
}

?>
