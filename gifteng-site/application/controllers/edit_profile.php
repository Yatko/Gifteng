<?php

class Edit_profile extends CI_Controller {
    
    private $initialized = false;
    
    public function edit_profile_redirect() {
        $this->init();
        
        if ( !validate_login() ) return;
        
        if ( isBusinessAccount() ) {
            safe_redirect("/edit_profile/business");
        } else {
            safe_redirect("/edit_profile/member");
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