<?php

class Request extends CI_Controller {
    
    private $initialized = false;
    
    public function view($requestId) {
        $this->init();
        
        if ( !validate_login() ) return;
        
        try {
            if ( $requestId == null ) {
                $request = null;
                $ad = null;
            } else {
                $request = $this->ad_service->getRequestById($requestId);
                $ad = $this->ad_service->getAdById($request->adId);
            }
        } catch ( Exception $ex ) {
            $request = null;
            $ad = null;
        }
        
        if ( !validate_request($request) ) return;
        
        $currentUser = $this->usermanagement_service->loadUser();
        
        if (
            $currentUser->id != $request->user->id &&
            $currentUser->id != $ad->creator->id
        ) {
            // if the logged used it has nothing to do with the request (is not the ad creator neather the requestor)
            if ( !validate_request(null) ) return;
        }
        
        $is_modal = key_exists('modal', $_GET) ? true : false;
        $userId = key_exists('userId', $_GET) ? $_GET['userId'] : null;
        
        if ( key_exists('giving', $_GET) ) {
            $is_giving = true;
        } else if ( $currentUser->id == $request->user->id ) {
            $is_giving = false;
        } else if ( $currentUser->id == $ad->creator->id ) {
            $is_giving = true;
        } else {
            //impossible situation
            $is_giving = false;
        }
        
        if ( $is_giving ) {
            $user1Id = $request->user->id;
            $user2Id = $currentUser->id;
        } else {
            $user1Id = $ad->creator->id;
            $user2Id = $currentUser->id;
        }
        
        $messages = $this->message_service->getMessagesByAdAndUsers($ad->id, $user1Id, $user2Id);
        
        $data = array();
        $data['request'] = $request;
        $data['ad'] = $ad;
        $data['currentUser'] = $currentUser;
        $data['messages'] = $messages;
        $data['is_modal'] = $is_modal;
        $data['userId'] = $userId;
        
        if ( $is_modal ) {
            if ( $is_giving ) {
                $this->load->view('pages/request_giving', $data);
            } else {
                $this->load->view('pages/request_receiving', $data);
            }
        } else {
            $this->load->view('templates/'.TEMPLATES.'/header');
            $this->load->view('javascript/message');
            $this->load->view('javascript/ad');
            $this->load->view('javascript/request');
            if ( $is_giving ) {
                $this->load->view('pages/request_giving', $data);
            } else {
                $this->load->view('pages/request_receiving', $data);
            }
            $this->load->view('templates/'.TEMPLATES.'/footer');
        }
    }
    
    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            $this->lang->load('request');
            
            $this->load->library('ad_service');
            $this->load->library('usermanagement_service');
            $this->load->library('message_service');
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('ad_model');
            $this->load->model('adstatistics_model');
            $this->load->model('user_model');
            $this->load->model('userstatistics_model');
            $this->load->model('message_model');
            $this->load->model('request_model');
            
            $this->initialized = true;
        }
    }
    
}
