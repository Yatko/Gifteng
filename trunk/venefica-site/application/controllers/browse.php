<?php

class Browse extends CI_Controller {
    
    const AJAX_STATUS_RESULT = 'result';
    const AJAX_STATUS_ERROR = 'error';
    
    private $initialized = false;
    
    private static $STARTING_AD_NUM = 5;
    private static $CONTINUING_AD_NUM = 10;
    
    public function view($category = null) {
        $this->init();
        
        if ( !validate_login() ) return;
        
        $user = $this->usermanagement_service->loadUser();
        $lastAdId = -1;
        $ads = $this->getAds($lastAdId, Browse::$STARTING_AD_NUM);
        
        $data = array();
        $data['is_ajax'] = false;
        $data['ads'] = $ads;
        $data['user'] = $user;
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/browse', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    public function get_more() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_GET ) {
            return;
        }
        
        $user = $this->usermanagement_service->loadUser();
        $lastAdId = $_GET['lastAdId'];
        $ads = $this->getAds($lastAdId, Browse::$CONTINUING_AD_NUM);
        
        $data = array();
        $data['is_ajax'] = true;
        $data['ads'] = $ads;
        $data['user'] = $user;
        
        $this->load->view('pages/browse', $data);
    }
    
    public function follow() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_GET ) {
            return;
        }
        
        try {
            $userId = $_GET['userId'];
            $this->usermanagement_service->follow($userId);
            
            $this->respondAjax(Browse::AJAX_STATUS_RESULT, 'OK');
        } catch ( Exception $ex ) {
            $this->respondAjax(Browse::AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    public function unfollow() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_GET ) {
            return;
        }
        
        try {
            $userId = $_GET['userId'];
            $this->usermanagement_service->unfollow($userId);
            
            $this->respondAjax(Browse::AJAX_STATUS_RESULT, 'OK');
        } catch ( Exception $ex ) {
            $this->respondAjax(Browse::AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    public function comment() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_POST ) {
            return;
        }
        
        try {
            $adId = $this->input->post('commentAdId');
            $text = $this->input->post('commentText');
            
            $comment = new Comment_model();
            $comment->text = $text;
            
            $this->message_service->addCommentToAd($adId, $comment);
            $statistics = $this->ad_service->getStatistics($adId);
            
            $this->respondAjax(Browse::AJAX_STATUS_RESULT, $statistics->numComments);
        } catch ( Exception $ex ) {
            $this->respondAjax(Browse::AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            $this->lang->load('browse');
            
            $this->load->library('ad_service');
            $this->load->library('usermanagement_service');
            $this->load->library('message_service');
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('ad_model');
            $this->load->model('adstatistics_model');
            $this->load->model('user_model');
            $this->load->model('comment_model');
            $this->load->model('request_model');
            
            $this->initialized = true;
        }
    }
    
    private function getAds($lastAdId, $numberAds) {
        /**
        // to mock
        
        $ads = array();
        $user = $this->usermanagement_service->loadUser();
        for ( $i = 1; $i <= $numberAds; $i++ ) {
            $ad = new Ad_model();
            $ad->id = $lastAdId + $i;
            $ad->owner = true;
            $ad->creator = $user;
            $ad->title = 'Test';
            
            array_push($ads, $ad);
        }
        return $ads;
        /**/
        
        try {
            return $this->ad_service->getAdsExDetail($lastAdId, $numberAds, null, true, true, 2);
        } catch ( Exception $ex ) {
            return $ex->getMessage();
        }
    }
    
    private function respondAjax($status, $result) {
        $data['obj'] = array($status => $result);
        $this->load->view('json', $data);
    }
}
