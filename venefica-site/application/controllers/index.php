<?php

class Index extends CI_Controller {
    
    public function view() {
        //load translations
        $this->lang->load('main');
        
        $this->load->view('templates/header');
        $this->load->view('pages/index');
        $this->load->view('templates/footer');
    }
}
