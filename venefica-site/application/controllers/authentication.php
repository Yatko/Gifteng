<?php

class Authentication extends CI_Controller {
    
    private static $ACTION_FORGOT_PASSWORD = "forgot";
    private static $ACTION_REQUEST_INVITATION = "request";
    private static $ACTION_VERIFY_INVITATION = "verify";
    
    private $initialized = false;
    
    public function view($extra_data = array()) {
        $this->init();
        
        $data = array();
        $data['selected_tab'] = $this->getSelectedTab();
        $data['action'] = $this->getAction();
        $data['step'] = $this->getStep();
        $data['is_logged'] = $this->auth_service->isLogged();
        $data['user'] = $this->usermanagement_service->loadUser();
        
        $data = array_merge($data, $extra_data);
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/authentication', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    /**
     * Method is invoked upon login related POST (or direct url access).
     */
    public function login() {
        $this->init();
        $action = $this->getAction();
        
        if ( $action == self::$ACTION_FORGOT_PASSWORD ) {
            $is_valid = $this->login_forgot();
        } else {
            $is_valid = $this->login_login();
        }
        
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
    
    /**
     * Method is invoked upon invitation related POST (or direct url access).
     */
    public function invitation() {
        $this->init();
        $action = $this->getAction();
        $extra_data = array();
        
        if ( $action == self::$ACTION_REQUEST_INVITATION ) {
            $is_valid = $this->process_invitation_request($extra_data);
        } else if ( $action == self::$ACTION_VERIFY_INVITATION ) {
            $is_valid = $this->process_invitation_verify($extra_data);
        } else {
            log_message(ERROR, "Invalid invitation action ($action)!");
            $is_valid = FALSE;
        }
        
        if ( $is_valid == FALSE ) {
            $this->view($extra_data);
        }
    }
    
    
    
    
    
    
    
    // invitation tab related
    
    private function process_invitation_request(&$extra_data) {
        $step = $this->getStep();
        $is_valid = $this->invitation_request($step);
        if ( $is_valid ) {
            if ( $step == 1 ) {
                $email = $this->input->post('invitation_email');
                $this->session->set_flashdata('invitation_email', $email);
                
                redirect('/authentication/invitation/'.self::$ACTION_REQUEST_INVITATION.'/2');
            } elseif ( $step == 2 ) {
                $email = $this->input->post('invitation_email');
                $this->session->set_flashdata('invitation_email', $email);
                
                redirect('/authentication/invitation/'.self::$ACTION_REQUEST_INVITATION.'/3');
            } else {
                log_message(INFO, "Invalid step ($step)!");
                redirect('/authentication/invitation/'.self::$ACTION_REQUEST_INVITATION.'/1');
            }
        } elseif ( $_POST ) {
            if ( $step == 1 ) {
                //nothing special here
            } elseif ( $step == 2 ) {
                $email = $this->input->post('invitation_email');
                $this->session->set_flashdata('invitation_email', $email);
                
                $extra_data['invitation_email'] = $email;
            } else {
                log_message(INFO, "Invalid step ($step)!");
                redirect('/authentication/invitation/'.self::$ACTION_REQUEST_INVITATION.'/1');
            }
        } else {
            if ( $step == 1 ) {
                //nothing special here
            } elseif ( $step == 2 ) {
                $email = $this->session->flashdata('invitation_email');
                if ( $email ) {
                    $extra_data['invitation_email'] = $email;
                } else {
                    redirect('/authentication/invitation/'.self::$ACTION_REQUEST_INVITATION.'/1');
                }
            } elseif ( $step == 3 ) {
                $email = $this->session->flashdata('invitation_email');
                if ( $email ) {
                    //success and thank you message
                } else {
                    redirect('/authentication/invitation/'.self::$ACTION_REQUEST_INVITATION.'/1');
                }
            } else {
                log_message(INFO, "Invalid step ($step)!");
                redirect('/authentication/invitation/'.self::$ACTION_REQUEST_INVITATION.'/1');
            }
        }
        return $is_valid;
    }
    
    private function process_invitation_verify(&$extra_data) {
        $step = $this->getStep();
        $is_valid = $this->invitation_verify($step);
        if ( $is_valid ) {
            if ( $step == 1 ) {
                $code = $this->input->post('invitation_code');
                $this->session->set_flashdata('invitation_code', $code);
                
                redirect('/authentication/invitation/'.self::$ACTION_VERIFY_INVITATION.'/2');
            } else {
                log_message(INFO, "Invalid step ($step)!");
                redirect('/authentication/invitation/'.self::$ACTION_VERIFY_INVITATION.'/1');
            }
        } elseif ( $_POST ) {
            if ( $step == 1 ) {
                //nothing special here
            } elseif ( $step == 2 ) {
                $code = $this->input->post('invitation_code');
                $this->session->set_flashdata('invitation_code', $code);
                
                redirect('/registration');
            } else {
                log_message(INFO, "Invalid step ($step)!");
                redirect('/authentication/invitation/'.self::$ACTION_VERIFY_INVITATION.'/1');
            }
        } else {
            if ( $step == 1 ) {
                //nothing special here
            } elseif ( $step == 2 ) {
                $code = $this->session->flashdata('invitation_code');
                if ( $code ) {
                    $extra_data['invitation_code'] = $code;
                } else {
                    redirect('/authentication/invitation/'.self::$ACTION_VERIFY_INVITATION.'/1');
                }
            } else {
                log_message(INFO, "Invalid step ($step)!");
                redirect('/authentication/invitation/'.self::$ACTION_VERIFY_INVITATION.'/1');
            }
        }
        return $is_valid;
    }
    
    private function invitation_request($step) {
        $this->load->library('form_validation', null, 'request_invitation_form');
        $this->request_invitation_form->set_error_delimiters('<div class="error">', '</div>');
        if ( $step == 1 ) {
            $this->request_invitation_form->set_rules('invitation_email', 'lang:invitation_email', 'trim|required|valid_email');
        } elseif ( $step == 2 ) {
            $this->request_invitation_form->set_rules('invitation_email', 'lang:invitation_email', 'trim|required|valid_email|callback_request_invitation');
            //$this->request_invitation_form->set_rules('invitation_zipcode', 'lang:invitation_zipcode', 'trim|required');
            //$this->request_invitation_form->set_rules('invitation_source', 'lang:invitation_source', 'trim|required');
        }
        $is_valid = $this->request_invitation_form->run();
        return $is_valid;
    }
    
    private function invitation_verify($step) {
        $this->load->library('form_validation', null, 'verify_invitation_form');
        $this->verify_invitation_form->set_error_delimiters('<div class="error">', '</div>');
        if ( $step == 1 ) {
            $this->verify_invitation_form->set_rules('invitation_code', 'lang:invitation_code', 'trim|required|callback_verify_invitation');
        } elseif ( $step == 2 ) {
            //nothing special here
        }
        $is_valid = $this->verify_invitation_form->run();
        return $is_valid;
    }
    
    /**
     * Internal validation method, that will tries to create an invitation.
     * 
     * @param string $email
     * @return boolean
     */
    public function request_invitation($email) {
        if ( $this->request_invitation_form->hasErrors() ) {
            return FALSE;
        }
        
        $this->load->model('invitation_model');
        $this->invitation_model->email = $this->input->post('invitation_email');
        $this->invitation_model->zipCode = $this->input->post('invitation_zipcode');
        $this->invitation_model->source = $this->input->post('invitation_source');
        $this->invitation_model->userType = $this->input->post('invitation_usertype');

        try {
            $this->load->library('invitation_service');
            $this->invitation_service->requestInvitation($this->invitation_model);
        } catch ( Exception $ex ) {
            //TODO: create language key
            $this->request_invitation_form->set_message('request_invitation', 'Cannot request invitation! '.$ex->getMessage());
            return FALSE;
        }
        return TRUE;
    }
    
    /**
     * 
     * @param string $code invitation code
     * @return boolean
     */
    public function verify_invitation($code) {
        if ( $this->verify_invitation_form->hasErrors() ) {
            return FALSE;
        }
        
        try {
            $this->load->library('invitation_service');
            $valid = $this->invitation_service->isInvitationValid($code);
            if ( !$valid ) {
                //TODO: create language key
                $this->verify_invitation_form->set_message('verify_invitation', 'Invitation is not valid!');
                return FALSE;
            }
        } catch ( Exception $ex ) {
            //TODO: create language key
            $this->verify_invitation_form->set_message('verify_invitation', 'Cannot verify invitation! '.$ex->getMessage());
            return FALSE;
        }
        return TRUE;
    }
    
    
    
    
    
    // login tab related
    
    private function login_forgot() {
        $this->load->library('form_validation', null, 'forgot_password_form');
        $this->forgot_password_form->set_error_delimiters('<div class="error">', '</div>');
        $this->forgot_password_form->set_rules('forgot_password_email', 'lang:forgot_password_email', 'trim|required|valid_email');
        $is_valid = $this->forgot_password_form->run();
        return $is_valid;
    }
    
    private function login_login() {
        $this->load->library('form_validation', null, 'login_form');
        $this->login_form->set_error_delimiters('<div class="error">', '</div>');
        $this->login_form->set_rules('login_email', 'lang:authentication_email', 'trim|required|valid_email');
        $this->login_form->set_rules('login_password', 'lang:authentication_password', 'required|callback_authorize_email_password');
        $this->login_form->set_rules('login_remember_me', 'lang:authentication_remember_me', '');
        $is_valid = $this->login_form->run();
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
            return FALSE;
        }
        
        try {
            $email = $this->input->post('login_email');
            $token = $this->auth_service->authenticateEmail($email, $password);
            $this->usermanagement_service->storeUser($email, $token);
        } catch ( Exception $ex ) {
            //TODO: create language key
            $this->login_form->set_message('authorize_email_password', 'Email and/or password is incorrect! '.$ex->getMessage());
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
    
    private function getSelectedTab() {
        $selected_tab = $this->uri->segment(2, null);
        return $selected_tab;
    }
    
    private function getAction() {
        $action = $this->uri->segment(3, null);
        return $action;
    }
    
    private function getStep() {
        $step = $this->uri->segment(4, null);
        return $step;
    }
}
