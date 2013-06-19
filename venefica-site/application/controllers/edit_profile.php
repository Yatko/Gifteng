<?php

class Edit_profile extends CI_Controller {
    
    private $initialized = false;
    
    public function edit_profile_redirect() {
        $this->init();
        
        if ( !validate_login() ) return;
        
        $qs = $this->input->server('QUERY_STRING');
        $this->user = $this->usermanagement_service->loadUser();
        if ( $this->user->businessAccount ) {
            redirect("/edit_profile/business".(trim($qs) == '' ? '' : '?'.$qs));
        } else {
            redirect("/edit_profile/member".(trim($qs) == '' ? '' : '?'.$qs));
        }
    }
    
    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            $this->load->library('usermanagement_service');
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('user_model');
            
            $this->initialized = true;
        }
    }
}