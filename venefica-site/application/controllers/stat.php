<?php

class Stat extends CI_Controller {
    
    private $initialized = false;
    
    public function view() {
        $this->init();
        
        if ( !validate_login() ) return;
        
        $data = array();
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/stat', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            
            clear_cache();
            
            $this->initialized = true;
        }
    }
}
