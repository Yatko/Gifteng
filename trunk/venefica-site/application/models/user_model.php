<?php

/**
 * Description of User DTO
 *
 * @author gyuszi
 */
class User_model extends CI_Model {
    
    const GENDER_MALE = 'MALE';
    const GENDER_FEMALE = 'FEMALE';
    
    var $id; //string
    var $name; //string
    var $firstName; //string
    var $lastName; //string
    var $email; //string
    var $phoneNumber; //string
    var $dateOfBirth; //long - timestamp
    var $about; //string
    var $avatar; //Image_model
    var $joinedAt; //long - timestamp
    var $inFollowers; //boolean
    var $inFollowings; //boolean
    var $gender; //enum: MALE, FEMALE
    var $address; //Address_model
    var $businessAccount; //boolean
    var $score; //float
    var $pendingScore; //float
    var $statistics; //UserStatistics_model
    var $verified; //boolean
    
    // business user data
    var $businessName; //string
    var $contactName; //string
    var $businessCategoryId; //long
    var $businessCategory; //string
    var $addresses; //array of Address_model
    
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
            $this->about = getField($obj, 'about');
            $this->joinedAt = getField($obj, 'joinedAt');
            $this->inFollowers = getField($obj, 'inFollowers');
            $this->inFollowings = getField($obj, 'inFollowings');
            $this->gender = getField($obj, 'gender');
            $this->businessAccount = getField($obj, 'businessAccount');
            $this->score = getField($obj, 'score');
            $this->pendingScore = getField($obj, 'pendingScore');
            $this->verified = getField($obj, 'verified');
            $this->businessName= getField($obj, 'businessName');
            $this->contactName = getField($obj, 'contactName');
            $this->businessCategoryId = getField($obj, 'businessCategoryId');
            $this->businessCategory = getField($obj, 'businessCategory');
            if ( hasField($obj, 'statistics') ) {
                $this->statistics = UserStatistics_model::convertUserStatistics($obj->statistics);
            }
            if ( hasField($obj, 'avatar') ) {
                $this->avatar = Image_model::convertImage($obj->avatar);
            }
            if ( hasField($obj, 'address') ) {
                $this->address = Address_model::convertAddress($obj->address);
            }
            if ( hasField($obj, 'addresses') ) {
                $this->addresses = Address_model::convertAddresses($obj->addresses);
            }
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "User";
        return parent::__get($key);
    }
    
    // helper urls
    
    public function getProfileUrl() {
        $name = $this->name;
        if ( $name == null || is_empty($name) ) $name = $this->id;
        return base_url() . 'profile/' . $name;
    }
    
    public function getAvatarUrl() {
        $url = '';
        if ( $this->avatar != null ) {
            $url = SERVER_URL.$this->avatar->url;
        }
        if ( trim($url) == '' ) {
            $url = DEFAULT_USER_URL;
        }
        return $url;
    }

    //
    
    public function getAddressById($id) {
        if ( $this->addresses && sizeof($this->addresses) > 0 ) {
            foreach ( $this->addresses as $address ) {
                if ( $address->id == $id ) {
                    return $address;
                }
            }
        }
        return null;
    }
    
    public function getLastAddress() {
        if ( $this->addresses && sizeof($this->addresses) > 0 ) {
            $lastAddress = null;
            foreach ( $this->addresses as $address ) {
                if ( $lastAddress == null ) {
                    $lastAddress = $address;
                    continue;
                }
                if (
                    $lastAddress->id != null &&
                    $address->id != null &&
                    $lastAddress->id < $address->id
                ) {
                    $lastAddress = $address;
                }
            }
            return $lastAddress;
        }
        return null;
    }
    
    public function addAddress(Address_model $address) {
        if ( $this->addresses == null || sizeof($this->addresses) == 0 ) {
            $this->addresses = array();
        }
        array_push($this->addresses, $address);
    }
    
    public function getFullName() {
        $name = trim($this->firstName) . ' ' . trim($this->lastName);
        if ( trim($name) == '' ) {
            $name = '&nbsp;';
        }
        return $name;
    }
    
//    public function getJoinDate() {
//        if ( $this->joinedAt == null ) {
//            return '';
//        }
//        return date(DATE_FORMAT, $this->joinedAt / 1000);
//    }
    
    public function getJoinDateHumanTiming() {
        if ( $this->joinedAt == null ) {
            return '';
        }
        //return humanTiming($this->joinedAt / 1000);
        return convertTimestampToDateForProfile($this->joinedAt / 1000);
    }
    
    public function getLocation() {
        if ( $this->address == null ) {
            return '';
        }
        return $this->address->getLocation();
    }
    
    public function getZipCode() {
        if ( $this->address == null ) {
            return '';
        }
        return $this->address->zipCode;
    }
    
    public function getPoints($includePendingScore = true) {
        $score = round($this->score != null && is_numeric($this->score) ? $this->score : 0);
        if ( $includePendingScore ) {
            $pendingScore = round($this->pendingScore != null && is_numeric($this->pendingScore) ? $this->pendingScore : 0);
        } else {
            $pendingScore = 0;
        }
        return $score . ($pendingScore > 0 ? '/'.$pendingScore : '');
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
        } elseif ( !is_empty($usersResult) ) {
            $user = $usersResult;
            array_push($users, User_model::convertUser($user));
        }
        return $users;
    }
    
    public static function convertUser($user) {
        return new User_model($user);
    }
}