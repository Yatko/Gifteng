<?php

class Comment_model {
    
    public $id; //long
    public $text; //string
    public $owner; //boolean
    public $publisherId; //long
    public $publisherName; //string
    public $publisherFullName; //string
    public $publisherAvatarUrl; //string
    public $createdAt; //long - timestamp
    
}