<?php

/**
 * Description of Comment DTO
 *
 * @author gyuszi
 */
class Comment_model extends CI_Model {
    
    var $id; //long
    var $text; //string
    var $owner; //boolean
    var $publisherId; //long
    var $publisherName; //string
    var $publisherFullName; //string
    var $publisherAvatarUrl; //string
    var $createdAt; //long - timestamp
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing Comment_model");
        
        if ( $obj != null ) {
            $this->id = getField($obj, 'id');
            $this->text = getField($obj, 'text');
            $this->owner = getField($obj, 'owner');
            $this->publisherId = getField($obj, 'publisherId');
            $this->publisherName = getField($obj, 'publisherName');
            $this->publisherFullName = getField($obj, 'publisherFullName');
            $this->publisherAvatarUrl = getField($obj, 'publisherAvatarUrl');
            $this->createdAt = getField($obj, 'createdAt');
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Comment";
        return parent::__get($key);
    }
    
    // helper urls
    
    public function getPublisherProfileUrl() {
        $name = $this->publisherName;
        if ( $name == null || is_empty($name) ) $name = $this->publisherId;
        return base_url() . 'profile/' . $name;
    }
    
    public function getPublisherAvatarUrl() {
        $url = $this->publisherAvatarUrl;
        if ( $url == null || trim($url) == '' ) {
            return DEFAULT_USER_URL;
        }
        return get_image_url($url);
    }
    
    //
    
//    public function getCreateDate() {
//        if ( $this->createdAt == null ) {
//            return '';
//        }
//        return date(DATE_FORMAT, $this->createdAt / 1000);
//    }
    
    public function getCreateDateHumanTiming() {
        if ( $this->createdAt == null ) {
            return '';
        }
        //return humanTiming($this->createdAt / 1000) . ' ago';
        return convertTimestampToDateForComment($this->createdAt / 1000) . ' ago';
    }
    
    public function getSafeText() {
        return safe_content($this->text);
    }
    
    // static helpers
    
    public static function convertComments($commentsResult) {
        $comments = array();
        if ( is_array($commentsResult) && count($commentsResult) > 0 ) {
            foreach ( $commentsResult as $comment ) {
                array_push($comments, Comment_model::convertComment($comment));
            }
        } elseif ( !is_empty($commentsResult) ) {
            $comment = $commentsResult;
            array_push($comments, Comment_model::convertComment($comment));
        }
        return $comments;
    }
    
    public static function convertComment($comment) {
        return new Comment_model($comment);
    }
}
