<?php

class Index extends CI_Controller {
    
    public function view() {
        //load translations
        $this->lang->load('main');
        $this->lang->load('index');
        $this->lang->load('invitation');
        
        $this->load->library('user_agent');
        
        /* mobile redirection */
        if ( $this->agent->is_mobile() ) {
            redirect('/authentication/login/', 'refresh');
        }
	
        $this->load->view('templates/'.TEMPLATES.'/header');
        //$this->load->view('pages/index');
        $this->load->view('pages/welcome');
        $this->load->view('pages/footer');
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
}
