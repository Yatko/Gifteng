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
        log_message(DEBUG, "Initializing User_model");
        
        if ( $obj != null ) {
            $this->name = getField($obj, 'name');
            $this->firstName = getField($obj, 'firstName');
            $this->lastName = getField($obj, 'lastName');
            $this->email = getField($obj, 'email');
            $this->phoneNumber = getField($obj, 'phoneNumber');
            $this->dateOfBirth = getField($obj, 'dateOfBirth');
            $this->country = getField($obj, 'country');
            $this->city = getField($obj, 'city');
            $this->area = getField($obj, 'area');
            $this->zipCode = getField($obj, 'zipCode');
            $this->avatar = hasField($obj, 'avatar') ? new Image_model($obj->avatar) : null;
            $this->joinedAt = getField($obj, 'joinedAt');
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "User";
        return parent::__get($key);
    }
    
    public function getAvatarUrl() {
        if ( $this->avatar == null ) {
            return '';
        }
        return SERVER_URL.$this->avatar->url;
    }
    
    public function getFullName() {
        return $this->firstName.' '.$this->lastName;
    }
    
    public function getJoinDate() {
        return date('d-m-y', $this->joinedAt / 1000);
    }
    
    public function getLocation() {
        $separator = ', ';
        $ret = $this->city.$separator.$this->country;
        if ( trim($ret) == trim($separator) ) {
            $ret = '';
        }
        return $ret;
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