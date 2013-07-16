<?php

class Browse extends CI_Controller {
    
    private $initialized = false;
    
    const STARTING_AD_NUM = 5;
    const CONTINUING_AD_NUM = 10;
    const COMMENTS_NUM = 2;
    
    public function view($category = null) {
        $this->init();
        
        if ( !validate_login() ) return;
        
        $currentUser = $this->usermanagement_service->loadUser();
        $lastAdId = -1;
        $ads = $this->getAds($lastAdId, Browse::STARTING_AD_NUM);
        
        $data = array();
        $data['is_ajax'] = false;
        $data['ads'] = $ads;
        $data['currentUser'] = $currentUser;
        
        $modal = $this->load->view('modal/comment', array(), true);
        
        $this->load->view('templates/'.TEMPLATES.'/header', array('modal' => $modal));
        $this->load->view('javascript/follow');
        $this->load->view('javascript/bookmark');
        $this->load->view('javascript/comment');
        $this->load->view('pages/browse', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    //ajax call
    public function get_more() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_GET ) {
            return;
        }
        
        $currentUser = $this->usermanagement_service->loadUser();
        $lastAdId = $_GET['lastAdId'];
        $ads = $this->getAds($lastAdId, Browse::CONTINUING_AD_NUM);
        
        $data = array();
        $data['is_ajax'] = true;
        $data['ads'] = $ads;
        $data['currentUser'] = $currentUser;
        
        $this->load->view('pages/browse', $data);
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
            $this->load->model('userstatistics_model');
            $this->load->model('comment_model');
            $this->load->model('request_model');
            
            $this->initialized = true;
        }
    }
    
    private function getAds($lastAdId, $numberAds) {
        /**
        // to mock
        
        $ads = array();
        $currentUser = $this->usermanagement_service->loadUser();
        for ( $i = 1; $i <= $numberAds; $i++ ) {
            $ad = new Ad_model();
            $ad->id = $lastAdId + $i;
            $ad->owner = true;
            $ad->creator = $currentUser;
            $ad->title = 'Test';
            
            array_push($ads, $ad);
        }
        return $ads;
        /**/
        
        try {
            return $this->ad_service->getAdsExDetail($lastAdId, $numberAds, null, true, true, Browse::COMMENTS_NUM);
        } catch ( Exception $ex ) {
            return $ex->getMessage();
        }
    }
}
