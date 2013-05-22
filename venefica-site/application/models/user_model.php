<?php

/**
 * Description of User DTO
 *
 * @author gyuszi
 */
class User_model extends CI_Model {
    
    var $id; //string
    var $name; //string
    var $firstName; //string
    var $lastName; //string
    var $email; //string
    var $phoneNumber; //string
    var $dateOfBirth; //long - timestamp
    var $avatar; //Image_model
    var $joinedAt; //long - timestamp
    var $inFollowers; //boolean
    var $inFollowings; //boolean
    var $gender; //MALE, FEMALE
    var $address; //Address_model
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing User_model");
        
        if ( $obj != null ) {
            $this->id = getField($obj, 'id');
            $this->name = getField($obj, 'name');
            $this->firstName = getField($obj, 'firstName');
            $this->lastName = getField($obj, 'lastName');
            $this->email = getField($obj, 'email');
            $this->phoneNumber = getField($obj, 'phoneNumber');
            $this->dateOfBirth = getField($obj, 'dateOfBirth');
            $this->joinedAt = getField($obj, 'joinedAt');
            $this->inFollowers = getField($obj, 'inFollowers');
            $this->inFollowings = getField($obj, 'inFollowings');
            $this->gender = getField($obj, 'gender');
            if ( hasField($obj, 'avatar') ) {
                $this->avatar = Image_model::convertImage($obj->avatar);
            }
            if ( hasField($obj, 'address') ) {
                $this->address = Address_model::convertAddress($obj->address);
            }
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
        if ( $this->joinedAt == null ) {
            return '';
        }
        return date(DATE_FORMAT, $this->joinedAt / 1000);
    }
    
    public function getLocation() {
        if ( $this->address == null ) {
            return '';
        }
        return $this->address->getLocation();
    }
    
    public function toString() {
        return "User ["
            ."name=".$this->name.", "
            ."firstName=".$this->firstName.", "
            ."lastName=".$this->lastName.", "
            ."email=".$this->email.", "
            ."phoneNumber=".$this->phoneNumber.", "
            ."dateOfBirth=".$this->dateOfBirth.", "
            ."avatar=".$this->avatar.", "
            ."joinedAt=".$this->joinedAt.", "
            ."inFollowers=".$this->inFollowers.", "
            ."inFollowings=".$this->inFollowings.""
            ."]";
    }
    
    // static helpers
    
    public static function convertUsers($usersResult) {
        $users = array();
        if ( is_array($usersResult) && count($usersResult) > 0 ) {
            foreach ( $usersResult as $user ) {
                array_push($users, User_model::convertUser($user));
            }
        } else {
            $user = $usersResult;
            array_push($users, User_model::convertUser($user));
        }
        return $users;
    }
    
    public static function convertUser($user) {
        return new User_model($user);
    }
}