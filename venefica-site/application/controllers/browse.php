<?php

class Browse extends CI_Controller {
    
    private $initialized = false;
    
    private static $STARTING_AD_NUM = 5;
    private static $CONTINUING_AD_NUM = 10;
    
    public function view($category = null) {
        $this->init();
        
        $data = array();
        $data['isLogged'] = isLogged();
        $data['is_ajax'] = false;
        
        if ( isLogged() ) {
            $lastAdId = $this->getLastAdId();
            $ads = $this->getAds($lastAdId, Browse::$STARTING_AD_NUM);
            
            $data['ads'] = $ads;
        }
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/browse', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    public function ajax() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        }
        
        $lastAdId = $this->getLastAdId();
        $ads = $this->getAds($lastAdId, Browse::$CONTINUING_AD_NUM);
        
        $data = array();
        $data['isLogged'] = isLogged();
        $data['is_ajax'] = true;
        $data['ads'] = $ads;
        
        $this->load->view('pages/browse', $data);
    }
    
    // internal
    
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
    
    private function getLastAdId() {
        //default value is -1
        $lastAdId = $this->uri->segment(3, -1);
        return $lastAdId;
    }
}
