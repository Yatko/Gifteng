<?php

if ( ! function_exists('validate_login')) {
    function validate_login() {
        $CI =& get_instance();
        $is_logged = isLogged();
        
        if ( $is_logged ) {
            $CI->load->library('usermanagement_service');
            $currentUser = $CI->usermanagement_service->loadUser();
            if ( is_empty($currentUser) ) {
                $is_logged = false;
            }
        }
        
        if ( $is_logged == false ) {
            $is_modal = key_exists('modal', $_GET);
            
            $data = array();
            $data['is_modal'] = $is_modal;
            
            if ( $is_modal ) {
                $CI->load->view('errors/invalid_login', $data);
            } else {
                $CI->load->view('templates/'.TEMPLATES.'/header');
                $CI->load->view('errors/invalid_login', $data);
                $CI->load->view('templates/'.TEMPLATES.'/footer');
            }
            return false;
        }
        return true;
    }
}
if ( ! function_exists('validate_user')) {
    function validate_user($user) {
        if ( $user == null ) {
            $is_modal = key_exists('modal', $_GET);
            $CI =& get_instance();
            
            $data = array();
            $data['is_modal'] = $is_modal;
            
            if ( $is_modal ) {
                $CI->load->view('errors/invalid_user', $data);
            } else {
                $CI->load->view('templates/'.TEMPLATES.'/header');
                $CI->load->view('errors/invalid_user', $data);
                $CI->load->view('templates/'.TEMPLATES.'/footer');
            }
            return false;
        }
        return true;
    }
}
if ( ! function_exists('validate_ad')) {
    function validate_ad($ad) {
        if ( $ad == null ) {
            $is_modal = key_exists('modal', $_GET);
            $CI =& get_instance();
            
            $data = array();
            $data['is_modal'] = $is_modal;
            
            if ( $is_modal ) {
                $CI->load->view('errors/invalid_ad', $data);
            } else {
                $CI->load->view('templates/'.TEMPLATES.'/header');
                $CI->load->view('errors/invalid_ad', $data);
                $CI->load->view('templates/'.TEMPLATES.'/footer');
            }
            return false;
        }
        return true;
    }
}
if ( ! function_exists('validate_request')) {
    function validate_request($request) {
        if ( $request == null ) {
            $is_modal = key_exists('modal', $_GET);
            $CI =& get_instance();
            
            $data = array();
            $data['is_modal'] = $is_modal;
            
            if ( $is_modal ) {
                $CI->load->view('errors/invalid_request', $data);
            } else {
                $CI->load->view('templates/'.TEMPLATES.'/header');
                $CI->load->view('errors/invalid_request', $data);
                $CI->load->view('templates/'.TEMPLATES.'/footer');
            }
            return false;
        }
        return true;
    }
}
