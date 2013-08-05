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
    
    //*****************
    //* user statistics *
    //*****************
    
    public function getStatistics($userId) {
        try {
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions(loadToken()));
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
    
    public function getUsers() {
        try {
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $userService->getUsers();
            
            $users = array();
            if ( hasField($result, 'user') && $result->user ) {
                $users = User_model::convertUsers($result->user);
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
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions(loadToken()));
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
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions(loadToken()));
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
            $userService->unfollow(array("userId" => $userId));
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
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions(loadToken()));
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
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions(loadToken()));
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
            $userService = new SoapClient(USER_SERVICE_WSDL, getSoapOptions(loadToken()));
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
