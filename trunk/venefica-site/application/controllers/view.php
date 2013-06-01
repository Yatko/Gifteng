<?php

class View extends CI_Controller {
    
    private $initialized = false;
    
    public function show($adId) {
        $this->init();
        
        if ( !validate_login() ) return;
        
        try {
            $ad = $this->ad_service->getAdById($adId);
        } catch ( Exception $ex ) {
            $ad = null;
        }
        
        if ( !validate_ad($ad) ) return;
        
        $user = $this->usermanagement_service->loadUser();
        
        $data = array();
        $data['adId'] = $adId;
        $data['ad'] = $ad;
        $data['user'] = $user;
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        if ( $user->businessAccount ) {
            $this->load->view('pages/business_ad_view', $data);
        } else {
            $this->load->view('pages/member_ad_view', $data);
        }
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    public function invalid($data = array()) {
        $this->init();
        
        if ( !validate_login() ) return;
        if ( !validate_ad(null) ) return;
    }
    
    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            $this->lang->load('view');
            
            $this->load->library('auth_service');
            $this->load->library('ad_service');
            $this->load->library('usermanagement_service');
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('ad_model');
            $this->load->model('user_model');
            
            $this->initialized = true;
        }
    }
}