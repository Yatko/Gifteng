<?php

class Invitation {
    
    const USERTYPE_GIVER = 'GIVER';
    const USERTYPE_RECEIVER = 'RECEIVER';
    
    public $email; //string
    public $ipAddress; //string
    public $country; //string
    public $zipCode; //string
    public $source; //string
    public $otherSource; //string
    public $userType; //enum: GIVER, RECEIVER
    
    public function __construct() {
    }
	
}

?>
