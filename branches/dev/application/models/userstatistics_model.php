<?php

/**
 * Description of User statistics DTO
 *
 * @author gyuszi
 */
class UserStatistics_model extends CI_Model {
    
    var $numReceivings = 0; //int
    var $numGivings = 0; //int
    var $numBookmarks = 0; //int
    var $numFollowers = 0; //int
    var $numFollowings = 0; //int
    var $numRatings = 0; //int
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing UserStatistics_model");
        
        if ( $obj != null ) {
            $this->numReceivings = getField($obj, 'numReceivings');
            $this->numGivings = getField($obj, 'numGivings');
            $this->numBookmarks = getField($obj, 'numBookmarks');
            $this->numFollowers = getField($obj, 'numFollowers');
            $this->numFollowings = getField($obj, 'numFollowings');
            $this->numRatings = getField($obj, 'numRatings');
        }
    }
    
    // static helpers
    
    public static function convertUserStatistics($statistics) {
        return new UserStatistics_model($statistics);
    }
}
