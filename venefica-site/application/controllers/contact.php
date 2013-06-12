<?php

class Contact extends CI_Controller {
    
    private $initialized = false;
    
    function view() {
        $this->init();
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/contact');
        $this->load->view('pages/footer');
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            $this->lang->load('main');
            
            $this->initialized = true;
        }
    }
}
