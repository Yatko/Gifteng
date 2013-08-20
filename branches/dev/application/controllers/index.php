<?php

class Index extends CI_Controller {
    
    public function view() {
        //load translations
        $this->lang->load('main');
        $this->lang->load('index');
        $this->lang->load('invitation');
        
        $this->load->view('templates/'.TEMPLATES.'/welcome_header');
        $this->load->view('pages/welcome');
        $this->load->view('pages/footer');
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
}
