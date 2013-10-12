<?php

class View extends CI_Controller {
    
    private $initialized = false;
    
    const COMMENTS_NUM = 11;
    
    public function show($adId) {
        $this->init();
        
        if ( !validate_login() ) return;
        $referrer = $this->agent->referrer();
        $is_admin = false;
        
        $ad = $this->getAd($adId);
        if ( endsWith($referrer, '/admin') || endsWith($referrer, '/admin/') ) {
            //coming from admin
            //TODO: find a better and nicer solution
            $is_admin = true;
        } else if ( !$ad->owner && (!$ad->approved || !$ad->online) ) {
            $ad = null;
        }
        
        if ( !validate_ad($ad) ) return;
        
        $currentUser = $this->usermanagement_service->loadUser();
        $comments = $this->message_service->getCommentsByAd($adId, -1, View::COMMENTS_NUM);
        
        /**
        // to mock
        
        $ad = new Ad_model();
        $ad->id = 1;
        $ad->owner = false;
        $ad->creator = $currentUser;
        $ad->title = 'Test';
        
        $comments = null;
        /**/
        
        $data = array();
        $data['ad'] = $ad;
        $data['currentUser'] = $currentUser;
        $data['comments'] = $comments;
        $data['isAdmin'] = $is_admin;
        
        $js_data = array();
        $js_data['currentUser'] = $currentUser;
        
        $modal = '';
        $modal .= $this->load->view('modal/request_create', array(), true);
        $modal .= $this->load->view('modal/edit_post', array(), true);
        $modal .= $this->load->view('modal/ad_delete', array(), true);
        $modal .= $this->load->view('modal/ad_relist', array(), true);
        $modal .= $this->load->view('modal/social', array(), true);
        
        $this->load->view('templates/'.TEMPLATES.'/header', array('modal' => $modal));
        $this->load->view('javascript/follow');
        $this->load->view('javascript/bookmark');
        $this->load->view('javascript/comment', $js_data);
        $this->load->view('javascript/map');
        $this->load->view('javascript/social');
        if ( isBusinessAccount() ) {
            $this->load->view('pages/view_business', $data);
        } else {
            $this->load->view('pages/view_member', $data);
        }
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    public function preview($adId) {
        $this->init();
        
        if ( !validate_login() ) return;
        
        $ad = $this->getAd($adId);
        if ( !validate_ad($ad) ) return;
        
        $data = array();
        $data['ad'] = $ad;
        
        $this->load->view('element/ad_preview', $data);
    }
    
    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            $this->lang->load('view');
            
            $this->load->library('user_agent');
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
            
            clear_cache();
            
            $this->initialized = true;
        }
    }
    
    private function getAd($adId) {
        if ( $adId != null ) {
            try {
                return $this->ad_service->getAdById($adId);
            } catch ( Exception $ex ) {
            }
        }
        return null;
    }
}