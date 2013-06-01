<?php

class Authentication extends CI_Controller {
    
    private $initialized = false;
    
    public function view($extra_data = array()) {
        $this->init();
        
        $data = array();
        $data['action'] = $this->getActionFromUri();
        $data['isLogged'] = isLogged();
        $data['user'] = $this->usermanagement_service->loadUser();
        
        $data = array_merge($data, $extra_data);
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/authentication', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    public function login() {
        $this->init();
        $action = $this->getActionFromUri();
        
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
        $extra_data = array();
        
        $is_valid = $this->login_forgot();
        
        if ( $is_valid ) {
            //form data valid after post
            $this->session->set_flashdata('authentication_forgot_password_requested', true);
            
            redirect('/authentication/forgot');
        } elseif ( $_POST ) {
            //form data not valid after post
        } else {
            //direct access of the page
            $authentication_forgot_password_requested = $this->session->flashdata('authentication_forgot_password_requested');
            $extra_data['authentication_forgot_password_requested'] = $authentication_forgot_password_requested;
        }
        
        if ( $is_valid == FALSE ) {
            $this->view($extra_data);
        }
    }
    
    public function reset() {
        $this->init();
        $extra_data = array();
        
        $code = $this->getCodeFromUri();
        if ( empty($code) ) {
            $code = $this->input->post('reset_password_code');
        }
        if ( empty($code) ) {
            redirect('/authentication/login');
        }
        
        $is_valid = $this->login_reset();
        $extra_data['reset_password_code'] = $code;
        
        if ( $is_valid ) {
            //form data valid after post
            redirect('/authentication/login');
        } elseif ( $_POST ) {
            //form data not valid after post
        } else {
            //direct access of the page
        }
        
        if ( $is_valid == FALSE ) {
            $this->view($extra_data);
        }
    }
    
    public function logout() {
        $this->init();
        
        destroySession();
        
        redirect('/authentication/login');
    }
    
    
    
    private function login_reset() {
        $is_valid = true;
        $this->load->library('form_validation', null, 'reset_password_form');
        $this->reset_password_form->set_error_delimiters('<div class="error">', '</div>');
        
        if ( $_POST ) {
            if (
                !$this->reset_password_form->required($this->input->post('reset_password_p_1')) ||
                !$this->reset_password_form->required($this->input->post('reset_password_p_2'))
            ) {
                $this->reset_password_form->setError(lang('reset_password_failed_empty_passwords'));
                $is_valid = false;
            } else if ( !$this->reset_password_form->matches($this->input->post('reset_password_p_1'), 'reset_password_p_2') ) {
                $this->reset_password_form->setError(lang('reset_password_failed_not_matching_passwords'));
                $is_valid = false;
            }
        }
        
        if ( $is_valid ) {
            $this->reset_password_form->set_rules('reset_password_p_1', 'lang:reset_password_p_1', 'required|matches[reset_password_p_2]');
            $this->reset_password_form->set_rules('reset_password_p_2', 'lang:reset_password_p_2', 'required|callback_reset_password');
            
            $this->reset_password_form->set_message('required', lang('validation_required'));
        }
        
        if ( $is_valid ) {
            $is_valid = $this->reset_password_form->run();
        }
        return $is_valid;
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
        
        $email = $this->input->post('login_email');
        if ( !login($email, $password) ) {
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
        
        try {
            $this->auth_service->forgotPasswordEmail($email);
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Forgot password request failed: '.$ex->getMessage());
            $this->forgot_password_form->set_message('forgot_password', lang('forgot_password_failed'));
            return FALSE;
        }
        return TRUE;
    }
    
    public function reset_password($password) {
        if ( $this->reset_password_form->hasErrors() ) {
            $this->reset_password_form->set_message('reset_password', '');
            return FALSE;
        }
        
        $code = $this->input->post('reset_password_code');
        
        try {
            $this->auth_service->changeForgottenPassword($password, $code);
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Forgot password change request failed: '.$ex->getMessage());
            $this->reset_password_form->set_message('reset_password', lang('reset_password_failed'));
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
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('user_model');
            
            $this->initialized = true;
        }
    }
    
    private function getActionFromUri() {
        $action = $this->uri->segment(2, null);
        return $action;
    }
    
    private function getCodeFromUri() {
        $code = $this->uri->segment(3, null);
        return $code;
    }
}
