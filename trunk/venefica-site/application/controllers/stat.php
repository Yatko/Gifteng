<?php

class Stat extends CI_Controller {
    
    private $initialized = false;
    
    const USERS_NUM = 8;
    
    public function view() {
        $this->init();
        
        if ( !validate_login() ) return;
        
        try {
            $users = $this->usermanagement_service->getTopUsers(Stat::USERS_NUM);
        } catch ( Exception $ex ) {
            $users = array();
        }
        
        $data = array();
        $data['users'] = $users;
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/stat', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            
            $this->load->library('usermanagement_service');
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('user_model');
            $this->load->model('userstatistics_model');
            
            clear_cache();
            
            $this->initialized = true;
        }
    }
}
