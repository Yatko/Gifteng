<?php

class Member_edit_profile extends CI_Controller {
    
    const AJAX_STATUS_RESULT = 'result';
    const AJAX_STATUS_ERROR = 'error';
    
    private $initialized = false;
    
    var $user;
    
    public function view() {
        $this->init();
        
        if ( !validate_login() ) return;
        
        $qs = $this->input->server('QUERY_STRING');
        $this->user = $this->usermanagement_service->loadUser();
        if ( $this->user->businessAccount ) {
            redirect("/edit_profile/business".(trim($qs) == '' ? '' : '?'.$qs));
        }
        
        if ( key_exists('modal', $_GET) ) {
            $is_modal = true;
        } else {
            $is_modal = false;
        }
        
        $result = $this->process();
        if ( key_exists(Member_edit_profile::AJAX_STATUS_RESULT, $result) ) {
            redirect('/profile');
        }
        
        $data = array();
        $data['user'] = $this->user;
        $data['is_modal'] = $is_modal;
        
        if ( $is_modal ) {
            $this->load->view('pages/member_edit_profile', $data);
        } else {
            $this->load->view('templates/'.TEMPLATES.'/header');
            $this->load->view('pages/member_edit_profile', $data);
            $this->load->view('templates/'.TEMPLATES.'/footer');
        }
    }
    
    public function ajax() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_POST ) {
            return;
        }
        
        $this->user = $this->usermanagement_service->loadUser();
        if ( $this->user->businessAccount ) {
            return;
        }
        
        $result = $this->process();
        $this->respondAjax($result);
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
            
            $this->initialized = true;
        }
    }
    
    private function respondAjax($responseArray) {
        $obj = array();
        foreach ( $responseArray as $status => $result ) {
            $obj[$status] = $result;
        }
        
        $data['obj'] = $obj;
        $this->load->view('json', $data);
    }
    
    private function process() {
        $is_valid = $this->post_form();
        if ( $is_valid ) {
            //form data valid after post
            
            $zipCode = $this->input->post('zipCode');
            $location = getLocationByZipCode($zipCode);
            
            $user = $this->usermanagement_service->loadUser();
            $user->firstName = $this->input->post('firstName');
            $user->lastName = $this->input->post('lastName');
            $user->about = $this->input->post('about');
            $user->address->zipCode = $zipCode;
            $user->address->longitude = $location['longitude'];
            $user->address->latitude = $location['latitude'];
            
            try {
                $this->usermanagement_service->updateUser($user);
                $this->usermanagement_service->refreshUser();
                $this->user = $this->usermanagement_service->loadUser();
                
                return array(
                    Member_edit_profile::AJAX_STATUS_RESULT => 'OK'
                );
            } catch ( Exception $ex ) {
                log_message(ERROR, $ex->getMessage());
                return array(
                    Member_edit_profile::AJAX_STATUS_ERROR => 'Something went wrong when updating user!'
                );
            }
        } elseif ( $_POST ) {
            //form data not valid after post
            return array(
                Member_edit_profile::AJAX_STATUS_ERROR => $this->edit_profile_form->error_string()
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