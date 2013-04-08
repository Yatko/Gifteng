<?php

/**
 * Description of User DTO
 *
 * @author gyuszi
 */
class User_model extends CI_Model {
    
    var $name; //string
    var $firstName; //string
    var $lastName; //string
    var $email; //string
    var $phoneNumber; //string
    var $dateOfBirth; //long - timestamp
    var $country; //string
    var $city; //string
    var $area; //string
    var $zipCode; //string
    var $avatar; //Image_model
    var $joinedAt; //long - timestamp

    public function __construct($obj = null) {
        // Call the Model constructor
        parent::__construct();
        
        if ( $obj != null ) {
            $this->name = $obj->name;
            $this->firstName = $obj->firstName;
            $this->lastName = $obj->lastName;
            $this->email = $obj->email;
            $this->phoneNumber = getField($obj, 'phoneNumber');
            $this->dateOfBirth = $obj->dateOfBirth;
            $this->country = $obj->country;
            $this->city = $obj->city;
            $this->area = $obj->area;
            $this->zipCode = $obj->zipCode;
            $this->avatar = new Image_model($obj->avatar);
            $this->joinedAt = $obj->joinedAt;
        }
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