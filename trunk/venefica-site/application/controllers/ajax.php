<?php

class Ajax extends CI_Controller {
    
    private $initialized = false;
    
    public function invalid() {
        return;
    }
    
    public function getAdGiving($adId = null, $userId = null) {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        }
        
        if ( $adId == null || $userId == null ) {
            if ( !$_POST ) {
                return;
            }
        }
        
        if ( $adId == null ) {
            $adId = $this->input->post('adId');
        }
        if ( $userId == null ) {
            $userId = $this->input->post('userId');
        }
        
        try {
            $ad = $this->ad_service->getAdById($adId);
            $result = $this->load->view('element/ad_giving', array('ad' => $ad, 'user_id' => $userId), true);
            
            respond_ajax(AJAX_STATUS_RESULT, $result);
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    public function getAdReceiving($adId = null, $requestId = null, $userId = null) {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        }
        
        if ( $adId == null || $requestId == null || $userId == null ) {
            if ( !$_POST ) {
                return;
            }
        }
        
        if ( $adId == null ) {
            $adId = $this->input->post('adId');
        }
        if ( $requestId == null ) {
            $requestId = $this->input->post('requestId');
        }
        if ( $userId == null ) {
            $userId = $this->input->post('userId');
        }
        
        try {
            $ad = $this->ad_service->getAdById($adId);
            $request = $this->ad_service->getRequestById($requestId);
            $result = $this->load->view('element/ad_receiving', array('ad' => $ad, 'request' => $request, 'user_id' => $userId), true);
            
            respond_ajax(AJAX_STATUS_RESULT, $result);
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    // admin related
    
    public function approve() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_POST ) {
            return;
        }
        
        try {
            $adId = $this->input->post('adId');
            
            $this->admin_service->approveAd($adId);
            
            respond_ajax(AJAX_STATUS_RESULT, 'OK');
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    public function unapprove() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_POST ) {
            return;
        }
        
        try {
            $adId = $this->input->post('adId');
            $reason = $this->input->post('reason');
            
            $this->admin_service->unapproveAd($adId, $reason);
            
            respond_ajax(AJAX_STATUS_RESULT, 'OK');
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    public function online() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_POST ) {
            return;
        }
        
        try {
            $adId = $this->input->post('adId');
            
            $this->admin_service->onlineAd($adId);
            
            respond_ajax(AJAX_STATUS_RESULT, 'OK');
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }

    /**
    public function share_message() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_POST ) {
            return;
        }
        
        try {
            $text = $this->input->post('text');
            
            $this->message_service->shareOnSocialNetworks($text);
            
            respond_ajax(AJAX_STATUS_RESULT, 'OK');
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    /**/
    
    // user related
    
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
    
    // comment related
    
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
            $ad_statistics = $this->ad_service->getStatistics($adId);
            
            respond_ajax(AJAX_STATUS_RESULT, $ad_statistics->numComments);
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    // message related
    
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
    
    // ad related
    
    public function approval() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_POST ) {
            return;
        }
        
        $adId = $this->input->post('adId');
        $revision = $this->input->post('revision');
        
        try {
            $approval = $this->ad_service->getApproval($adId, $revision);
            $result = $approval->text;
        } catch ( Exception $ex ) {
            $result = 'Not yet approved';
        }
        
        respond_ajax(AJAX_STATUS_RESULT, $result);
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
            $ad_statistics = $this->ad_service->getStatistics($adId);
            $this->usermanagement_service->refreshUser();
            $currentUser = $this->usermanagement_service->loadUser();
            
            respond_ajax(AJAX_STATUS_RESULT, array(
                AD_BOOKMARKS_NUM => $ad_statistics->numBookmarks,
                USER_BOOKMARKS_NUM => $currentUser->statistics->numBookmarks
            ));
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
            $ad_statistics = $this->ad_service->getStatistics($adId);
            $this->usermanagement_service->refreshUser();
            $currentUser = $this->usermanagement_service->loadUser();
            
            respond_ajax(AJAX_STATUS_RESULT, array(
                AD_BOOKMARKS_NUM => $ad_statistics->numBookmarks,
                USER_BOOKMARKS_NUM => $currentUser->statistics->numBookmarks
            ));
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    public function delete_ad() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_POST ) {
            return;
        }
        
        try {
            $adId = $this->input->post('adId');
            $this->ad_service->deleteAd($adId);
            $this->usermanagement_service->refreshUser();
            
            respond_ajax(AJAX_STATUS_RESULT, 'OK');
        } catch ( Exception $ex ) {
            respond_ajax(AJAX_STATUS_ERROR, $ex->getMessage());
        }
    }
    
    // request related
    
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
        } else if ( !$_POST ) {
            return;
        }
        
        try {
            $requestId = $this->input->post('requestId');
            $adId = $this->input->post('adId');
            $userId = $this->input->post('userId');
            $is_giving = $this->input->post('requestType') == 'giving' ? true : false;
            
            $this->ad_service->cancelRequest($requestId);
            $this->usermanagement_service->refreshUser();
            
            if ( $is_giving ) {
                $this->getAdGiving($adId, $userId);
            } else {
                $this->getAdReceiving($adId, $requestId, $userId);
            }
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
            
            $this->getAdGiving($adId, $userId);
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
            
            $this->getAdGiving($adId, $userId);
            
//            $ad = $this->ad_service->getAdById($adId);
//            
//            $result = $this->load->view('element/ad_giving', array('ad' => $ad, 'user_id' => $userId), true);
//            respond_ajax(AJAX_STATUS_RESULT, $result);
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
            $userId = $_GET['userId'];
            
            $this->ad_service->markAsReceived($requestId);
            $ad = $this->ad_service->getAdById($adId);
            $request = $this->ad_service->getRequestById($requestId);
            
            $result = $this->load->view('element/ad_receiving', array('ad' => $ad, 'request' => $request, 'user_id' => $userId), true);
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
            $this->load->library('admin_service');
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('ad_model');
            $this->load->model('adstatistics_model');
            $this->load->model('user_model');
            $this->load->model('userstatistics_model');
            $this->load->model('comment_model');
            $this->load->model('message_model');
            $this->load->model('request_model');
            $this->load->model('approval_model');
            
            $this->initialized = true;
        }
    }
}
