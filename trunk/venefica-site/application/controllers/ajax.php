<?php

class Ajax extends CI_Controller {
    
    private $initialized = false;
    
    public function invalid() {
        return;
    }
    
    public function follow() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_GET ) {
            return;
        }
        
        try {
            $userId = $_GET['userId'];
            $this->usermanagement_service->follow($userId);
            
            respond_ajax(AJAX_STATUS_RESULT, 'OK');
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    public function unfollow() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_GET ) {
            return;
        }
        
        try {
            $userId = $_GET['userId'];
            $this->usermanagement_service->unfollow($userId);
            
            respond_ajax(AJAX_STATUS_RESULT, 'OK');
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    public function comment() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_POST ) {
            return;
        }
        
        try {
            $adId = $this->input->post('commentAdId');
            $text = $this->input->post('commentText');
            
            $comment = new Comment_model();
            $comment->text = $text;
            
            $this->message_service->addCommentToAd($adId, $comment);
            $statistics = $this->ad_service->getStatistics($adId);
            
            respond_ajax(AJAX_STATUS_RESULT, $statistics->numComments);
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
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
            $statistics = $this->ad_service->getStatistics($adId);
            
            respond_ajax(AJAX_STATUS_RESULT, $statistics->numBookmarks);
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
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
            $statistics = $this->ad_service->getStatistics($adId);
            
            respond_ajax(AJAX_STATUS_RESULT, $statistics->numBookmarks);
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    // internal functions
    
    private function init() {
        if ( !$this->initialized ) {
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
}
