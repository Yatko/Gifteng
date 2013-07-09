<?php

class Post extends CI_Controller {
    
    private $initialized = false;
    
    public function post_redirect() {
        $this->init();
        
        if ( !validate_login() ) return;
        
        if ( isBusinessAccount() ) {
            safe_redirect("/post/business");
        } else {
            safe_redirect("/post/member");
        }
    }
    
    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            $this->load->library('usermanagement_service');
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('user_model');
            $this->load->model('userstatistics_model');
            
            $this->initialized = true;
        }
    }
}