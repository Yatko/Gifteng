<?php

class Landing extends CI_Controller {
    
    private $initialized = false;
    
    public function totegiveaway() {
        $this->init();
        
        $data = array();
        $data['address'] = 'http://eepurl.com/AsK6D';
        
        $this->load->view('iframe', $data);
    }
    
    public function jobs() {
        $this->init();
        
        $data = array();
        $data['address'] = 'http://gifteng.zendesk.com/forums/22379438-Jobs';
        
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
