<?php

if ( ! function_exists('validate_login')) {
    function validate_login() {
        if ( !isLogged() ) {
            $CI =& get_instance();
            
            $CI->load->view('templates/'.TEMPLATES.'/header');
            $CI->load->view('errors/invalid_login');
            $CI->load->view('templates/'.TEMPLATES.'/footer');
            return false;
        }
        return true;
    }
}
if ( ! function_exists('validate_user')) {
    function validate_user($user) {
        if ( $user == null ) {
            $CI =& get_instance();
            
            $CI->load->view('templates/'.TEMPLATES.'/header');
            $CI->load->view('errors/invalid_user');
            $CI->load->view('templates/'.TEMPLATES.'/footer');
            return false;
        }
        return true;
    }
}
if ( ! function_exists('validate_ad')) {
    function validate_ad($ad) {
        if ( $ad == null ) {
            $CI =& get_instance();
            
            $CI->load->view('templates/'.TEMPLATES.'/header');
            $CI->load->view('errors/invalid_ad');
            $CI->load->view('templates/'.TEMPLATES.'/footer');
            return false;
        }
        return true;
    }
}
if ( ! function_exists('validate_request')) {
    function validate_request($request) {
        if ( $request == null ) {
            $CI =& get_instance();
            
            $CI->load->view('templates/'.TEMPLATES.'/header');
            $CI->load->view('errors/invalid_request');
            $CI->load->view('templates/'.TEMPLATES.'/footer');
            return false;
        }
        return true;
    }
}
