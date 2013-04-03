<?php

/**
 * Description of User DTO
 *
 * @author gyuszi
 */
class User_model extends CI_Model {
    
    var $name;
    var $firstName;
    var $lastName;
    var $email;
    var $phoneNumber;
    var $dateOfBirth;
    var $country;
    var $city;
    var $area;
    var $zipCode;
    var $avatar;
    var $joinedAt;

    public function __construct() {
        // Call the Model constructor
        parent::__construct();
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "User";
        return parent::__get($key);
    }
    
    public function toString() {
        return "User ["
            ."name=".$this->name.", "
            ."firstName=".$this->firstName.", "
            ."lastName=".$this->lastName.", "
            ."email=".$this->email.", "
            ."phoneNumber=".$this->phoneNumber.", "
            ."dateOfBirth=".$this->dateOfBirth.", "
            ."country=".$this->country.", "
            ."city=".$this->city.", "
            ."area=".$this->area.", "
            ."zipCode=".$this->zipCode.", "
            ."avatar=".$this->avatar.", "
            ."joinedAt=".$this->joinedAt.""
            ."]";
    }
}