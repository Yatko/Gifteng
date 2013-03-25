<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * Authentication service.
 *
 * @author gyuszi
 */
class Auth_service {
    
    public function isLogged() {
        return $this->hasToken();
    }
    
    public function authenticateEmail($email, $password) {
        try {
            $authService = new SoapClient(AUTH_SERVICE_WSDL, getSoapOptions());
            $result = $authService->authenticateEmail(array("email" => $email, "password" => $password));
            $token = $result->AuthToken;
            $this->remember_token($token);
        } catch ( Exception $ex ) {
            log_message(INFO, 'Email and/or password is incorrect! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /* internal functions */
    
    private function remember_token($token) {
        storeToken($token);
    }
    
    private function hasToken() {
        if ( loadToken() ) {
            return TRUE;
        }
        return FALSE;
    }
}
