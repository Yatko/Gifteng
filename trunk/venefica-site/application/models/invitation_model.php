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
    var $userType; //string: GIVER, RECEIVER
    
    public function __construct() {
        // Call the Model constructor
        parent::__construct();
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
            ."userType=".$this->userType.""
            ."]";
    }
}

?>
