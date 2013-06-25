<?php

class View extends CI_Controller {
    
    const AJAX_STATUS_RESULT = 'result';
    const AJAX_STATUS_ERROR = 'error';
    
    private $initialized = false;
    
    public function show($adId) {
        $this->init();
        
        if ( !validate_login() ) return;
        if ( $adId == null ) {
            validate_ad(null);
            return;
        }
        
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
    
    public function bookmark() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_GET ) {
            return;
        }
        
        try {
            $adId = $_GET['adId'];
            $this->ad_service->bookmarkAd($adId);
            
            $this->respondAjax(View::AJAX_STATUS_RESULT, 'OK');
        } catch ( Exception $ex ) {
            $this->respondAjax(View::AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    public function remove_bookmark() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_GET ) {
            return;
        }
        
        try {
            $adId = $_GET['adId'];
            $this->ad_service->removeBookmark($adId);
            
            $this->respondAjax(View::AJAX_STATUS_RESULT, 'OK');
        } catch ( Exception $ex ) {
            $this->respondAjax(View::AJAX_STATUS_ERROR, $ex->getMessage());
        }
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
            $this->load->model('adstatistics_model');
            $this->load->model('user_model');
            
            $this->initialized = true;
        }
    }
    
    private function respondAjax($status, $result) {
        $data['obj'] = array($status => $result);
        $this->load->view('json', $data);
    }
}