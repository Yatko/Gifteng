<?php

/**
 * Description of Review DTO
 *
 * @author gyuszi
 */
class Review_model extends CI_Model {
    
    var $adId; //long
    var $positive; //boolean
    var $text; //string
    var $reviewedAt; //long - timestamp
    var $reviewedUser; //User_model
    var $reviewerUser; //User_model
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing Review_model");
        
        if ( $obj != null ) {
            $this->adId = getField($obj, 'adId');
            $this->positive = getField($obj, 'positive');
            $this->text = getField($obj, 'text');
            $this->reviewedAt = getField($obj, 'reviewedAt');
            if ( hasField($obj, 'reviewedUser') ) {
                $this->reviewedUser = User_model::convertUser($obj->reviewedUser);
            }
            if ( hasField($obj, 'reviewerUser') ) {
                $this->reviewerUser = User_model::convertUser($obj->reviewerUser);
            }
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Review";
        return parent::__get($key);
    }
    
    public function getReviewDate() {
        if ( $this->reviewedAt == null ) {
            return '';
        }
        return date(DATE_FORMAT, $this->reviewedAt / 1000);
    }
    
    public function getFromAvatarUrl() {
        if ( $this->reviewerUser == null ) {
            return '';
        }
        return $this->reviewerUser->getAvatarUrl();
    }
    
    public function getFromFullName() {
        if ( $this->reviewerUser == null ) {
            return '';
        }
        return $this->reviewerUser->getFullName();
    }
    
    public function getToAvatarUrl() {
        if ( $this->reviewedUser == null ) {
            return '';
        }
        return $this->reviewedUser->getAvatarUrl();
    }
    
    public function getToFullName() {
        if ( $this->reviewedUser == null ) {
            return '';
        }
        return $this->reviewedUser->getFullName();
    }
    
    // static helpers
    
    public static function convertReviews($reviewsResult) {
        $reviews = array();
        if ( is_array($reviewsResult) && count($reviewsResult) > 0 ) {
            foreach ( $reviewsResult as $review ) {
                array_push($reviews, Review_model::convertReview($review));
            }
        } else {
            $review = $reviewsResult;
            array_push($reviews, Review_model::convertReview($review));
        }
        return $reviews;
    }
    
    public static function convertReview($review) {
        return new Review_model($review);
    }
}

?>
