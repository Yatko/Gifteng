<?php

class ShippingModel {   

	
	public $id;
	public $requestId;
	public $adId;
	public $creatorId;
	public $creatorName;
	public $creatorFullName;
	public $receiverId;
	public $receiverName;
	public $receiverFullName;
	public $acceptedAt;
	public $receivedAmount;
	public $trackingNumber;
	public $barcodeImage;
	public $emailCreatorSent;
	public $emailReceiverSent;
	public $requestStatus;
	public $image;
    
    public function __construct() {
    }
	
}

?>
