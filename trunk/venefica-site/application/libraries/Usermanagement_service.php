<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * User management service
 *
 * @author gyuszi
 */
class Usermanagement_service {
    
    public function storeUser($email) {
        $user = $this->getUserByEmail($email, loadToken());
        storeIntoSession('user', $user);
    }
    
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
