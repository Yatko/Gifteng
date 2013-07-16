<?php

class Profile extends CI_Controller {
    
    private $initialized = false;
    
    const TAB_GIFTS = 'gifts';
    const TAB_CONNECTIONS = 'connections';
    const TAB_ACCOUNT = 'account';
    const TAB_BIO = 'bio';
    
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
    
    public function view($name = null) {
        $this->init();
        
        if ( !validate_login() ) return;
        
        try {
            $currentUser = $this->usermanagement_service->loadUser();
            
            if (
                $name != null &&
                !empty($name) &&
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
                $this->usermanagement_service->refreshUser();
                $user = $this->usermanagement_service->loadUser();
            }
        } catch ( Exception $ex ) {
            $user = null;
        }
        
        if ( !validate_user($user) ) return;
        
        $has_menu = false;
        $follow_users = null;
        $follow_ads = null;

        if ( key_exists('receiving', $_GET) ) {
            $has_menu = true;
            $is_receiving = true;
            try {
                $receivings = $this->ad_service->getUserRequestedAds($user->id, true);
            } catch ( Exception $ex ) {
                $receivings = null;
            }
        } else {
            $is_receiving = false;
            $receivings = null;
        }
        
        if ( key_exists('favorite', $_GET) ) {
            $has_menu = true;
            $is_bookmark = true;
            try {
                $bookmarks = $this->ad_service->getBookmarkedAds($user->id);
            } catch ( Exception $ex ) {
                $bookmarks = null;
            }
        } else {
            $is_bookmark = false;
            $bookmarks = null;
        }
        
        if ( key_exists('following', $_GET) ) {
            $has_menu = true;
            $is_following = true;
            try {
                $follow_users = $this->usermanagement_service->getFollowings($user->id);
                
                $follow_ads = array();
                foreach ($follow_users as $following) {
                    try {
                        $ads = $this->ad_service->getUserAds($following->id, false);
                        
                        $follow_ads[$following->id] = array();
                        foreach ($ads as $ad) {
                            array_push($follow_ads[$following->id], $ad);
                            if ( count($follow_ads[$following->id]) == Profile::ADS_NUM ) {
                                //max to display reached
                                break;
                            }
                        }
                    } catch ( Exception $ex ) {
                    }
                }
            } catch ( Exception $ex ) {
                $follow_users = null;
                $follow_ads = null;
            }
        } else {
            $is_following = false;
        }
        
        if ( key_exists('follower', $_GET) ) {
            $has_menu = true;
            $is_follower = true;
            try {
                $follow_users = $this->usermanagement_service->getFollowers($user->id);
                
                $follow_ads = array();
                foreach ($follow_users as $follower) {
                    try {
                        $ads = $this->ad_service->getUserAds($follower->id, false);
                        
                        $follow_ads[$follower->id] = array();
                        foreach ($ads as $ad) {
                            array_push($follow_ads[$follower->id], $ad);
                            if ( count($follow_ads[$follower->id]) == Profile::ADS_NUM ) {
                                //max to display reached
                                break;
                            }
                        }
                    } catch ( Exception $ex ) {
                    }
                }
            } catch ( Exception $ex ) {
                $follow_users = null;
                $follow_ads = null;
            }
        } else {
            $is_follower = false;
        }
        
        if ( key_exists('rating', $_GET) ) {
            $has_menu = true;
            $is_rating = true;
            try {
                $ratings = $this->ad_service->getReceivedRatings($user->id);
            } catch ( Exception $ex ) {
                $ratings = null;
            }
        } else {
            $is_rating = false;
            $ratings = null;
        }
        
        if ( !$has_menu || key_exists('giving', $_GET) ) {
            $has_menu = true;
            $is_giving = true;
            try {
                $givings = $this->ad_service->getUserAds($user->id, true);
            } catch ( Exception $ex ) {
                $givings = null;
            }
        } else {
            $is_giving = false;
            $givings = null;
        }
        
        $data = array();
        $data['currentUser'] = $currentUser;
        $data['user'] = $user;
        $data['givings'] = $givings;
        $data['receivings'] = $receivings;
        $data['bookmarks'] = $bookmarks;
        $data['follow_users'] = $follow_users;
        $data['follow_ads'] = $follow_ads;
        $data['ratings'] = $ratings;
        
        if ( $is_bookmark ) {
            $request_modal = $this->load->view('modal/request_create', array(), true);
        } else {
            $request_modal = '';
        }
        
        if ( $is_receiving ) {
            $receiving_modal = $this->load->view('modal/request_view', array(), true);
        } else {
            $receiving_modal = '';
        }
        
        if ( $is_giving ) {
            $giving_modal = $this->load->view('modal/request_view', array(), true);
        } else {
            $giving_modal = '';
        }
        
        $avatar_modal = $this->load->view('modal/avatar', array(), true); //permanent
        $edit_profile_modal = $this->load->view('modal/edit_profile', array(), true); //permanent
        
        $modal = $request_modal . $receiving_modal . $giving_modal . $avatar_modal . $edit_profile_modal;
        
        $this->load->view('templates/'.TEMPLATES.'/header', array('modal' => $modal));
        $this->load->view('javascript/follow');
        $this->load->view('javascript/bookmark');
        $this->load->view('javascript/message');
        $this->load->view('javascript/ad');
        $this->load->view('javascript/request');
        $this->load->view('pages/profile', $data);
        if ( $is_giving ) {
            $this->load->view('pages/profile_giving', $data);
        } else if ( isOwner($user) && $is_receiving ) {
            //only owner can see receiving list
            $this->load->view('pages/profile_receiving', $data);
        } else if ( isOwner($user) && $is_bookmark ) {
            //only owner can see bookmark list
            $this->load->view('pages/profile_bookmark', $data);
        } else if ( $is_following || $is_follower ) {
            $this->load->view('pages/profile_following', $data);
        } else if ( $is_rating ) {
            $this->load->view('pages/profile_rating', $data);
        }
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
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
            $this->usermanagement_service->refreshUser();
            $currentUser = $this->usermanagement_service->loadUser();
            
            respond_ajax(AJAX_STATUS_RESULT, $currentUser->getAvatarUrl());
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
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('ad_model');
            $this->load->model('adstatistics_model');
            $this->load->model('user_model');
            $this->load->model('userstatistics_model');
            $this->load->model('rating_model');
            $this->load->model('request_model');
            
            $this->initialized = true;
        }
    }
}