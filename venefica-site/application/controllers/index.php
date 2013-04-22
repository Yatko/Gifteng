<?php

class Index extends CI_Controller {
    
    public function view() {
        //load translations
        $this->lang->load('main');
        $this->lang->load('index');
        $this->lang->load('invitation');
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/index');
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
}
