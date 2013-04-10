<?php

class Browse extends CI_Controller {
    
    private $initialized = false;
    
    public function view($category = null) {
        $this->init();
        
        $data = array();
        
        if ( $this->auth_service->isLogged() ) {
            $lastAdId = -1;
            $data['ads'] = $this->getAds($lastAdId, 5);
            $data['is_ajax'] = false;
            
            $this->storeLastAdId($data['ads']);
        }
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/browse', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    public function ajax() {
        $this->init();
        
        if ( !$this->auth_service->isLogged() ) {
            return;
        }
        
        $data = array();
        $lastAdId = $this->loadLastAdId();
        $data['ads'] = $this->getAds($lastAdId, 10);
        $data['is_ajax'] = true;
        
        $this->storeLastAdId($data['ads']);
        
        $this->load->view('pages/browse', $data);
    }
    
    // internal
    
    private function storeLastAdId($ads) {
        if ( $ads!= null && is_array($ads) && count($ads) > 0 ) {
            $lastAd = end($ads);
            $lastAdId = $lastAd->id;
        } else {
            //this will restart the list
            log_message(ERROR, 'Ads array is null or empty, using lastAdId as -1');
            $lastAdId = -1;
        }
        $this->session->set_flashdata('lastAdId', $lastAdId);
    }
    
    private function loadLastAdId() {
        $lastAd = $this->session->flashdata('lastAdId');
        return $lastAd;
    }
    
    private function getAds($lastAdId, $numberAds) {
        try {
            /**
            $ads = array();
            foreach ( $this->ad_service->getAds($lastAdId, $numberAds) as $ad ) {
                $adId = $ad->id;
                $ad_complete = $this->ad_service->getAdById($adId);
                $ad_complete->comments = $this->comment_service->getCommentsByAd($adId, -1, 2);

                array_push($ads, $ad_complete);
            }
            
            return $ads;
            /**/
            
            return $this->ad_service->getAdsExDetail($lastAdId, $numberAds, null, true, true, 2);
        } catch ( Exception $ex ) {
            return $ex->getMessage();
        }
    }
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            
            $this->load->library('auth_service');
            $this->load->library('ad_service');
            $this->load->library('comment_service');
            
            $this->load->model('image_model');
            $this->load->model('ad_model');
            $this->load->model('user_model');
            $this->load->model('comment_model');
            
            $this->initialized = true;
        }
    }
}
