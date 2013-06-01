<?php

class Post extends CI_Controller {
    
    private $initialized = false;
    
    const STEP_START = 'start';
    const STEP_ONLINE = 'online';
    const STEP_LOCATION = 'location';
    const STEP_PREVIEW = 'preview';
    const STEP_POST = 'post';
    
    public function view() {
        $this->init();
        
        if ( !validate_login() ) return;
        
        $user = $this->usermanagement_service->loadUser();
        $current_step = $this->getCurrentStep();
        $next_step = $this->getNextStep($current_step);
        $is_valid = $this->process($current_step);
        
        if ( $is_valid ) {
            //form data valid after post
            $this->keepPostData($current_step);
            $step = $next_step;
        } elseif ( $_POST ) {
            //form data not valid after post
            $this->keepPostData($current_step);
            $step = $current_step;
        } else {
            //direct access of the page
            $step = $current_step;
        }
        
        $data = array();
        $data['user'] = $user;
        $data['step'] = $step;
        
        if ( $step == Post::STEP_POST ) {
            $error = $this->create_ad($user);
            $data['error'] = $error;
        }
        
        if ( $step == Post::STEP_START ) {
            try {
                $categories = $this->ad_service->getAllCategories();
            } catch ( Exception $ex ) {
                $categories = array();
            }
            
            $data['categories'] = $categories;
        }
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        if ( $user->businessAccount ) {
            $this->load->view('pages/business_post', $data);
        } else {
            $this->load->view('pages/member_post', $data);
        }
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            $this->lang->load('post');
            
            $this->load->library('auth_service');
            $this->load->library('ad_service');
            $this->load->library('usermanagement_service');
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('ad_model');
            $this->load->model('user_model');
            $this->load->model('category_model');
            
            $this->initialized = true;
        }
    }
    
    private function getCurrentStep() {
        if ( $_POST ) {
            return $this->input->post('step');
        } else {
            $step = $this->session->flashdata('step');
            if ( $step ) {
                return $step;
            }
        }
        return Post::STEP_START;
    }
    
    private function getNextStep($current_step) {
        if ( $current_step == Post::STEP_START ) {
            if ( $_POST ) {
                if ( $this->input->post('place') == Ad_model::PLACE_ONLINE ) {
                    return Post::STEP_ONLINE;
                } else {
                    return Post::STEP_LOCATION;
                }
            } else {
                return Post::STEP_START;
            }
        }
//        elseif ( $current_step == Post::STEP_ONLINE || $current_step == Post::STEP_LOCATION ) {
//            return Post::STEP_PREVIEW;
//        } elseif ( $current_step == Post::STEP_PREVIEW ) {
//            return Post::STEP_POST;
//        }
        elseif ( $current_step == Post::STEP_ONLINE || $current_step == Post::STEP_LOCATION ) {
            return Post::STEP_POST;
        }
        return null;
    }
    
    private function keepPostData($current_step) {
        $this->session->keep_flashdata('post_'.Post::STEP_START);
        $this->session->keep_flashdata('post_'.Post::STEP_ONLINE);
        $this->session->keep_flashdata('post_'.Post::STEP_LOCATION);
        $this->session->set_flashdata('post_'.$current_step, $_POST);
    }
    
    private function process($current_step) {
        if ( $current_step == Post::STEP_START ) {
            return $this->post_start();
        } elseif ( $current_step == Post::STEP_ONLINE ) {
            return $this->post_online();
        } elseif ( $current_step == Post::STEP_LOCATION ) {
            return $this->post_location();
        }
//        elseif ( $current_step == Post::STEP_PREVIEW ) {
//            return $this->post_preview();
//        }
        return true;
    }

    private function post_start() {
        $this->load->library('form_validation', null, 'post_form');
        $this->post_form->set_error_delimiters('<div class="error">', '</div>');
        
        $this->post_form->set_rules('title', 'lang:post_title', 'trim|required');
        $this->post_form->set_rules('subtitle');
        $this->post_form->set_rules('price', 'lang:post_price', 'trim|integer');
        $this->post_form->set_rules('category', 'lang:post_category', 'required');
        $this->post_form->set_rules('place');
        $this->post_form->set_rules('availableAt');
        $this->post_form->set_rules('expiresAt');
        $this->post_form->set_rules('expires');
        
        $this->post_form->set_message('required', lang('validation_required'));
        
        $is_valid = $this->post_form->run();
        return $is_valid;
    }
    
    private function post_online() {
        $this->load->library('form_validation', null, 'post_form');
        $this->post_form->set_error_delimiters('<div class="error">', '</div>');
        
        $this->post_form->set_rules('description');
        $this->post_form->set_rules('quantity', 'lang:post_quantity', 'trim|required|is_natural_no_zero');
        $this->post_form->set_rules('promocode');
        $this->post_form->set_rules('website');
        
        $this->post_form->set_message('required', lang('validation_required'));
        $this->post_form->set_message('is_natural_no_zero', lang('validation_is_natural_no_zero'));
        
        $is_valid = $this->post_form->run();
        return $is_valid;
    }
    
    private function post_location() {
        $this->load->library('form_validation', null, 'post_form');
        $this->post_form->set_error_delimiters('<div class="error">', '</div>');
        
        $this->post_form->set_rules('description');
        $this->post_form->set_rules('available_days');
        $this->post_form->set_rules('availableFromTime');
        $this->post_form->set_rules('availableToTime');
        $this->post_form->set_rules('availableAllDay');
        $this->post_form->set_rules('website');
        
        $is_valid = $this->post_form->run();
        return $is_valid;
    }
    
    private function create_ad($user) {
        $start_array = $this->session->flashdata("post_".Post::STEP_START);
        $online_array = $this->session->flashdata("post_".Post::STEP_ONLINE);
        $location_array = $this->session->flashdata("post_".Post::STEP_LOCATION);
        
        $ad = new Ad_model();
        $ad->type = Ad_model::ADTYPE_BUSINESS;
        $ad->title = $start_array['title'];
        $ad->subtitle = $start_array['subtitle'];
        $ad->categoryId = $start_array['category'];
        $ad->price = hasElement($start_array, 'price') ? getElement($start_array, 'price') * 1 : 0;
        $ad->place = $start_array['place'];
        $ad->availableAt = $start_array['availableAt']; //TODO: needs convert to long/Date
        $ad->expiresAt = $start_array['expiresAt']; //TODO: needs convert to long/Date
        $ad->expires = hasElement($start_array, 'expires') ? true : false;
        if ( $start_array['place'] == Ad_model::PLACE_ONLINE ) {
            $ad->description = $online_array['description'];
            $ad->quantity = $online_array['quantity'];
            $ad->promoCode = $online_array['promocode'];
            $ad->website = $online_array['website'];
        } elseif ( $start_array['place'] == Ad_model::PLACE_LOCATION ) {
            $addresses = $location_array['address'];
            $quantities = $location_array['quantity'];
            $address = $user->addresses[$addresses[0]];
            $quantity = $quantities[0];
            
            $ad->description = $location_array['description'];
            $ad->needsReservation = hasElement($location_array, 'needsReservation') ? true : false;
            $ad->availableFromTime = $location_array['availableFromTime']; //TODO: needs convert to long/Date
            $ad->availableToTime = $location_array['availableToTime']; //TODO: needs convert to long/Date
            $ad->availableAllDay = hasElement($location_array, 'availableAllDay') ? true : false;
            $ad->availableDays = $location_array['availableDays'];
            $ad->address = $address;
            $ad->quantity = $quantity;
        }
        
        //print_r($this->session->all_userdata());
        //print_r($ad);
        
        try {
            $adId = $this->ad_service->placeAd($ad);
            redirect("/view/".$adId);
        } catch ( Exception $ex ) {
            return $ex->getMessage();
        }
    }
}