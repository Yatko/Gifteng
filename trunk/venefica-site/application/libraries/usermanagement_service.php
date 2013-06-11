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
    
    //**********************
    //* categories related *
    //**********************
    
    /**
     * 
     * @return array of BusinessCategory_model
     * @throws Exception
     */
    public function getAllBusinessCategories() {
        try {
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions());
            $result = $userService->getAllBusinessCategories();
            
            $categories = array();
            if ( hasField($result, 'category') && $result->category ) {
                $categories = BusinessCategory_model::convertBusinessCategories($result->category);
            }
            return $categories;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Getting all business categories failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    //************************************
    //* user crud (create/update/delete) *
    //************************************
    
    /**
     * Registers business user.
     * 
     * @param User_model $user
     * @param string $password
     * @return long the created user ID
     * @throws Exception
     */
    public function registerBusinessUser($user, $password) {
        try {
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions());
            $result = $userService->registerBusinessUser(array(
                "user" => $user,
                "password" => $password
            ));
            return $result->userId;
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
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
            $result = $userService->registerUser(array(
                "user" => $user,
                "password" => $password,
                "invitationCode" => $code
            ));
            return $result->userId;
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param User_model $user
     * @return boolean true if all required information is gathered.
     * @throws Exception
     */
    public function updateUser($user) {
        try {
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $userService->updateUser(array("user" => $user));
            return $result->complete;
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    //***************
    //* user search *
    //***************
    
    /**
     * Request user by its user name;
     * 
     * @param string $name
     * @return User_model
     * @throws Exception
     */
    public function getUserByName($name) {
        try {
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $userService->getUserByName(array("name" => $name));
            $user = $result->user;
            return User_model::convertUser($user);
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    //***************
    //* user follow *
    //***************
    
    /**
     * Follow the given user.
     * 
     * @param long $userId
     * @throws Exception if user not found or WS error
     */
    public function follow($userId) {
        try {
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions(loadToken()));
            $userService->follow(array("userId" => $userId));
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * Unfollow the given user.
     * 
     * @param long $userId
     * @throws Exception if user not found or WS error
     */
    public function unfollow($userId) {
        try {
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions(loadToken()));
            $userService->follow(array("userId" => $userId));
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * Requests the followers list - the ones that logged/current user is following.
     * 
     * @param long $userId
     * @return list of User_model
     * @throws Exception
     */
    public function getFollowers($userId) {
        try {
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $userService->getFollowers(array("userId" => $userId));
            
            $users = array();
            if ( hasField($result, 'follower') && $result->follower ) {
                $users = User_model::convertUsers($result->follower);
            }
            return $users;
        } catch ( Exception $ex ) {
            log_message(ERROR, "User followers request failed! " . $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * Returns the followings list.
     * 
     * @param long $userId
     * @return list of User_model
     * @throws Exception
     */
    public function getFollowings($userId) {
        try {
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $userService->getFollowings(array("userId" => $userId));
            
            $users = array();
            if ( hasField($result, 'following') && $result->following ) {
                $users = User_model::convertUsers($result->following);
            }
            return $users;
        } catch ( Exception $ex ) {
            log_message(ERROR, "User followings request failed! " . $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    
    
    
    
    /**
     * Gets the user from the server by the given email and stores into session
     * under 'user' key.
     * 
     * @param string $email the user email
     * @param string $token
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
    
    /**
     * refresh the user in the session by requesting server
     */
    public function refreshUser() {
        $user = $this->loadUser();
        $token = loadToken();
        $this->storeUser($user->email, $token);
    }

    /* internal functions */
    
    private function getUserByEmail($email, $token) {
        try {
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions($token));
            $result = $userService->getUserByEmail(array("email" => $email));
            $user = $result->user;
            return User_model::convertUser($user);
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
}
