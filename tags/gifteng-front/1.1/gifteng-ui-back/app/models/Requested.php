<?php

class Requested {
    
    const STATUS_PENDING = 'PENDING'; //REQUESTED - giver didn't make a decision (the starter status of a request)
    const STATUS_ACCEPTED = 'ACCEPTED'; //SELECTED - the receiver/owner acceped the request
    const STATUS_UNACCEPTED = 'UNACCEPTED'; //someone else selected by the owner and the available quantity is 0 (auto status)
    const STATUS_CANCELED = 'CANCELED'; //the receiver/requestor cancelled the request (it's own request)
    const STATUS_DECLINED = 'DECLINED'; //REJECTED - the giver/owner declined the request
    const STATUS_SENT = 'SENT'; //the giver/owner clicked on 'Mark as shipped'
    const STATUS_RECEIVED = 'RECEIVED'; //the receiver/requestor selected 'Received'
    
    public $id; //long
    public $adId; //long
    public $user; //User_model
    public $requestedAt; //long - timestamp
    public $status; //enum: PENDING, ACCEPTED, UNACCEPTED, CANCELED, DECLINED, SENT, RECEIVED
    public $adStatus; //enum: ACTIVE, IN_PROGRESS, FINALIZED, EXPIRED (see Ad_model)
    public $adExpiresAt; //long - timestamp
    public $type; //enum: MEMBER, BUSINESS (see Ad_model)
    public $accepted; //boolean
    public $sent; //boolean
    public $received; //boolean
    public $isActive;
    public $isDeclined;
    public $isCanceled;
    public $isPending;
    public $isExpired;
	public $numUnreadMessages; // integer
    public $redeemed;
    public $promoCode;
    
    public function __construct($obj = null) {
        if ( $obj != null ) {
        	foreach(get_class_vars(__CLASS__) as $k=>$v) {
        		if(isset($obj->$k))
					$this->$k = $obj->$k;
				elseif(is_array($obj) && isset($obj[$k]))
					$this->$k = $obj[$k];
        	}
        }
		
		$this->isActive = $this->isActive();
		$this->isDeclined = $this->isDeclined();
		$this->isCanceled = $this->isCanceled();
		$this->isPending = $this->isPending();
		$this->isExpired = $this->isExpired();
    }

    public function isActive() {
        switch ( $this->status ) {
            case Requested::STATUS_PENDING:
            case Requested::STATUS_ACCEPTED:
            case Requested::STATUS_UNACCEPTED:
            case Requested::STATUS_SENT:
            case Requested::STATUS_RECEIVED:
                return true;
        }
        return false;
    }
    
    public function isDeclined() {
        if ( $this->status == Requested::STATUS_DECLINED ) {
            return true;
        }
        return false;
    }
    
    public function isCanceled() {
        if ( $this->status == Requested::STATUS_CANCELED ) {
            return true;
        }
        return false;
    }
    
    public function isPending() {
        if ( $this->status == Requested::STATUS_PENDING ) {
            return true;
        }
        return false;
    }
    
    public function isExpired() {
        if (
            $this->status == Requested::STATUS_UNACCEPTED ||
            $this->status == Requested::STATUS_DECLINED ||
            $this->status == Requested::STATUS_CANCELED
        ) {
            return true;
        }
        return false;
    }
}