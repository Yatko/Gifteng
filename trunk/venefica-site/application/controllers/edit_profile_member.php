<?php

class Edit_profile_member extends CI_Controller {
    
    private $initialized = false;
    
    public function view() {
        $this->init();
        
        if ( !validate_login() ) return;
        
        if ( isBusinessAccount() ) {
            safe_redirect("/edit_profile/business");
        }
        
        $is_modal = key_exists('modal', $_GET);
        $result = $this->process();
        
        if ( key_exists(AJAX_STATUS_RESULT, $result) ) {
            redirect('/profile');
        }
        
        $currentUser = $this->usermanagement_service->loadUser();
        
        $data = array();
        $data['currentUser'] = $currentUser;
        $data['is_modal'] = $is_modal;
        
        if ( $is_modal ) {
            $this->load->view('pages/edit_profile_member', $data);
        } else {
            $this->load->view('templates/'.TEMPLATES.'/header');
            $this->load->view('pages/edit_profile_member', $data);
            $this->load->view('templates/'.TEMPLATES.'/footer');
        }
    }
    
    public function ajax() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_POST ) {
            return;
        } else if ( isBusinessAccount() ) {
            return;
        }
        
        $result = $this->process();
        respond_ajax_array($result);
    }
    
    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            $this->lang->load('edit_profile');
            
            $this->load->library('usermanagement_service');
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('user_model');
            $this->load->model('userstatistics_model');
            
            $this->initialized = true;
        }
    }
    
    private function process() {
        $is_valid = $this->post_form();
        if ( $is_valid ) {
            //form data valid after post
            
            $currentUser = $this->usermanagement_service->loadUser();
            $zipCode = $this->input->post('zipCode');
            //$location = getLocationByZipCode($zipCode);
            $addressLocation = getAddressByZipCode($zipCode);
            
            if ( $currentUser->address == null ) {
                $address = new Address_model();
            } else {
                $address = $currentUser->address;
            }
            
            $address->zipCode = $zipCode;
            //$address->longitude = $location['longitude'];
            //$address->latitude = $location['latitude'];
            $address->longitude = $addressLocation->longitude;
            $address->latitude = $addressLocation->latitude;
            $address->city = $addressLocation->city;
            $address->state = $addressLocation->state;
            $address->stateAbbreviation = $addressLocation->stateAbbreviation;
            
            $currentUser->firstName = $this->input->post('firstName');
            $currentUser->lastName = $this->input->post('lastName');
            $currentUser->about = $this->input->post('about');
            $currentUser->address = $address;
            
            try {
                $this->usermanagement_service->updateUser($currentUser);
                $this->usermanagement_service->refreshUser();
                
                return array(
                    AJAX_STATUS_RESULT => 'OK'
                );
            } catch ( Exception $ex ) {
                log_message(ERROR, $ex->getMessage());
                return array(
                    AJAX_STATUS_ERROR => 'Something went wrong when updating user!'
                );
            }
        } elseif ( $_POST ) {
            //form data not valid after post
            return array(
                AJAX_STATUS_ERROR => $this->edit_profile_form->error_string()
            );
        } else {
            //direct access of the page
            return array();
        }
    }

    private function post_form() {
        $this->load->library('form_validation', null, 'edit_profile_form');
        $this->edit_profile_form->set_error_delimiters('<div class="error">', '</div>');
        
        $this->edit_profile_form->set_rules('firstName', 'lang:m_edit_profile_firstName', 'trim|required');
        $this->edit_profile_form->set_rules('lastName', 'lang:m_edit_profile_lastName', 'trim|required');
        $this->edit_profile_form->set_rules('about');
        $this->edit_profile_form->set_rules('zipCode', 'lang:m_edit_profile_zipCode', 'trim|required');
        $this->edit_profile_form->set_rules('email', 'lang:m_edit_profile_email', 'trim|required|valid_email');
        
        $this->edit_profile_form->set_message('required', lang('validation_required'));
        $this->edit_profile_form->set_message('valid_email', lang('validation_valid_email'));
        
        $is_valid = $this->edit_profile_form->run();
        return $is_valid;
    }
}