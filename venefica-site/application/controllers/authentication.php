<?php

class Authentication extends CI_Controller {
    
    var $initialized = false;
    
    public function view() {
        $this->init();
        
        $data['selected_tab'] = $this->uri->segment(2, null);
        
        $this->load->view('templates/header');
        $this->load->view('pages/authentication', $data);
        $this->load->view('templates/footer');
    }
    
    public function login() {
        $this->init();
        
        $this->load->library('form_validation', null, 'login_form');
        $this->login_form->set_error_delimiters('<div class="error">', '</div>');
        $this->login_form->set_rules('login_email', 'lang:authentication_email', 'trim|required|valid_email');
        $this->login_form->set_rules('login_password', 'lang:authentication_password', 'required');
        $this->login_form->set_rules('login_remember_me', 'lang:authentication_remember_me', '');
        $is_valid = $this->login_form->run();
        
        if ( $is_valid == FALSE ) {
            $this->view();
        } else {
            $email = $this->input->post('login_email');
            $password = $this->input->post('login_password');
            $remember_me = $this->input->post('login_remember_me');
            
            //$this->session->set_flashdata('msg', validation_errors('<div>','</div>'));
            //redirect('/','location');
            
            echo "login validated";
        }
    }
    
    public function invitation() {
        $this->init();
        
        $this->load->library('form_validation', null, 'invitation_form');
        $this->invitation_form->set_error_delimiters('<div class="error">', '</div>');
        $this->invitation_form->set_rules('invitation_email', 'lang:invitation_email', 'trim|required|valid_email');
        $is_valid = $this->invitation_form->run();
        
        if ( $is_valid == FALSE ) {
            $this->view();
        } else {
            $email = $this->input->post('invitation_email');
            
            echo "inivitation validated";
        }
    }
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            $this->lang->load('authentication');
            
            $this->initialized = true;
        }
    }
}
