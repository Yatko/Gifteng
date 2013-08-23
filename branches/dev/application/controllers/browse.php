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
        $query = key_exists('q', $_GET) ? $_GET['q'] : '';
        $lastAdId = -1;
        $filter = $this->buildFilter($query);
        $ads = $this->getAds($lastAdId, Browse::STARTING_AD_NUM, $filter);
        
        $data = array();
        $data['is_ajax'] = false;
        $data['query'] = $query;
        $data['ads'] = $ads;
        $data['currentUser'] = $currentUser;
        
        $modal = $this->load->view('modal/comment', array(), true);
        $modal .= $this->load->view('modal/social', array(), true);
        
        $this->load->view('templates/'.TEMPLATES.'/header', array('modal' => $modal));
        $this->load->view('javascript/follow');
        $this->load->view('javascript/bookmark');
        $this->load->view('javascript/comment');
        $this->load->view('javascript/social');
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
        $query = key_exists('q', $_GET) ? $_GET['q'] : '';
        $lastAdId = $_GET['lastAdId'];
        $filter = $this->buildFilter($query);
        $ads = $this->getAds($lastAdId, Browse::CONTINUING_AD_NUM, $filter);
        
        $data = array();
        $data['is_ajax'] = true;
        $data['query'] = $query;
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
            $this->load->model('filter_model');
            
            $this->initialized = true;
        }
    }
    
    private function getAds($lastAdId, $numberAds, $filter = null) {
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
            $ads = $this->ad_service->getAdsExDetail($lastAdId, $numberAds, $filter, true, true, Browse::COMMENTS_NUM);
            
            if ( count($ads) > 0 ) {
                $has_ads = true;
                $last_ad = end(array_values($ads));
                $last_ad_id = $last_ad->id;
            } else {
                $has_ads = false;
                $last_ad_id = -1;
            }
            
            foreach ( $ads as $key => $ad ) {
                if ( $ad->approved && $ad->online ) {
                    continue;
                }
                //remove all non approved and non online ads
                unset($ads[$key]);
            }
            
            if ( count($ads) == 0 && $has_ads ) {
                $ads = $this->getAds($last_ad_id, $numberAds, $filter);
            }
            
            return $ads;
        } catch ( Exception $ex ) {
            return $ex->getMessage();
        }
    }
    
    private function buildFilter($query) {
        if ( $query == null || trim($query) == '' ) {
            return null;
        }
        $filter = new Filter_model();
        $filter->searchString = $query;
        return $filter;
    }
}
