<?php

class Edit_profile_business extends CI_Controller {
    
    private $initialized = false;
    
    var $user;
    
    public function view() {
        $this->init();
        
        if ( !validate_login() ) return;
        
        if ( !isBusinessAccount() ) {
            safe_redirect("/edit_profile/member");
        }
    }
    
    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            
            $this->load->library('usermanagement_service');
            
            $this->load->model('address_model');
            $this->load->model('user_model');
            $this->load->model('userstatistics_model');
            
            $this->initialized = true;
        }
    }
}