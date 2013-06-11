<?php

class Landing extends CI_Controller {
    
    private $initialized = false;
    
    public function totegiveaway() {
        $this->init();
        
        $data['address'] = 'http://eepurl.com/AsK6D';
        
        $this->load->view('iframe', $data);
    }
    
    // internal functions
    
    private function init() {
        if ( !$this->initialized ) {
            //TODO:
            
            $this->initialized = true;
        }
    }
}
