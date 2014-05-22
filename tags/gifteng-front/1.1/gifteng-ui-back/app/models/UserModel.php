<?php

class UserModel {
    
    const GENDER_MALE = 'MALE';
    const GENDER_FEMALE = 'FEMALE';
    
    public $id; //string
    public $name; //string
    public $firstName; //string
    public $lastName; //string
    public $email; //string
    public $phoneNumber; //string
    public $dateOfBirth; //long - timestamp
    public $about; //string
    public $avatar; //Image_model
    public $joinedAt; //long - timestamp
    public $inFollowers; //boolean
    public $inFollowings; //boolean
    public $gender; //enum: MALE, FEMALE
    public $address; //Address_model
    public $businessAccount; //boolean
    public $score; //float
    public $pendingScore; //float
    public $statistics; //UserStatistics_model
    public $verified; //boolean
    
    // business user data
    public $businessName; //string
    public $contactName; //string
    public $businessCategoryId; //long
    public $businessCategory; //string
    public $addresses; //array of Address_model
    
    public function __construct($obj = null) {
        
        if ( $obj != null ) {
        	foreach(get_class_vars(__CLASS__) as $k=>$v) {
        		if(isset($obj->$k))
					$this->$k = $obj->$k;
				elseif(is_array($obj) && isset($obj[$k]))
					$this->$k = $obj[$k];
        	}
        }
    }
}