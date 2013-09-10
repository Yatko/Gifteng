<?php

/**
 * Description of registration
 *
 * @author gyuszi
 */
class Registration extends CI_Controller {
    
    private $initialized = false;
    private $auto_login = true;
    
    public function user() {
        $this->init();
        
        $extra_data = array();
        
        $this->registerUser($extra_data);
        
        $data = $extra_data;
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/registration_member', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    public function business() {
        $this->init();
        
        $extra_data = array();
        
        $this->registerBusiness($extra_data);
        
        $data = $extra_data;
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/registration_business', $data);
        $this->load->view('pages/footer');
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    public function verify($code) {
        $this->init();
        
        if ( $code == null || trim($code) == '' ) {
            redirect('/profile');
        }
        
        try {
            $this->usermanagement_service->verifyUser($code);
        } catch ( Exception $ex ) {
        }
        
        redirect('/profile');
    }
    
    private function registerUser(&$extra_data) {
        $this->load->library('form_validation', null, 'registration_form');
        $this->registration_form->set_error_delimiters('<div class="error">', '</div>');
        $this->registration_form->set_rules('registration_firstname', 'lang:u_registration_firstname', 'trim|required|alpha');
        $this->registration_form->set_rules('registration_lastname', 'lang:u_registration_lastname', 'trim|required|alpha');
        $this->registration_form->set_rules('registration_email', 'lang:u_registration_email', 'trim|required|valid_email');
        $this->registration_form->set_rules('registration_password', 'lang:u_registration_password', 'required|callback_register_user');
        
        $this->registration_form->set_message('alpha', lang('validation_alpha'));
        $this->registration_form->set_message('required', lang('validation_required'));
        $this->registration_form->set_message('valid_email', lang('validation_valid_email'));
        
        $is_valid = $this->registration_form->run();
        if ( $is_valid ) {
            //form data valid after post
            if ( $this->auto_login ) {
                //logging in into system automatically
                $email = $this->input->post('registration_email');
                $password = $this->input->post('registration_password');

                if ( login($email, $password, false) ) {
                    redirect('/profile?first');
                } else {
                    log_message(ERROR, 'Newly created business cannot auto login into system');
                }
            } else {
                redirect('/browse');
            }
        } elseif ( $_POST ) {
            //form data not valid after post
            $code = $this->input->post('invitation_code');
            $this->session->set_flashdata('invitation_code', $code);
            
            $extra_data['invitation_code'] = $code;
        } else {
            //direct access of the page
            $code = $this->session->flashdata('invitation_code');
            if ( $code ) {
                $extra_data['invitation_code'] = $code;
            } else {
                redirect('/invitation/verify/1');
            }
        }
    }
    
    private function registerBusiness(&$extra_data) {
        $this->load->library('form_validation', null, 'registration_form');
        $this->registration_form->set_error_delimiters('<div class="error">', '</div>');
        
        $this->registration_form->set_rules('businessName', 'lang:b_registration_businessName', 'trim|required');
        $this->registration_form->set_rules('contactName');
        $this->registration_form->set_rules('phoneNumber');
        $this->registration_form->set_rules('email', 'lang:b_registration_email', 'trim|required|valid_email');
        $this->registration_form->set_rules('zipCode', 'lang:b_registration_zipCode', 'trim|required');
        $this->registration_form->set_rules('businessCategory', 'lang:b_registration_category', 'required');
        $this->registration_form->set_rules('password_1', 'lang:b_registration_password_1', 'required|matches[password_2]');
        $this->registration_form->set_rules('password_2', 'lang:b_registration_password_2', 'required|callback_register_business');
        
        $this->registration_form->set_message('required', lang('validation_required'));
        $this->registration_form->set_message('valid_email', lang('validation_valid_email'));
        
        $is_valid = $this->registration_form->run();
        if ( $is_valid ) {
            //form data valid after post
            if ( $this->auto_login ) {
                //logging in into system automatically
                $email = $this->input->post('email');
                $password = $this->input->post('password_1');

                if ( login($email, $password, false) ) {
                    redirect('/profile');
                } else {
                    log_message(ERROR, 'Newly created business cannot auto login into system');
                }
            } else {
                $this->session->set_flashdata('registration_success', true);
                redirect('/registration/business');
            }
        } elseif ( $_POST ) {
            //form data not valid after post
        } else {
            //direct access of the page
            
            $registration_success = $this->session->flashdata('registration_success');
            if ( $registration_success ) {
                $extra_data['registration_success'] = true;
                return;
            }
        }
        
        try {
            $categories = $this->usermanagement_service->getAllBusinessCategories();
        } catch ( Exception $ex ) {
            $categories = array();
        }
        
        $extra_data['categories'] = $categories;
    }
    
    //callback
    public function register_user($password) {
        if ( $this->registration_form->hasErrors() ) {
            //$this->registration_form->set_message('register_user', 'Cannot register user!');
            $this->registration_form->set_message('register_user', '');
            return FALSE;
        }
        
        if ( $password == null || strlen($password) < PASSWORD_MIN_SIZE ) {
            $this->registration_form->set_message('register_user', 'Your password needs to be minimum ' . PASSWORD_MIN_SIZE . ' characters');
            return FALSE;
        }
        
        $code = $this->input->post('invitation_code');
        
        $user = new User_model();
        $user->firstName = $this->input->post('registration_firstname');
        $user->lastName = $this->input->post('registration_lastname');
        $user->email = $this->input->post('registration_email');
        
        try {
            $this->usermanagement_service->registerUser($user, $password, $code);
        } catch ( Exception $ex ) {
            log_message(ERROR, 'User registration failed! '.$ex->getMessage());
            
            if ( getField($ex->detail, "UserAlreadyExistsError") != null ) {
                $this->registration_form->set_message('register_user', lang('u_registration_failed_user_already_exists'));
            } else {
                $this->registration_form->set_message('register_user', lang('u_registration_failed'));
            }
            return FALSE;
        }
        return TRUE;
    }
    
    //callback
    public function register_business($password) {
        if ( $this->registration_form->hasErrors() ) {
            $this->registration_form->set_message('register_business', '');
            return FALSE;
        }
        
        $address = new Address_model();
        $address->zipCode = $this->input->post('zipCode');
        
        $addresses = array();
        array_push($addresses, $address);
        
        $user = new User_model();
        $user->businessName = $this->input->post('businessName');
        $user->contactName = $this->input->post('contactName');
        $user->phoneNumber = $this->input->post('phoneNumber');
        $user->email = $this->input->post('email');
        $user->addresses = $addresses;
        $user->businessCategoryId = $this->input->post('businessCategory');
        
        try {
            $this->usermanagement_service->registerBusinessUser($user, $password);
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Business user registration failed! '.$ex->getMessage());
            $this->registration_form->set_message('register_business', lang('b_registration_failed'));
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
            
            $this->load->library('usermanagement_service');
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('user_model');
            $this->load->model('userstatistics_model');
            $this->load->model('businesscategory_model');
            
            clear_cache();
            
            $this->initialized = true;
        }
    }
}

?>
