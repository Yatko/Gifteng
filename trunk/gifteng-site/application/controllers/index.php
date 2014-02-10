<?php

class Index extends CI_Controller {
    
    private $initialized = false;
    
    public function view() {
        $this->init();
        
        /* mobile redirection */
        if ( $this->agent->is_mobile() ) {
            redirect('/authentication/login', 'refresh');
        }
	
        $this->load->view('templates/'.TEMPLATES.'/header');
        //$this->load->view('pages/index');
        $this->load->view('pages/welcome');
        $this->load->view('pages/footer');
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            $this->lang->load('index');
            $this->lang->load('invitation');

            $this->load->library('user_agent');
            
            clear_cache();
            
            $this->initialized = true;
        }
    }
}
