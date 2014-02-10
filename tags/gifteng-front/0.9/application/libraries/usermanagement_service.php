<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * User management service
 *
 * @author gyuszi
 */
class Usermanagement_service {
    
    var $userService;
    var $token;
    
    public function __construct() {
        log_message(DEBUG, "Initializing Usermanagement_service");
    }
    
    //*****************************
    //* user verification related *
    //*****************************
    
    /**
     * 
     * @param string $code
     * @throws Exception
     */
    public function verifyUser($code) {
        try {
            $userService = $this->getService();
            $userService->verifyUser(array("code" => $code));
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @throws Exception
     */
    public function resendVerification() {
        try {
            $userService = $this->getService();
            $userService->resendVerification();
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
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
            $userService = $this->getService();
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
            $userService = $this->getService();
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
            $userService = $this->getService();
            $result = $userService->registerUser(array(
                "user" => $user,
                "password" => $password,
                "invitationCode" => $code
            ));
            return $result->userId;
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw $ex;
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
            $userService = $this->getService();
            $result = $userService->updateUser(array("user" => $user));
            return $result->complete;
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * Put the currently logged user into deactivated state.
     * 
     * @throws Exception
     */
    public function deactivateUser() {
        try {
            $userService = $this->getService();
            $userService->deactivateUser();
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }


    //*****************
    //* user statistics *
    //*****************
    
    public function getStatistics($userId) {
        try {
            $userService = $this->getService();
            $result = $userService->getStatistics(array("userId" => $userId));
            $statistics = UserStatistics_model::convertUserStatistics($result->statistics);
            return $statistics;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'User statistics (id: '.$userId.') request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    //***************
    //* user search *
    //***************
    
    /**
     * Returns a list of users having the highest score.
     * 
     * @param type $numberUsers
     * @return type
     * @throws Exception
     */
    public function getTopUsers($numberUsers) {
        try {
            $userService = $this->getService();
            $result = $userService->getTopUsers(array("numberUsers" => $numberUsers));
            
            $users = array();
            if ( hasField($result, 'users') && $result->users ) {
                $users = User_model::convertUsers($result->users);
            }
            return $users;
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * Request user by its user name;
     * 
     * @param string $name
     * @return User_model
     * @throws Exception
     */
    public function getUserByName($name) {
        try {
            $userService = $this->getService();
            $result = $userService->getUserByName(array("name" => $name));
            $user = $result->user;
            return User_model::convertUser($user);
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $userId
     * @return User_model
     * @throws Exception
     */
    public function getUserById($userId) {
        try {
            $userService = $this->getService();
            $result = $userService->getUserById(array("userId" => $userId));
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
     * @return UserStatistics_model
     * @throws Exception if user not found or WS error
     */
    public function follow($userId) {
        try {
            $userService = $this->getService();
            $result = $userService->follow(array("userId" => $userId));
            $statistics = UserStatistics_model::convertUserStatistics($result->statistics);
            return $statistics;
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * Unfollow the given user.
     * 
     * @param long $userId
     * @return UserStatistics_model
     * @throws Exception if user not found or WS error
     */
    public function unfollow($userId) {
        try {
            $userService = $this->getService();
            $result = $userService->unfollow(array("userId" => $userId));
            $statistics = UserStatistics_model::convertUserStatistics($result->statistics);
            return $statistics;
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
            $userService = $this->getService();
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
            $userService = $this->getService();
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
    
    //*****************
    //* user settings *
    //*****************
    
    /**
     * 
     * @return UserSetting_model
     * @throws Exception
     */
    public function getUserSetting() {
        try {
            $userService = $this->getService();
            $result = $userService->getUserSetting();
            
            $userSetting = UserSetting_model::convertUserSetting($result->setting);
            return $userSetting;
        } catch ( Exception $ex ) {
            log_message(ERROR, "User setting request failed! " . $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param UserSetting_model $userSetting
     * @throws Exception
     */
    public function saveUserSetting($userSetting) {
        try {
            $userService = $this->getService();
            $userService->saveUserSetting(array("setting" => $userSetting));
        } catch ( Exception $ex ) {
            log_message(ERROR, "Saving user setting failed! " . $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    //******************
    //* social network *
    //******************
    
    /**
     * 
     * @return array of string
     * @throws Exception
     */
    public function getConnectedSocialNetworks() {
        try {
            $userService = $this->getService();
            $result = $userService->getConnectedSocialNetworks();
            
            $networks = array();
            if ( hasField($result, 'network') && $result->network ) {
                if ( is_array($result->network) && count($result->network) > 0 ) {
                    foreach ( $result->network as $network ) {
                        array_push($networks, $network);
                    }
                } elseif ( !is_empty($result->network) ) {
                    $network = $result->network;
                    array_push($networks, $network);
                }
            }
            return $networks;
        } catch ( Exception $ex ) {
            log_message(ERROR, "Getting connected social network list failed! " . $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    
    
    /**
     * Gets the user from the server by the given email and stores into session.
     * 
     * @param string $email the user email
     * @param string $token
     * @throws Exception in case of WS invocation error
     */
    public function storeUserByEmail($email, $token) {
        $user = $this->getUserByEmail($email, $token);
        $this->storeUser($user);
    }
    
    /**
     * Stores the given user into session under 'user' key.
     * 
     * @param User_model $user
     */
    public function storeUser($user) {
        storeIntoSession('user', $user);
    }
    
    /**
     * Loads the user from the session that was stored previously.
     * 
     * @return User_model
     */
    public function loadUser() {
        return loadFromSession('user');
    }
    
    /**
     * Refresh the user in the session by requesting server.
     * 
     * @return User_model
     */
    public function refreshUser() {
        $user = $this->loadUser();
        $token = loadToken();
        $this->storeUserByEmail($user->email, $token);
        return $this->loadUser();
    }
    
    /**
     * Refresh the currently logged in user statistics.
     * 
     * @return User_model
     */
    public function refreshStatistics() {
        $user = $this->loadUser();
        
        try {
            $statistics = $this->getStatistics($user->id);
            $user->statistics = $statistics;
            $this->storeUser($user);
        } catch ( Exception $ex ) {
        }
        
        return $user;
    }
    
    /* internal functions */
    
    private function getUserByEmail($email, $token) {
        try {
            $userService = $this->getService($token);
            $result = $userService->getUserByEmail(array("email" => $email));
            $user = $result->user;
            return User_model::convertUser($user);
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    private function getService($token = null) {
        if ( $this->userService == null || $this->token != $token ) {
            if ( $token == null ) {
                $token = loadToken();
            }
            
            $this->token = $token;
            $this->userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions($this->token));
        }
        return $this->userService;
    }
}