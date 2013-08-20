<?php

/**
 * Description of Rating DTO
 *
 * @author gyuszi
 */
class Rating_model extends CI_Model {
    
    var $adId; //long
    var $text; //string
    var $value; //int
    var $toUserId; //long
    var $ratedAt; //long - timestamp
    var $fromUser; //User_model
    var $toUser; //User_model
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing Rating_model");
        
        if ( $obj != null ) {
            $this->adId = getField($obj, 'adId');
            $this->text = getField($obj, 'text');
            $this->value = getField($obj, 'value');
            $this->toUserId = getField($obj, 'toUserId');
            $this->ratedAt = getField($obj, 'ratedAt');
            if ( hasField($obj, 'fromUser') ) {
                $this->fromUser = User_model::convertUser($obj->fromUser);
            }
            if ( hasField($obj, 'toUser') ) {
                $this->toUser = User_model::convertUser($obj->toUser);
            }
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Rating";
        return parent::__get($key);
    }
    
    // helper urls
    
    public function getFromAvatarUrl() {
        if ( $this->fromUser == null ) {
            return DEFAULT_USER_URL;
        }
        return $this->fromUser->getAvatarUrl();
    }
    
    public function getFromProfileUrl() {
        if ( $this->fromUser == null ) {
            return '';
        }
        return $this->fromUser->getProfileUrl();
    }
    
    public function getToAvatarUrl() {
        if ( $this->toUser == null ) {
            return DEFAULT_USER_URL;
        }
        return $this->toUser->getAvatarUrl();
    }
    
    public function getToProfileUrl() {
        if ( $this->toUser == null ) {
            return '';
        }
        return $this->toUser->getProfileUrl();
    }
    
    //
    
    public function getRateDate() {
        if ( $this->ratedAt == null ) {
            return '';
        }
        return date(DATE_FORMAT, $this->ratedAt / 1000);
    }
    
    public function getFromFullName() {
        if ( $this->fromUser == null ) {
            return '&nbsp;';
        }
        return $this->fromUser->getFullName();
    }
    
    public function getToFullName() {
        if ( $this->toUser == null ) {
            return '&nbsp;';
        }
        return $this->toUser->getFullName();
    }
    
    // static helpers
    
    public static function convertRatings($ratingsResult) {
        $ratings = array();
        if ( is_array($ratingsResult) && count($ratingsResult) > 0 ) {
            foreach ( $ratingsResult as $rating ) {
                array_push($ratings, Rating_model::convertRating($rating));
            }
        } elseif ( !is_empty($ratingsResult) ) {
            $rating = $ratingsResult;
            array_push($ratings, Rating_model::convertRating($rating));
        }
        return $ratings;
    }
    
    public static function convertRating($rating) {
        return new Rating_model($rating);
    }
}

?>
