<?php

class Authentication extends CI_Controller {
    
    private static $ACTION_FORGOT_PASSWORD = "forgot";
    
    private $initialized = false;
    
    public function view($extra_data = array()) {
        $this->init();
        
        $data = array();
        $data['action'] = $this->getAction();
        $data['step'] = $this->getStep();
        $data['is_logged'] = $this->auth_service->isLogged();
        $data['user'] = $this->usermanagement_service->loadUser();
        
        $data = array_merge($data, $extra_data);
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/authentication', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    public function login() {
        $this->init();
        $action = $this->getAction();
        
        $is_valid = $this->login_login();
        
        if ( $is_valid ) {
            //$email = $this->input->post('login_email');
            //$password = $this->input->post('login_password');
            //$remember_me = $this->input->post('login_remember_me');
            
            redirect('/authentication/login');
        }
        
        if ( $is_valid == FALSE ) {
            $this->view();
        }
    }
    
    public function forgot() {
        $this->init();
        
        $is_valid = $this->login_forgot();
        
        if ( $is_valid ) {
            redirect('/authentication/login');
        }
        
        if ( $is_valid == FALSE ) {
            $this->view();
        }
    }
    
    private function login_forgot() {
        $this->load->library('form_validation', null, 'forgot_password_form');
        $this->forgot_password_form->set_error_delimiters('<div class="error">', '</div>');
        $this->forgot_password_form->set_rules('forgot_password_email', 'lang:forgot_password_email', 'trim|required|valid_email|callback_forgot_password');
        
        $this->forgot_password_form->set_message('required', lang('validation_required'));
        $this->forgot_password_form->set_message('valid_email', lang('validation_valid_email'));
        
        $is_valid = $this->forgot_password_form->run();
        return $is_valid;
    }
    
    private function login_login() {
        $is_valid = true;
        $this->load->library('form_validation', null, 'login_form');
        $this->login_form->set_error_delimiters('<div class="error">', '</div>');
        
        //custom validation
        if ( $_POST ) {
            if (
                !$this->login_form->required($this->input->post('login_email')) &&
                !$this->login_form->required($this->input->post('login_password'))
            ) {
                $this->login_form->setError(lang('login_failed_empty_email_password'));
                $is_valid = false;
            } else if ( !$this->login_form->valid_email($this->input->post('login_email')) ) {
                $this->login_form->setError(lang('login_failed_invalid_email'));
                $is_valid = false;
            } else if ( !$this->login_form->required($this->input->post('login_password')) ) {
                $this->login_form->setFieldValue('login_email', $this->input->post('login_email'));
                $this->login_form->setError(lang('login_failed_empty_password'));
                $is_valid = false;
            }
        }
        
        if ( $is_valid ) {
            $this->login_form->set_rules('login_email', 'lang:login_email', 'trim|required|valid_email');
            $this->login_form->set_rules('login_password', 'lang:login_password', 'required|callback_authorize_email_password');
            $this->login_form->set_rules('login_remember_me', 'lang:login_remember_me', '');
            
            $this->login_form->set_message('required', lang('validation_required'));
            $this->login_form->set_message('valid_email', lang('validation_valid_email'));
            
            $is_valid = $this->login_form->run();
        }
        return $is_valid;
    }
    
    /**
     * Internal validation method, that will tries to authenticate user with the
     * provided email and password. On success the token and user data will be
     * stored in the session.
     * 
     * @param string $password
     * @return boolean
     */
    public function authorize_email_password($password) {
        if ( $this->login_form->hasErrors() ) {
            //$this->login_form->set_message('authorize_email_password', 'Cannot authorize!');
            $this->login_form->set_message('authorize_email_password', '');
            return FALSE;
        }
        
        try {
            $email = $this->input->post('login_email');
            $token = $this->auth_service->authenticateEmail($email, $password);
            $this->usermanagement_service->storeUser($email, $token);
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Email and/or password is incorrect: '.$ex->getMessage());
            $this->login_form->set_message('authorize_email_password', lang('login_failed'));
            return FALSE;
        }
        return TRUE;
    }
    
    /**
     * Internal validation method.
     * 
     * @param string $email
     * return boolean
     */
    public function forgot_password($email) {
        if ( $this->forgot_password_form->hasErrors() ) {
            $this->forgot_password_form->set_message('forgot_password', '');
            return FALSE;
        }
        
        if ( true ) {
            //TODO: this will be removed after forgot password will be implememted on server side
            //Till than every attempt will be refused.
            $this->forgot_password_form->set_message('forgot_password', lang('forgot_password_failed'));
            return FALSE;
        }
        return TRUE;
    }
    
    
    // internal functions
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            $this->lang->load('authentication');
            
            $this->load->library('auth_service');
            $this->load->library('usermanagement_service');
            
            $this->initialized = true;
        }
    }
    
    private function getAction() {
        $action = $this->uri->segment(2, null);
        return $action;
    }
    
    private function getStep() {
        $step = $this->uri->segment(3, null);
        return $step;
    }
}
