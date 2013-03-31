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
