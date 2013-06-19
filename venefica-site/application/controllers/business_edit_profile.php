<?php

class Business_edit_profile extends CI_Controller {
    
    private $initialized = false;
    
    var $user;
    
    public function view() {
        $this->init();
        
        if ( !validate_login() ) return;
        
        $qs = $this->input->server('QUERY_STRING');
        $this->user = $this->usermanagement_service->loadUser();
        if ( !$this->user->businessAccount ) {
            redirect("/edit_profile/member".(trim($qs) == '' ? '' : '?'.$qs));
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
            
            $this->initialized = true;
        }
    }
}