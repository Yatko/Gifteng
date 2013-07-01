<?php

class Profile extends CI_Controller {
    
    private $initialized = false;
    
    public function view($name = null) {
        $this->init();
        
        if ( !validate_login() ) return;
        
        try {
            if ( !empty($name) ) {
                if ( is_numeric($name) ) {
                    $user = $this->usermanagement_service->getUserById($name);
                } else {
                    $user = $this->usermanagement_service->getUserByName($name);
                }
            } else {
                $user = $this->usermanagement_service->loadUser();
            }
        } catch ( Exception $ex ) {
            $user = null;
        }
        
        if ( !validate_user($user) ) return;

//        try {
//            $receivings = $this->ad_service->getUserRequestedAds($user->id);
//        } catch ( Exception $ex ) {
//            $receivings = null;
//        }

//        try {
//            $givings = $this->ad_service->getUserAds($user->id, false);
//        } catch ( Exception $ex ) {
//            $givings = null;
//        }

//        try {
//            $bookmarks = $this->ad_service->getBookmarkedAds($user->id);
//        } catch ( Exception $ex ) {
//            $bookmarks = null;
//        }

//        try {
//            $followers = $this->usermanagement_service->getFollowers($user->id);
//        } catch ( Exception $ex ) {
//            $followers = null;
//        }
//        try {
//            $followings = $this->usermanagement_service->getFollowings($user->id);
//        } catch ( Exception $ex ) {
//            $followings = null;
//        }

//        try {
//            $ratings = $this->ad_service->getReceivedRatings($user->id);
//        } catch ( Exception $ex ) {
//            $ratings = null;
//        }
        
        $data = array();
        $data['user'] = $user;
        //$data['receivings'] = $receivings;
        //$data['givings'] = $givings;
        //$data['followers'] = $followers;
        //$data['followings'] = $followings;
        //$data['ratings'] = $ratings;
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/profile', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    public function change_avatar() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_FILES ) {
            return;
        }
        
        $field = 'image';
        if ( !isset($_FILES[$field]) || $_FILES[$field]['error'] == 4 ) {
            //no file selected for upload
            return;
        }
        
        $config['upload_path'] = TEMP_FOLDER;
        $config['allowed_types'] = 'gif|jpg|png|jpeg';
        $config['encrypt_name'] = true;
        
        $this->load->library('upload', $config);
        if ( !$this->upload->do_upload($field) ) {
            $error = $this->upload->display_errors();
            respond_ajax(AJAX_STATUS_ERROR, $error);
            return;
        }
        
        $data = $this->upload->data();
        $image_file_name = $data['file_name'];
        $image = Image_model::createImageModel($image_file_name);
        
        try {
            $user = $this->usermanagement_service->loadUser();
            $user->avatar = $image;
            
            $this->usermanagement_service->updateUser($user);
            $this->usermanagement_service->refreshUser();
            $user = $this->usermanagement_service->loadUser();
            
            respond_ajax(AJAX_STATUS_RESULT, $user->getAvatarUrl());
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->getMessage());
            respond_ajax(AJAX_STATUS_ERROR, 'Something went wrong !');
        }
    }

    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            $this->lang->load('profile');
            
            $this->load->library('ad_service');
            $this->load->library('usermanagement_service');
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('ad_model');
            $this->load->model('adstatistics_model');
            $this->load->model('user_model');
            $this->load->model('userstatistics_model');
            $this->load->model('rating_model');
            
            $this->initialized = true;
        }
    }
}