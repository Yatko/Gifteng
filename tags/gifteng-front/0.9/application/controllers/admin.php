<?php

class Admin extends CI_Controller {
    
    private $initialized = false;
    
    public function dashboard() {
        $this->init();
        
        if ( !validate_login() ) return;
        
        $data = array();
        //$data['businessUsers'] = $this->getBusinessUsers();
        $data['unapprovedAds'] = $this->getUnapprovedAds();
        //$data['offlineAds'] = $this->getOfflineAds();
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('javascript/admin');
        $this->load->view('admin/dashboard', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    // internal functions
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            
            $this->load->library('admin_service');
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('user_model');
            $this->load->model('userstatistics_model');
            $this->load->model('approval_model');
            
            clear_cache();
            
            $this->initialized = true;
        }
    }
    
    private function getBusinessUsers() {
        try {
            $users = $this->admin_service->getUsers();
        } catch ( Exception $ex ) {
            $users = array();
        }
        
        $businessUsers = array();
        foreach ($users as $user) {
            if ( !$user->businessAccount ) {
                continue;
            }
            array_push($businessUsers, $user);
        }
        return $businessUsers;
    }
    
    private function getUnapprovedAds() {
        try {
            $ads = $this->admin_service->getUnapprovedAds();
        } catch ( Exception $ex ) {
            $ads = array();
        }
        return $ads;
    }
    
    private function getOfflineAds() {
        try {
            $ads = $this->admin_service->getOfflineAds();
        } catch ( Exception $ex ) {
            $ads = array();
        }
        return $ads;
    }
}