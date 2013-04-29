<?php

class Profile extends CI_Controller {
    
    private $initialized = false;
    
    public function view($name = null) {
        $this->init();
        
        $user = null;
        $ads = null;
        $receivings = null;
        $givings = null;
        $favorites = null;
        $followers = null;
        $followings = null;
        $reviews = null;
        
        $givings_num = 0;
        $receivings_num = 0;
        $favorites_num = 0;
        $followers_num = 0;
        $followings_num = 0;
        $reviews_num = 0;
        
        if ( isLogged() ) {
            try {
                if ( !empty($name) ) {
                    $user = $this->usermanagement_service->getUserByName($name);
                } else {
                    $user = $this->usermanagement_service->loadUser();
                }
            } catch ( Exception $ex ) {
            }
            
            try {
                $ads = $this->ad_service->getUserAds($user->id);
                if ( $ads ) {
                    $receivings = array();
                    $givings = array();
                    foreach ( $ads as $ad ) {
                        if ( $ad->wanted === TRUE ) {
                            array_push($receivings, $ad);
                        } else {
                            array_push($givings, $ad);
                        }
                    }
                }
            } catch ( Exception $ex ) {
            }
            
            try {
                $favorites = $this->ad_service->getFavorites($user->id);
            } catch ( Exception $ex ) {
            }
            
            try {
                $followers = $this->usermanagement_service->getFollowers($user->id);
            } catch ( Exception $ex ) {
            }
            try {
                $followings = $this->usermanagement_service->getFollowings($user->id);
            } catch ( Exception $ex ) {
            }
            
            try {
                $reviews = $this->usermanagement_service->getReviews($user->id);
            } catch ( Exception $ex ) {
            }
        }
        
        if ( $receivings ) {
            $receivings_num = count($receivings);
        }
        if ( $givings ) {
            $givings_num = count($givings);
        }
        if ( $favorites ) {
            $favorites_num = count($favorites);
        }
        if ( $followers ) {
            $followers_num = count($followers);
        }
        if ( $followings ) {
            $followings_num = count($followings);
        }
        if ( $reviews ) {
            $reviews_num = count($reviews);
        }
        
        $data = array();
        $data['isLogged'] = isLogged();
        $data['is_ajax'] = false;
        $data['user'] = $user;
        $data['ads'] = $ads;
        $data['receivings'] = $receivings;
        $data['givings'] = $givings;
        $data['followers'] = $followers;
        $data['followings'] = $followings;
        $data['reviews'] = $reviews;
        
        $data['givings_num'] = $givings_num;
        $data['receivings_num'] = $receivings_num;
        $data['favorites_num'] = $favorites_num;
        $data['followers_num'] = $followers_num;
        $data['followings_num'] = $followings_num;
        $data['reviews_num'] = $reviews_num;
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/profile', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    public function ajax() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        }
        
        $data = array();
        $data['isLogged'] = isLogged();
        $data['is_ajax'] = true;
        
        $this->load->view('pages/profile', $data);
    }
    
    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            $this->lang->load('profile');
            
            $this->load->library('auth_service');
            $this->load->library('ad_service');
            $this->load->library('usermanagement_service');
            
            $this->load->model('image_model');
            $this->load->model('ad_model');
            $this->load->model('user_model');
            $this->load->model('review_model');
            
            $this->initialized = true;
        }
    }
}