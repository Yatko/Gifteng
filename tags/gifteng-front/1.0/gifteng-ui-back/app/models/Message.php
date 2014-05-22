<?php

class Message {
	
    public $id; //long
    public $requestId; //long
    public $adId; //long
    public $adTitle; //string
    public $text; //string
    public $owner; //boolean
    public $toId; //long
    public $toName; //string
    public $toFullName; //string
    public $toAvatarUrl; //string
    public $fromId; //long
    public $fromName; //string
    public $fromFullName; //string
    public $fromAvatarUrl; //string
    public $read; //boolean
    public $createdAt; //long - timestamp
    
}
