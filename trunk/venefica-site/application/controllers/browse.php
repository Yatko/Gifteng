<?php

class Browse extends CI_Controller {
    
    private $initialized = false;
    
    const TYPE_NEWEST = 'newest';
    const TYPE_OLDEST = 'oldest';
    const TYPE_CLOSEST = 'closest';
    const TYPE_GIFTED = 'gifted';
    
    const STARTING_AD_NUM = 15;
    const CONTINUING_AD_NUM = 10;
    const COMMENTS_NUM = 2;
    
    public function view() {
        $this->init();
        
        if ( !validate_login() ) return;
        
        try {
            $categories = $this->ad_service->getAllCategories();
        } catch ( Exception $ex ) {
            $categories = array();
        }
        
        if ( $_POST ) {
            $q = $this->input->post('q');
            $category = $this->input->post('category');
            $type = $this->input->post('type');
            
            if ( trim($category) == "" ) $category = null;
            if ( trim($type) == "" ) $type = null;
        } else if ( $_GET ) {
            $q = key_exists('q', $_GET) ? $_GET['q'] : '';
            $category = null;
            $type = null;
        } else {
            $q = null;
            $category = null;
            $type = null;
        }
        
        $currentUser = $this->usermanagement_service->loadUser();
        $filter = $this->buildFilter($currentUser, $q, $category, $type);
        
        $selected_q = $q;
        $selected_category = $category;
        $selected_type = $type;
        $lastIndex = 0;
        
        $ads = $this->getAds($lastIndex, Browse::STARTING_AD_NUM, $filter);
        //$last_ad_id = $this->getLastAdId($ads);
        $last_index = $this->getLastIndex($ads);
        
        $data = array();
        $data['is_ajax'] = false;
        $data['ads'] = $ads;
        //$data['last_ad_id'] = $last_ad_id;
        $data['last_index'] = $last_index;
        $data['currentUser'] = $currentUser;
        $data['categories'] = $categories;
        $data['selected_q'] = $selected_q;
        $data['selected_category'] = $selected_category;
        $data['selected_type'] = $selected_type;
        
        $js_data = array();
        $js_data['currentUser'] = $currentUser;
        
        $modal = $this->load->view('modal/comment', array(), true);
        $modal .= $this->load->view('modal/social', array(), true);
        
        $this->load->view('templates/'.TEMPLATES.'/header', array('modal' => $modal));
        $this->load->view('javascript/follow');
        $this->load->view('javascript/bookmark');
        $this->load->view('javascript/comment', $js_data);
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
        //$lastAdId = $_GET['lastAdId'];
        $lastIndex = $_GET['lastIndex'];
        $q = $this->input->post('q');
        $category = $this->input->post('category');
        $type = $this->input->post('type');
        $filter = $this->buildFilter($currentUser, $q, $category, $type);
        
        $ads = $this->getAds($lastIndex, Browse::CONTINUING_AD_NUM, $filter);
        //$last_ad_id = $this->getLastAdId($ads);
        $last_index = $this->getLastIndex($ads);
        
        if (is_empty($ads) ) {
            return '';
        }
        
        $data = array();
        $data['is_ajax'] = true;
        $data['ads'] = $ads;
        //$data['last_ad_id'] = $last_ad_id;
        $data['last_index'] = $last_index;
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
            $this->load->model('category_model');
            
            clear_cache();
            
            $this->initialized = true;
        }
    }
    
    private function getAds($lastIndex, $numberAds, $filter = null) {
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
            $ads = $this->ad_service->getAdsExDetail($lastIndex, $numberAds, $filter, true, true, Browse::COMMENTS_NUM);
            return $ads;
        } catch ( Exception $ex ) {
            return $ex->getMessage();
        }
    }
    
//    private function getLastAdId($ads) {
//        if ( isset($ads) && is_array($ads) && count($ads) > 0 ) {
//            $last_ad = end(array_values($ads));
//            $last_ad_id = $last_ad->id;
//        } else {
//            $last_ad_id = -1;
//        }
//        return $last_ad_id;
//    }
    
    private function getLastIndex($ads) {
        if ( isset($ads) && is_array($ads) && count($ads) > 0 ) {
            $last_ad = end(array_values($ads));
            $last_index = $last_ad->lastIndex;
        } else {
            $last_index = 0;
        }
        return $last_index;
    }
    
    private function buildFilter($currentUser, $query, $category = null, $type = null) {
        if ( $query != null && trim($query) == "" ) {
            $query = null;
        }
        if ( $type == null ) {
            $type = Browse::TYPE_NEWEST;
        }
        
        $filter = new Filter_model();
        //$filter->includeOwned = true;
        $filter->searchString = $query;
        $filter->categories = ($category != null ? array($category) : null);
        if ( $type == Browse::TYPE_NEWEST ) {
            $filter->orderAsc = false;
            $filter->orderClosest = false;
            $filter->filterType = Filter_model::FILTER_TYPE_ACTIVE;
            //$filter->includeInactive = false;
            //$filter->includeShipped = false;
            //$filter->includeRequested = true;
            //$filter->includeCanRequest = true;
        } else if ( $type == Browse::TYPE_OLDEST ) {
            $filter->orderAsc = true;
            $filter->orderClosest = false;
            $filter->filterType = Filter_model::FILTER_TYPE_ACTIVE;
            //$filter->includeInactive = false;
            //$filter->includeShipped = false;
            //$filter->includeRequested = true;
            //$filter->includeCanRequest = true;
        } else if ( $type == Browse::TYPE_CLOSEST ) {
            $filter->orderAsc = true;
            $filter->orderClosest = true;
            $filter->filterType = Filter_model::FILTER_TYPE_ACTIVE;
            //$filter->includeInactive = false;
            //$filter->includeShipped = false;
            //$filter->includeRequested = true;
            //$filter->includeCanRequest = true;
            $filter->latitude = ($currentUser != null && $currentUser->address != null) ? $currentUser->address->latitude : null;
            $filter->longitude = ($currentUser != null && $currentUser->address != null) ? $currentUser->address->longitude : null;
        } else if ( $type == Browse::TYPE_GIFTED ) {
            $filter->orderAsc = false;
            $filter->orderClosest = false;
            $filter->filterType = Filter_model::FILTER_TYPE_GIFTED;
            //$filter->includeInactive = true;
            //$filter->includeShipped = true;
            //$filter->includeRequested = true;
            //$filter->includeCanRequest = false;
        }
        return $filter;
    }
}
