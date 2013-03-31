<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * User management service
 *
 * @author gyuszi
 */
class Usermanagement_service {
    
    /**
     * Gets the user from the server by the given email and stores into session
     * under 'user' key.
     * 
     * @param string $email the user email
     * @throws Exception in case of WS invocation error
     */
    public function storeUser($email) {
        $user = $this->getUserByEmail($email, loadToken());
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
