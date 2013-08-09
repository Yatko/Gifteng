<?php

/**
 * Description of Approval DTO
 *
 * @author gyuszi
 */
class Approval_model extends CI_Model {
    
    var $id; //long
    var $adId; //long
    var $deciderId; //long
    var $deciderFullName; //string
    var $approvedAt; //long - timestamp
    var $text; //string
    
    public function __construct($obj = null) {
        log_message(DEBUG, "Initializing Approval_model");
        
        if ( $obj != null ) {
            $this->id = getField($obj, 'id');
            $this->adId = getField($obj, 'adId');
            $this->deciderId = getField($obj, 'deciderId');
            $this->deciderFullName = getField($obj, 'deciderFullName');
            $this->approvedAt = getField($obj, 'approvedAt');
            $this->text = getField($obj, 'text');
        }
    }
    
    public function __get($key) {
        //the following is queried by the SoapClient
        if ( $key == "type" ) return "Approval";
        return parent::__get($key);
    }
    
    public function getApprovedDateHumanTiming() {
        if ( $this->approvedAt == null ) {
            return '';
        }
        return convertTimestampToDateForMessage($this->approvedAt / 1000);
    }
    
    // static helpers
    
    public static function convertApprovals($approvalsResult) {
        $approvals = array();
        if ( is_array($approvalsResult) && count($approvalsResult) > 0 ) {
            foreach ( $approvalsResult as $approval ) {
                array_push($approvals, Approval_model::convertApproval($approval));
            }
        } elseif ( !is_empty($approvalsResult) ) {
            $approval = $approvalsResult;
            array_push($approvals, Approval_model::convertApproval($approval));
        }
        return $approvals;
    }
    
    public static function convertApproval($approval) {
        return new Approval_model($approval);
    }
}
