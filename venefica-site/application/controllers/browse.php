<?php

class Browse extends CI_Controller {
    
    private $initialized = false;
    
    private static $STARTING_AD_NUM = 5;
    private static $CONTINUING_AD_NUM = 10;
    
    public function view($category = null) {
        $this->init();
        
        if ( !validate_login() ) return;
        
        $lastAdId = $this->getLastAdIdFromUri();
        $ads = $this->getAds($lastAdId, Browse::$STARTING_AD_NUM);
        
        $data = array();
        $data['is_ajax'] = false;
        $data['ads'] = $ads;
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/browse', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    public function ajax() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        }
        
        $lastAdId = $this->getLastAdIdFromUri();
        $ads = $this->getAds($lastAdId, Browse::$CONTINUING_AD_NUM);
        
        $data = array();
        $data['is_ajax'] = true;
        $data['ads'] = $ads;
        
        $this->load->view('pages/browse', $data);
    }
    
    // internal
    
    private function getAds($lastAdId, $numberAds) {
        try {
            return $this->ad_service->getAdsExDetail($lastAdId, $numberAds, null, true, true, 2);
        } catch ( Exception $ex ) {
            return $ex->getMessage();
        }
    }
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            $this->lang->load('browse');
            
            $this->load->library('auth_service');
            $this->load->library('ad_service');
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('ad_model');
            $this->load->model('user_model');
            $this->load->model('comment_model');
            
            $this->initialized = true;
        }
    }
    
    private function getLastAdIdFromUri() {
        //default value is -1
        $lastAdId = $this->uri->segment(3, -1);
        return $lastAdId;
    }
}
