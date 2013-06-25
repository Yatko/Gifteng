<?php

class Admin extends CI_Controller {
    
    private $initialized = false;
    
    public function dashboard() {
        $this->init();
        
        if ( !validate_login() ) return;
        
        $data = array();
        
        try {
            $users = $this->usermanagement_service->getUsers();
            $data['users'] = $users;
        } catch ( Exception $ex ) {
        }
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('admin/dashboard', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    // internal functions
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            
            $this->load->library('usermanagement_service');
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('user_model');
            
            $this->initialized = true;
        }
    }
}