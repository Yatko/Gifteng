<?php

/**
 * Description of registration
 *
 * @author gyuszi
 */
class Registration extends CI_Controller {
    
    private static $ACTION_VERIFY_INVITATION = "verify";
    
    private $initialized = false;
    
    public function view() {
        $data = array();
        $extra_data = array();
        
        $this->init();
        $this->register($extra_data);
        
        $data = array_merge($data, $extra_data);
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/registration', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    private function register(&$extra_data) {
        $this->load->library('form_validation', null, 'registration_form');
        $this->registration_form->set_error_delimiters('<div class="error">', '</div>');
        $this->registration_form->set_rules('registration_firstname', 'lang:registration_firstname', 'trim|required|alpha');
        $this->registration_form->set_rules('registration_lastname', 'lang:registration_lastname', 'trim|required|alpha');
        $this->registration_form->set_rules('registration_email', 'lang:registration_email', 'trim|required|valid_email');
        $this->registration_form->set_rules('registration_password', 'lang:registration_password', 'required|callback_register_user');
        
        $this->registration_form->set_message('alpha', lang('validation_alpha'));
        $this->registration_form->set_message('required', lang('validation_required'));
        $this->registration_form->set_message('valid_email', lang('validation_valid_email'));
        
        $is_valid = $this->registration_form->run();
        if ( $is_valid ) {
            redirect('/browse');
        } elseif ( $_POST ) {
            $code = $this->input->post('invitation_code');
            $this->session->set_flashdata('invitation_code', $code);
            
            $extra_data['invitation_code'] = $code;
        } else {
            $code = $this->session->flashdata('invitation_code');
            if ( $code ) {
                $extra_data['invitation_code'] = $code;
            } else {
                redirect('/authentication/invitation/'.self::$ACTION_VERIFY_INVITATION.'/1');
            }
        }
    }
    
    public function register_user($password) {
        if ( $this->registration_form->hasErrors() ) {
            //$this->registration_form->set_message('register_user', 'Cannot register user!');
            $this->registration_form->set_message('register_user', '');
            return FALSE;
        }
        
        $code = $this->input->post('invitation_code');
        
        $user = new User_model();
        $user->firstName = $this->input->post('registration_firstname');
        $user->lastName = $this->input->post('registration_lastname');
        $user->email = $this->input->post('registration_email');
        
        try {
            $this->load->library('usermanagement_service');
            $this->usermanagement_service->registerUser($user, $password, $code);
        } catch ( Exception $ex ) {
            log_message(ERROR, 'User registration failed! '.$ex->getMessage());
            $this->registration_form->set_message('register_user', lang('registration_failed'));
            return FALSE;
        }
        return TRUE;
    }
    
    // internal functions
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            $this->lang->load('registration');
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('user_model');
            
            $this->initialized = true;
        }
    }
}

?>
