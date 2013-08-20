<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * Authentication service.
 *
 * @author gyuszi
 */
class Auth_service {
    
    public function __construct() {
        log_message(DEBUG, "Initializing Auth_service");
    }
    
    /**
     * Authenticates into server the user identified by the given email and password.
     * If authentication succeeds a token is returned that will be stored into
     * session under 'token' key.
     * 
     * @param string $email
     * @param string $password
     * @return string the token
     * @throws Exception in case of WS error
     */
    public function authenticateEmail($email, $password) {
        try {
            $authService = new SoapClient(AUTH_SERVICE_WSDL, getSoapOptions());
            $result = $authService->authenticateEmail(array(
                "email" => $email,
                "password" => $password
            ));
            $token = $result->AuthToken;
            storeToken($token);
            return $token;
        } catch ( Exception $ex ) {
            log_message(INFO, 'Email and/or password is incorrect! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param type $newPassword
     * @param type $code forgot password request identifier code
     * @throws Exception
     */
    public function changeForgottenPassword($newPassword, $code) {
        try {
            $authService = new SoapClient(AUTH_SERVICE_WSDL, getSoapOptions());
            $authService->changeForgottenPassword(array(
                "newPassword" => $newPassword,
                "code" => $code
            ));
        } catch ( Exception $ex ) {
            log_message(INFO, 'Change forgotten password failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * Sends a forgot password request to the server. The server generates a link
     * that will enable to chnage user password.
     * 
     * @param type $email
     * @throws Exception incorrect email or in case of WS error
     */
    public function forgotPasswordEmail($email) {
        try {
            $authService = new SoapClient(AUTH_SERVICE_WSDL, getSoapOptions());
            $authService->forgotPasswordEmail(array("email" => $email));
        } catch ( Exception $ex ) {
            log_message(INFO, 'Email is incorrect! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
}
