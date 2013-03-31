<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * Authentication service.
 *
 * @author gyuszi
 */
class Auth_service {
    
    /**
     * Check if there is valid token in the session.
     * 
     * @return boolean
     */
    public function isLogged() {
        if ( loadToken() ) {
            return TRUE;
        }
        return FALSE;
    }
    
    /**
     * Authenticates into server the user identified by the given email and password.
     * If authentication succeeds a token is returned that will be stored into
     * session under 'token' key.
     * 
     * @param string $email
     * @param string $password
     * @throws Exception in case of WS error
     */
    public function authenticateEmail($email, $password) {
        try {
            $authService = new SoapClient(AUTH_SERVICE_WSDL, getSoapOptions());
            $result = $authService->authenticateEmail(array("email" => $email, "password" => $password));
            $token = $result->AuthToken;
            storeIntoSession('token', $token);
        } catch ( Exception $ex ) {
            log_message(INFO, 'Email and/or password is incorrect! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
}
