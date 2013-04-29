<?php

/**
 * Description of Review DTO
 *
 * @author gyuszi
 */
class Review_model extends CI_Model {
    
    var $text; //string
    var $from; //User_model
    var $to; //User_model
    var $reviewedAt; //long - timestamp
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing Review_model");
        
        if ( $obj != null ) {
            $this->text = getField($obj, 'text');
            $this->reviewedAt = getField($obj, 'reviewedAt');
            if ( hasField($obj, 'from') ) {
                $this->from = User_model::convertUser($obj->from);
            }
            if ( hasField($obj, 'to') ) {
                $this->to = User_model::convertUser($obj->to);
            }
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Review";
        return parent::__get($key);
    }
    
    public function getReviewDate() {
        return date(DATE_FORMAT, $this->reviewedAt / 1000);
    }
    
    public function getFromAvatarUrl() {
        if ( $this->from == null ) {
            return '';
        }
        return $this->from->getAvatarUrl();
    }
    
    public function getFromFullName() {
        if ( $this->from == null ) {
            return '';
        }
        return $this->from->getFullName();
    }
    
    public function getToAvatarUrl() {
        if ( $this->to == null ) {
            return '';
        }
        return $this->to->getAvatarUrl();
    }
    
    public function getToFullName() {
        if ( $this->to == null ) {
            return '';
        }
        return $this->to->getFullName();
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
