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
    
    public function getPublisherAvatarUrl() {
        if ( $this->publisherAvatarUrl == null ) {
            return '';
        }
        return SERVER_URL.$this->publisherAvatarUrl;
    }
    
    public function getCreateDate() {
        if ( $this->createdAt == null ) {
            return '';
        }
        return date(DATE_FORMAT, $this->createdAt / 1000);
    }
    
    // static helpers
    
    public static function convertComments($commentsResult) {
        $comments = array();
        if ( is_array($commentsResult) && count($commentsResult) > 0 ) {
            foreach ( $commentsResult as $comment ) {
                array_push($comments, Comment_model::convertComment($comment));
            }
        } else {
            $comment = $commentsResult;
            array_push($comments, Comment_model::convertComment($comment));
        }
        return $comments;
    }
    
    public static function convertComment($comment) {
        return new Comment_model($comment);
    }
}
