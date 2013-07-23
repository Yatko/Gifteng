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
            $this->usermanagement_service->refreshUser();
            
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
            $this->usermanagement_service->refreshUser();
            
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
    
    public function message() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_POST ) {
            return;
        }
        
        try {
            $requestId = $this->input->post('messageRequestId');
            $toId = $this->input->post('messageToId');
            $text = $this->input->post('messageText');
            
            $message = new Message_model();
            $message->text = $text;
            $message->requestId = $requestId;
            $message->toId = $toId;
            
            $this->message_service->sendMessage($message);
            
            respond_ajax(AJAX_STATUS_RESULT, 'OK');
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
            $this->usermanagement_service->refreshUser();
            
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
            $this->usermanagement_service->refreshUser();
            
            respond_ajax(AJAX_STATUS_RESULT, $statistics->numBookmarks);
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    public function request() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_POST ) {
            return;
        }
        
        try {
            $adId = $this->input->post('requestAdId');
            $text = $this->input->post('requestText');
            
            $this->ad_service->requestAd($adId, $text);
            $this->usermanagement_service->refreshUser();
            
            respond_ajax(AJAX_STATUS_RESULT, 'OK');
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    public function hide_request() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_GET ) {
            return;
        }
        
        try {
            $requestId = $_GET['requestId'];
            $this->ad_service->hideRequest($requestId);
            $this->usermanagement_service->refreshUser();
            
            respond_ajax(AJAX_STATUS_RESULT, 'OK');
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    public function cancel_request() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_GET ) {
            return;
        }
        
        try {
            $requestId = $_GET['requestId'];
            $adId = $_GET['adId'];
            $userId = key_exists('userId', $_GET) ? $_GET['userId'] : null;
            $is_giving = key_exists('giving', $_GET) ? true : false;
            
            $this->ad_service->cancelRequest($requestId);
            $this->usermanagement_service->refreshUser();
            $ad = $this->ad_service->getAdById($adId);
            $request = $this->ad_service->getRequestById($requestId);
            
            if ( $is_giving ) {
                $result = $this->load->view('element/ad_giving', array('ad' => $ad, 'user_id' => $userId), true);
            } else {
                $result = $this->load->view('element/request_receiving', array('ad' => $ad, 'request' => $request), true);
            }
            
            respond_ajax(AJAX_STATUS_RESULT, $result);
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    public function select_request() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_GET ) {
            return;
        }
        
        try {
            $requestId = $_GET['requestId'];
            $adId = $_GET['adId'];
            $userId = $_GET['userId'];
            
            $this->ad_service->selectRequest($requestId);
            $ad = $this->ad_service->getAdById($adId);
            
            $result = $this->load->view('element/ad_giving', array('ad' => $ad, 'user_id' => $userId), true);
            respond_ajax(AJAX_STATUS_RESULT, $result);
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    public function send_request() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_GET ) {
            return;
        }
        
        try {
            $requestId = $_GET['requestId'];
            $adId = $_GET['adId'];
            $userId = $_GET['userId'];
            
            $this->ad_service->markAsSent($requestId);
            $ad = $this->ad_service->getAdById($adId);
            
            $result = $this->load->view('element/ad_giving', array('ad' => $ad, 'user_id' => $userId), true);
            respond_ajax(AJAX_STATUS_RESULT, $result);
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    public function receive_request() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_GET ) {
            return;
        }
        
        try {
            $requestId = $_GET['requestId'];
            $adId = $_GET['adId'];
            
            $this->ad_service->markAsReceived($requestId);
            $ad = $this->ad_service->getAdById($adId);
            $request = $this->ad_service->getRequestById($requestId);
            
            $result = $this->load->view('element/request_receiving', array('ad' => $ad, 'request' => $request), true);
            respond_ajax(AJAX_STATUS_RESULT, $result);
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
            $this->load->model('message_model');
            $this->load->model('request_model');
            
            $this->initialized = true;
        }
    }
}
