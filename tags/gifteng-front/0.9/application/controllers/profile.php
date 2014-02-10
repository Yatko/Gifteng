<?php

class Profile extends CI_Controller {
    
    private $initialized = false;
    
    const TAB_GIFTS = 'gifts';
    const TAB_CONNECTIONS = 'connections';
    const TAB_ACCOUNT = 'account';
    const TAB_BIO = 'bio';
    
    const MENU_FIRST = 'first';
    const MENU_GIVING = 'giving';
    const MENU_RECEIVING = 'receiving';
    const MENU_FAVORITE = 'favorite';
    const MENU_FOLLOWING = 'following';
    const MENU_FOLLOWER = 'follower';
    const MENU_RATING = 'rating';
    const MENU_NOTIFICATION = 'notification';
    const MENU_MESSAGE = 'message';
    const MENU_SETTING = 'setting';
    const MENU_ABOUT = 'about';
    
    const ADS_NUM = 3;
    
    public static function getActiveMenu() {
        reset($_GET);
        $active_menu = key($_GET);
        if ( $active_menu == null ) {
            $active_menu = Profile::MENU_GIVING;
        }
        return $active_menu;
    }
    
    public static function getActiveTab($active_menu) {
        $active_tab = Profile::TAB_GIFTS;
        if ( in_array($active_menu, array(Profile::MENU_GIVING, Profile::MENU_RECEIVING, Profile::MENU_FAVORITE)) ) {
            $active_tab = Profile::TAB_GIFTS;
        } else if ( in_array($active_menu, array(Profile::MENU_FOLLOWING, Profile::MENU_FOLLOWER, Profile::MENU_RATING)) ) {
            $active_tab = Profile::TAB_CONNECTIONS;
        } else if ( in_array($active_menu, array(Profile::MENU_NOTIFICATION, Profile::MENU_MESSAGE, Profile::MENU_SETTING)) ) {
            $active_tab = Profile::TAB_ACCOUNT;
        } else if ( in_array($active_menu, array(Profile::MENU_ABOUT)) ) {
            $active_tab = Profile::TAB_BIO;
        }
        return $active_tab;
    }

    public function view($name = null) {
        $this->init();
        
        if ( !validate_login() ) return;
        
        try {
            $currentUser = $this->usermanagement_service->loadUser();
            
            if (
                $name != null && !empty($name) &&
                $name != $currentUser->name &&
                $name != $currentUser->id
            ) {
                if ( is_numeric($name) ) {
                    $user = $this->usermanagement_service->getUserById($name);
                } else {
                    $user = $this->usermanagement_service->getUserByName($name);
                }
            } else {
                //needs to refresh the user as cached statistic data can reflect incorrect values
                $user = $this->usermanagement_service->refreshStatistics();
            }
        } catch ( Exception $ex ) {
            $user = null;
        }
        
        if ( !validate_user($user) ) return;
        
        if ( key_exists(Profile::MENU_FIRST, $_GET) ) {
            $this->first($user);
        } else if ( key_exists(Profile::MENU_NOTIFICATION, $_GET) ) {
            $this->notification($user);
        } else if ( key_exists(Profile::MENU_SETTING, $_GET) ) {
            $this->setting($user);
        } else if ( key_exists(Profile::MENU_MESSAGE, $_GET) ) {
            $this->message($user);
        } else if ( key_exists(Profile::MENU_FAVORITE, $_GET) ) {
            $this->favorite($user);
        } else if ( key_exists(Profile::MENU_RECEIVING, $_GET) ) {
            $this->receiving($user);
        } else if ( key_exists(Profile::MENU_RATING, $_GET) ) {
            $this->rating($user);
        } else if ( key_exists(Profile::MENU_FOLLOWING, $_GET) ) {
            $this->following($user);
        } else if ( key_exists(Profile::MENU_FOLLOWER, $_GET) ) {
            $this->follower($user);
        } else if ( key_exists(Profile::MENU_GIVING, $_GET) ) {
            $this->giving($user);
        } else {
            $this->giving($user);
        }
    }
    
    /**
     * 
     * @param User_model $user
     */
    private function first($user) {
        $modal = $this->getProfileModal();
        
        $data = array();
        $data['user'] = $user;
        
        $this->load->view('templates/'.TEMPLATES.'/header', array('modal' => $modal));
        $this->load->view('pages/profile', $data);
        $this->load->view('pages/profile_first', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }

    /**
     * Giving profile submenu.
     * @param User_model $user
     */
    private function giving($user) {
        try {
            $includeUnapproved = isOwner($user) ? true : false;
            $givings = $this->ad_service->getUserAds($user->id, 0, true, $includeUnapproved);
        } catch ( Exception $ex ) {
            $givings = null;
        }
        
        $modal = $this->getProfileModal();
        $modal .= $this->load->view('modal/request_view', array(), true);
        $modal .= $this->load->view('modal/ad_delete', array(), true);
        $modal .= $this->load->view('modal/ad_relist', array(), true);
        $modal .= $this->load->view('modal/approval', array(), true);
        
        $data = array();
        $data['user'] = $user;
        $data['givings'] = $givings;
        
        $this->load->view('templates/'.TEMPLATES.'/header', array('modal' => $modal));
        $this->load->view('javascript/follow');
        $this->load->view('javascript/bookmark');
        $this->load->view('javascript/message');
        $this->load->view('javascript/request');
        $this->load->view('javascript/social');
        $this->load->view('pages/profile', $data);
        $this->load->view('pages/profile_giving', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    /**
     * Receiving profile submenu.
     * @param User_model $user
     */
    private function receiving($user) {
        try {
            $receivings = $this->ad_service->getUserRequestedAds($user->id, true);
        } catch ( Exception $ex ) {
            $receivings = null;
        }
        
        $modal = $this->getProfileModal();
        $modal .= $this->load->view('modal/request_view', array(), true);
        $modal .= $this->load->view('modal/request_cancel', array(), true);
        $modal .= $this->load->view('modal/request_receive', array(), true);
        
        $data = array();
        $data['user'] = $user;
        $data['receivings'] = $receivings;
        
        $this->load->view('templates/'.TEMPLATES.'/header', array('modal' => $modal));
        $this->load->view('javascript/follow');
        $this->load->view('javascript/bookmark');
        $this->load->view('javascript/message');
        $this->load->view('javascript/request');
        $this->load->view('pages/profile', $data);
        $this->load->view('pages/profile_receiving', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    /**
     * Favorite/bookmark profile submenu.
     * @param User_model $user
     */
    private function favorite($user) {
        $is_owner = isOwner($user);
        $bookmarks = null;
        
        if ( $is_owner ) {
            try {
                $bookmarks = $this->ad_service->getBookmarkedAds($user->id);
            } catch ( Exception $ex ) {
            }
        }
        
        $modal = $this->getProfileModal();
        $modal .= $this->load->view('modal/request_create', array(), true);
        
        $data = array();
        $data['user'] = $user;
        $data['bookmarks'] = $bookmarks;
        
        $this->load->view('templates/'.TEMPLATES.'/header', array('modal' => $modal));
        $this->load->view('javascript/follow');
        $this->load->view('javascript/bookmark');
        $this->load->view('javascript/message');
        $this->load->view('javascript/request');
        $this->load->view('pages/profile', $data);
        if ( $is_owner ) {
            //only owner can see bookmark list
            $this->load->view('pages/profile_bookmark', $data);
        }
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    /**
     * Rating profile submenu.
     * @param User_model $user
     */
    private function rating($user) {
        try {
            $ratings = $this->ad_service->getReceivedRatings($user->id);
        } catch ( Exception $ex ) {
            $ratings = null;
        }
        
        $modal = $this->getProfileModal();
        
        $data = array();
        $data['user'] = $user;
        $data['ratings'] = $ratings;
        
        $this->load->view('templates/'.TEMPLATES.'/header', array('modal' => $modal));
        $this->load->view('javascript/follow');
        $this->load->view('javascript/bookmark');
        $this->load->view('javascript/message');
        $this->load->view('javascript/request');
        $this->load->view('pages/profile', $data);
        $this->load->view('pages/profile_rating', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    /**
     * Following profile submenu.
     * @param User_model $user
     */
    private function following($user) {
        try {
            $follow_users = $this->usermanagement_service->getFollowings($user->id);

            $follow_ads = array();
            foreach ($follow_users as $following) {
                try {
                    $ads = $this->ad_service->getUserAds($following->id, Profile::ADS_NUM, false, false);
                    $follow_ads[$following->id] = $ads;
                } catch ( Exception $ex ) {
                }
            }
        } catch ( Exception $ex ) {
            $follow_users = null;
            $follow_ads = null;
        }
        
        $modal = $this->getProfileModal();
        
        $data = array();
        $data['user'] = $user;
        $data['follow_users'] = $follow_users;
        $data['follow_ads'] = $follow_ads;
        
        $this->load->view('templates/'.TEMPLATES.'/header', array('modal' => $modal));
        $this->load->view('javascript/follow');
        $this->load->view('javascript/bookmark');
        $this->load->view('javascript/message');
        $this->load->view('javascript/request');
        $this->load->view('pages/profile', $data);
        $this->load->view('pages/profile_following', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    /**
     * Follower profile submenu.
     * @param User_model $user
     */
    private function follower($user) {
        try {
            $follow_users = $this->usermanagement_service->getFollowers($user->id);

            $follow_ads = array();
            foreach ($follow_users as $follower) {
                try {
                    $ads = $this->ad_service->getUserAds($follower->id, Profile::ADS_NUM, false, false);
                    $follow_ads[$follower->id] = $ads;
                } catch ( Exception $ex ) {
                }
            }
        } catch ( Exception $ex ) {
            $follow_users = null;
            $follow_ads = null;
        }
        
        $modal = $this->getProfileModal();
        
        $data = array();
        $data['user'] = $user;
        $data['follow_users'] = $follow_users;
        $data['follow_ads'] = $follow_ads;
        
        $this->load->view('templates/'.TEMPLATES.'/header', array('modal' => $modal));
        $this->load->view('javascript/follow');
        $this->load->view('javascript/bookmark');
        $this->load->view('javascript/message');
        $this->load->view('javascript/request');
        $this->load->view('pages/profile', $data);
        $this->load->view('pages/profile_following', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    /**
     * Notification profile submenu.
     * @param User_model $user
     */
    private function notification($user) {
        $is_owner = isOwner($user);
        $user_setting = null;
        
        if ( $is_owner ) {
            if ( $_POST ) {
                $user_setting = new UserSetting_model();
                $user_setting->notifiableTypes = hasElement($_POST, 'notifiableTypes') ? $this->input->post('notifiableTypes') : null;

                try {
                    $this->usermanagement_service->saveUserSetting($user_setting);
                } catch ( Exception $ex ) {
                }

                redirect('/profile?notification');
            }

            try {
                $user_setting = $this->usermanagement_service->getUserSetting();
            } catch ( Exception $ex ) {
            }
        }
        
        $modal = $this->getProfileModal();
        
        $data = array();
        $data['user'] = $user;
        $data['user_setting'] = $user_setting;
        
        $this->lang->load('notification');
        
        $this->load->view('templates/'.TEMPLATES.'/header', array('modal' => $modal));
        $this->load->view('javascript/follow');
        $this->load->view('javascript/bookmark');
        $this->load->view('javascript/message');
        $this->load->view('javascript/request');
        $this->load->view('pages/profile', $data);
        if ( $is_owner ) {
            $this->load->view('pages/profile_notification', $data);
        }
        $this->load->view('templates/'.TEMPLATES.'/footer');
        
    }
    
    /**
     * Message profile submenu.
     * @param User_model $user
     */
    private function message($user) {
        $is_owner = isOwner($user);
        $messages = null;
        $request = null;
        $ad = null;
        $request_messages = null;
        $currentUser = $this->usermanagement_service->loadUser();
        
        if ( $is_owner ) {
            if ( count($_GET) > 1 ) {
                $requestId = key(array_slice($_GET, 1, 1, true));
                
                if ( key_exists('delete', $_GET) ) {
                    try {
                        $this->message_service->hideRequestMessages($requestId);
                    } catch ( Exception $ex ) {
                    }
                    
                    redirect('/profile?message');
                }
                
                
                try {
                    $request_messages = $this->message_service->getMessagesByRequest($requestId);
                } catch ( Exception $ex ) {
                    $request_messages = array();
                }
                
                if ( sizeof($request_messages) > 0 ) {
                    try {
                        $request = $this->ad_service->getRequestById($requestId);
                    } catch ( Exception $ex ) {
                    }

                    try {
                        $ad = $this->ad_service->getAdById($request->adId, false);
                    } catch ( Exception $ex ) {
                    }

                    try {
                        $user = $this->usermanagement_service->refreshStatistics();
                    } catch ( Exception $ex ) {
                    }
                }
            }
            
            try {
                $messages = $this->message_service->getLastMessagePerRequest();
            } catch ( Exception $ex ) {
            }
        }
        
        $modal = $this->getProfileModal();
        
        $data = array();
        $data['user'] = $user;
        $data['messages'] = $messages;
        $data['request'] = $request;
        $data['ad'] = $ad;
        $data['request_messages'] = $request_messages;
        $data['currentUser'] = $currentUser;
        
        $this->load->view('templates/'.TEMPLATES.'/header', array('modal' => $modal));
        $this->load->view('javascript/follow');
        $this->load->view('javascript/bookmark');
        $this->load->view('javascript/message');
        $this->load->view('javascript/request');
        $this->load->view('pages/profile', $data);
        if ( $is_owner ) {
            $this->load->view('pages/profile_message', $data);
        }
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    /**
     * Setting profile submenu.
     * @param User_model $user
     */
    private function setting($user) {
        $is_owner = isOwner($user);
        $networks = array();
        
        if ( $is_owner ) {
            try {
                $networks = $this->usermanagement_service->getConnectedSocialNetworks();
            } catch ( Exception $ex ) {
            }
        }
        
        $modal = $this->getProfileModal();

        $data = array();
        $data['user'] = $user;
        $data['networks'] = $networks;
        
        $this->load->view('templates/'.TEMPLATES.'/header', array('modal' => $modal));
        $this->load->view('javascript/social');
        $this->load->view('pages/profile', $data);
        if ( $is_owner ) {
            $this->load->view('pages/profile_setting', $data);
        }
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    //ajax call
    
    public function change_avatar() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_FILES ) {
            return;
        }
        
        $field = 'avatar_image';
        if ( !isset($_FILES[$field]) || $_FILES[$field]['error'] == 4 ) {
            //no file selected for upload
            return;
        }
        
        $config['upload_path'] = TEMP_FOLDER;
        $config['allowed_types'] = 'gif|jpg|png|jpeg';
        $config['encrypt_name'] = true;
        $config['max_size'] = UPLOAD_FILE_MAX_SIZE;
        
        $this->load->library('upload', $config);
        if ( !$this->upload->do_upload($field) ) {
            $error = $this->upload->display_errors();
            respond_ajax(AJAX_STATUS_ERROR, $error);
            return;
        }
        
        $data = $this->upload->data();
        $image_file_name = $data['file_name'];
        $image = Image_model::createImageModel($image_file_name);
        
        try {
            $currentUser = $this->usermanagement_service->loadUser();
            $currentUser->avatar = $image;
            
            $this->usermanagement_service->updateUser($currentUser);
            $currentUser = $this->usermanagement_service->refreshUser();
            
            respond_ajax(AJAX_STATUS_RESULT, $currentUser->getAvatarUrl(SELF_USER_IMAGE_SIZE));
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->getMessage());
            respond_ajax(AJAX_STATUS_ERROR, 'Something went wrong !');
        }
    }

    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            $this->lang->load('profile');
            
            $this->load->library('ad_service');
            $this->load->library('usermanagement_service');
            $this->load->library('message_service');
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('ad_model');
            $this->load->model('adstatistics_model');
            $this->load->model('user_model');
            $this->load->model('userstatistics_model');
            $this->load->model('usersetting_model');
            $this->load->model('rating_model');
            $this->load->model('request_model');
            $this->load->model('message_model');
            $this->load->model('approval_model');
            
            clear_cache();
            
            $this->initialized = true;
        }
    }
    
    private function getProfileModal() {
        $avatar_modal = $this->load->view('modal/avatar', array(), true); //permanent
        $edit_profile_modal = $this->load->view('modal/edit_profile', array(), true); //permanent
        
        $modal = $avatar_modal . $edit_profile_modal;
        return $modal;
    }
}