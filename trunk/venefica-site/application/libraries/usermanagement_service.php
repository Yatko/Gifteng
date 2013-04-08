<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * User management service
 *
 * @author gyuszi
 */
class Usermanagement_service {
    
    public function __construct() {
        log_message(DEBUG, "Initializing Usermanagement_service");
    }
    
    /**
     * Invokes the user registration on WS.
     * 
     * @param User_model $user
     * @param string $password
     * @param string $code
     * @return long
     * @throws Exception in case of WS error
     */
    public function registerUser($user, $password, $code) {
        try {
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions());
            $result = $userService->registerUser(array("user" => $user, "password" => $password, "invitationCode" => $code));
            return $result->userId;
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * Gets the user from the server by the given email and stores into session
     * under 'user' key.
     * 
     * @param string $email the user email
     * @throws Exception in case of WS invocation error
     */
    public function storeUser($email, $token) {
        $user = $this->getUserByEmail($email, $token);
        storeIntoSession('user', $user);
    }
    
    /**
     * Loads the user from the session that was stored previously.
     * 
     * @return User model
     */
    public function loadUser() {
        return loadFromSession('user');
    }


    /* internal functions */
    
    private function getUserByEmail($email, $token) {
        try {
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions($token));
            $result = $userService->getUserByEmail(array("email" => $email));
            return $result->user;
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
}
