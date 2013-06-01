<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * Authentication helper.
 * 
 * NOTE: the PHP version 5.2 has a bug (fixed in 5.3) that the custom http headers
 * are not sent. This is a serious issue as the authorization token should be sent
 * to the venefica server on every WS request. The PHP dependency of the venefica
 * web-application for this reason is at least 5.3.
 *
 * @author gyuszi
 */

if ( ! function_exists('getSoapOptions')) {
    function getSoapOptions($token = null) {
        ini_set('soap.wsdl_cache_enabled', '0');
        if ( $token != null ) {
            ini_set('user_agent', "PHP-SOAP/".PHP_VERSION."\r\n"."AuthToken: ".$token);
        }
        return array();
        
        /**
        if ( $token == null ) {
            $soap_options = array('cache_wsdl' => WSDL_CACHE_NONE);
        } else {
            $soap_options = array(
                //'cache_wsdl' => WSDL_CACHE_NONE, //NOTE: with this it does not sends the token
                //'cache_wsdl' => 'WSDL_CACHE_NONE', //this works
                'cache_wsdl' => '0',
                'stream_context' => stream_context_create(array(
                    'http' => array('header' => "AuthToken: $token")
                ))
            );
        }
        return $soap_options;
        /**/
    }
}


if ( ! function_exists('login')) {
    /**
     * Authenticates and login the user by its given email/pass combination.
     * 
     * @param string $email
     * @param string $password
     * @return boolean true is login success
     */
    function login($email, $password) {
        $CI =& get_instance();
        $CI->load->library('auth_service');
        $CI->load->library('usermanagement_service');
        
        try {
            $token = $CI->auth_service->authenticateEmail($email, $password);
            $CI->usermanagement_service->storeUser($email, $token);
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Email and/or password is incorrect: '.$ex->getMessage());
            return FALSE;
        }
        return TRUE;
    }
}
if ( ! function_exists('isLogged')) {
    /**
     * Check if there is valid token in the session.
     * 
     * @return boolean
     */
    function isLogged() {
        if ( loadToken() ) {
            return TRUE;
        }
        return FALSE;
    }
}


if ( ! function_exists('loadToken')) {
    function loadToken() {
        return loadFromSession('token');
    }
}
if ( ! function_exists('storeToken')) {
    function storeToken($token) {
        log_message(INFO, 'Storing token: '.$token);
        storeIntoSession('token', $token);
    }
}

if ( ! function_exists('storeIntoSession')) {
    function storeIntoSession($key, $value) {
        $CI =& get_instance();
        $CI->load->library('session');
        $CI->session->set_userdata($key, $value);
    }
}

if ( ! function_exists('loadFromSession')) {
    function loadFromSession($key) {
        $CI =& get_instance();
        $CI->load->library('session');
        return $CI->session->userdata($key);
    }
}

if ( ! function_exists('destroySession')) {
    function destroySession() {
        $CI =& get_instance();
        $CI->load->library('session');
        return $CI->session->sess_destroy();
    }
}
