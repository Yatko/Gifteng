<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * Authentication helper.
 *
 * @author gyuszi
 */

if ( ! function_exists('getSoapOptions')) {
    function getSoapOptions($token = null) {
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
    }
}

if ( ! function_exists('storeToken')) {
    function storeToken($token) {
        storeIntoSession('token', $token);
    }
}

if ( ! function_exists('loadToken')) {
    function loadToken() {
        return loadFromSession('token');
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
