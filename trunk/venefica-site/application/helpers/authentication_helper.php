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
     * @param boolean $remember_me
     * @return boolean true is login success
     */
    function login($email, $password, $remember_me) {
        $CI =& get_instance();
        $CI->load->library('auth_service');
        $CI->load->library('usermanagement_service');
        
        try {
            $token = $CI->auth_service->authenticateEmail($email, $password);
            $CI->usermanagement_service->storeUser($email, $token);
            
            if ( $remember_me ) {
                $CI->load->library('remember_me');
                $user = $CI->usermanagement_service->loadUser();
                $CI->remember_me->setCookie($user->id);
            }
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Email and/or password is incorrect: '.$ex->getMessage());
            return FALSE;
        }
        return TRUE;
    }
}
if ( !function_exists('logout') ) {
    function logout() {
        destroySession();
        
        $CI =& get_instance();
        $CI->load->library('remember_me');
        $CI->remember_me->deleteCookie();
    }
}
if ( ! function_exists('isLogged')) {
    /**
     * Check if there is valid token in the session.
     * 
     * @return boolean
     */
    function isLogged() {
        $CI =& get_instance();
        $CI->load->library('remember_me');
        $cookie_user = $CI->remember_me->verifyCookie();
        $token = loadToken();
        
        if ( $cookie_user || $token ) {
            if ( empty($token) ) {
                //TODO: here should refresh the user by the cookie user ID
                return false;
            }
            return TRUE;
        }
        return FALSE;
    }
}


if ( ! function_exists('isOwner')) {
    /**
     * Checks the given user if is the one logged in.
     * 
     * @param User_model or long $user 
     * @return boolean
     */
    function isOwner($user) {
        if ( !isLogged() ) {
            return false;
        } else if ( $user == null ) {
            return false;
        }
        
        $CI =& get_instance();
        $CI->load->library('usermanagement_service');
        
        $currentUser = $CI->usermanagement_service->loadUser();
        
        if ( $user instanceof User_model ) {
            if ( $currentUser->id == $user->id ) {
                return true;
            }
        } elseif (is_numeric ($user) ) {
            if ( $currentUser->id == $user ) {
                return true;
            }
        }
        return false;
    }
}


if ( ! function_exists('isBusinessAccount')) {
    /**
     * Checks the given user if is business typed account. If the user is not present
     * will look at the actual logged in user flag.
     * 
     * @param User_model $user
     * @return boolean
     */
    function isBusinessAccount($user = null) {
        if ( $user == null ) {
            $CI =& get_instance();
            $CI->load->library('usermanagement_service');
            $user = $CI->usermanagement_service->loadUser();
        }
        
        $businessAccount = $user->businessAccount;
        if ( $businessAccount == null ) {
            $businessAccount = false;
        }
        
        return $businessAccount;
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

if ( ! function_exists('removeFromSession')) {
    function removeFromSession($key) {
        $CI =& get_instance();
        $CI->load->library('session');
        return $CI->session->unset_userdata($key);
    }
}

if ( ! function_exists('destroySession')) {
    function destroySession() {
        $CI =& get_instance();
        $CI->load->library('session');
        return $CI->session->sess_destroy();
    }
}

if ( ! function_exists('logSession')) {
    function logSession() {
        $CI =& get_instance();
        $CI->load->library('session');
        log_message(ERROR, 'Session data: ' . print_r($CI->session->all_userdata(), true));
    }
}
