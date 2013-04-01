<?php

/**
 * Description of Invitation
 *
 * @author gyuszi
 */
class Invitation_model extends CI_Model {
    
    var $email;
    var $zipCode;
    var $source;
    var $userType;
    
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
