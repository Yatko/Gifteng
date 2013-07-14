<?php

class Post_member extends CI_Controller {
    
    private $initialized = false;
    
    const STEP_START = 'start';
    const STEP_DETAILS = 'details';
    const STEP_MAP = 'map';
    const STEP_PREVIEW = 'preview';
    const STEP_POST = 'post';
    
    const SESSION_KEY_POST = 'post_';
    const SESSION_KEY_FILES = 'files_';
    
    var $steps = array(
        Post_member::STEP_START,
        Post_member::STEP_DETAILS,
        Post_member::STEP_MAP,
        Post_member::STEP_PREVIEW,
        Post_member::STEP_POST
    );
    
    var $user;
    var $unique_id;
    
    public function view() {
        $this->init();
        
        if ( !validate_login() ) return;
        
        if ( isBusinessAccount() ) {
            safe_redirect("/post/business");
        }
        
        $is_modal = key_exists('modal', $_GET);
        $is_first = ($_POST || $_FILES ? false : true);
        
        if ( $is_first ) {
            $this->unique_id = uniqid('gifteng_');
        } else {
            $this->unique_id = $this->input->post('unique_id');
        }
        
        $current_step = $this->getCurrentStep();
        $next_step = $this->getNextStep($current_step);
        $is_valid = $this->process($current_step);
        
        if ( $is_valid ) {
            //form data valid after post
            $step = $next_step;
            if ( $step == Post_member::STEP_POST ) {
                //do not keep flash data on last step - to avoid retransmit upon page reload
            } else {
                $this->storePostData($current_step);
            }
        } elseif ( $_POST ) {
            //form data not valid after post
            $step = $current_step;
            $this->storePostData($current_step);
        } else {
            //direct access of the page
            $step = $current_step;
        }
        
        $data = array();
        $data['user'] = $this->user;
        $data['step'] = $step;
        $data['is_modal'] = $is_modal;
        $data['is_first'] = $is_first;
        $data['unique_id'] = $this->unique_id;
        
        if ( $step == Post_member::STEP_START ) {
            $start_array = $this->loadPostData(Post_member::STEP_START);
            $post_start_array = $start_array[Post_member::SESSION_KEY_POST];
            $files_start_array = $start_array[Post_member::SESSION_KEY_FILES];
            
            $ad_image = $this->getImageFileName($post_start_array, $files_start_array, 'ad_image');
            if ( is_empty($ad_image) ) {
                $ad_image = null;
            }
            
            $data['ad_image'] = $ad_image;
        } else if ( $step == Post_member::STEP_DETAILS ) {
            try {
                $categories = $this->ad_service->getAllCategories();
            } catch ( Exception $ex ) {
                $categories = array();
            }
            
            $details_array = $this->loadPostData(Post_member::STEP_DETAILS);
            $post_details_array = $details_array[Post_member::SESSION_KEY_POST];
            $files_details_array = $details_array[Post_member::SESSION_KEY_FILES];
            
            $data['title'] = $post_details_array['title'];
            $data['description'] = $post_details_array['description'];
            $data['category'] = $post_details_array['category'];
            $data['price'] = $post_details_array['price'];
            $data['zipCode'] = $post_details_array['zipCode'];
            $data['pickUp'] = hasElement($post_details_array, 'pickUp') ? $post_details_array['pickUp'] : '0';
            $data['freeShipping'] = hasElement($post_details_array, 'freeShipping') ? $post_details_array['freeShipping'] : '0';
            $data['categories'] = $categories;
        } else if ( $step == Post_member::STEP_MAP ) {
            $details_array = $this->loadPostData(Post_member::STEP_DETAILS);
            $post_details_array = $details_array[Post_member::SESSION_KEY_POST];
            $files_details_array = $details_array[Post_member::SESSION_KEY_FILES];
            
            $map_array = $this->loadPostData(Post_member::STEP_MAP);
            $post_map_array = $map_array[Post_member::SESSION_KEY_POST];
            //$files_map_array = $map_array[Post_member::SESSION_KEY_FILES];
            
            $zipCode = $post_details_array['zipCode'];
            $longitude = $post_map_array['longitude'];
            $latitude = $post_map_array['latitude'];
            
            if ( empty($longitude) || empty($latitude) ) {
                $location = getLocationByZipCode($zipCode);
                $marker_longitude = $location['longitude'];
                $marker_latitude = $location['latitude'];
            } else {
                $marker_longitude = $longitude;
                $marker_latitude = $latitude;
            }
            
            if ( empty($marker_longitude) ) $marker_longitude = 0;
            if ( empty($marker_latitude) ) $marker_latitude = 0;
            
            $data['marker_longitude'] = $marker_longitude;
            $data['marker_latitude'] = $marker_latitude;
            $data['longitude'] = $longitude;
            $data['latitude'] = $latitude;
        } else if ( $step == Post_member::STEP_PREVIEW ) {
            try {
                $categories = $this->ad_service->getAllCategories();
            } catch ( Exception $ex ) {
                $categories = array();
            }
            
            $start_array = $this->loadPostData(Post_member::STEP_START);
            $post_start_array = $start_array[Post_member::SESSION_KEY_POST];
            $files_start_array = $start_array[Post_member::SESSION_KEY_FILES];
            
            $details_array = $this->loadPostData(Post_member::STEP_DETAILS);
            $post_details_array = $details_array[Post_member::SESSION_KEY_POST];
            $files_details_array = $details_array[Post_member::SESSION_KEY_FILES];
            
            $ad_image = $this->getImageFileName($post_start_array, $files_start_array, 'ad_image');
            if ( is_empty($ad_image) ) {
                $ad_image = null;
            }
            
            $category = '';
            $categoryId = $post_details_array['category'];
            foreach ($categories as $cat) {
                if ( $categoryId == $cat->id ) {
                    $category = $cat->name;
                    break;
                }
            }
            
            $data['ad_image'] = $ad_image;
            $data['title'] = $post_details_array['title'];
            $data['description'] = $post_details_array['description'];
            $data['price'] = $post_details_array['price'];
            $data['pickUp'] = hasElement($post_details_array, 'pickUp') ? $post_details_array['pickUp'] : '0';
            $data['freeShipping'] = hasElement($post_details_array, 'freeShipping') ? $post_details_array['freeShipping'] : '0';
            $data['categoryId'] = $categoryId;
            $data['category'] = $category;
        } else if ( $step == Post_member::STEP_POST ) {
            $error = $this->create_ad();
            $data['error'] = $error;
        }
        
        if ( $is_modal ) {
            if ( $is_first ) {
                $this->load->view('javascript/map');
            }
            
            $this->load->view('pages/post_member', $data);
        } else {
            $this->load->view('templates/'.TEMPLATES.'/header');
            $this->load->view('javascript/map');
            $this->load->view('pages/post_member', $data);
            $this->load->view('templates/'.TEMPLATES.'/footer');
        }
    }
    
    // internal
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            $this->lang->load('post');
            
            $this->load->library('ad_service');
            $this->load->library('usermanagement_service');
            
            $this->load->model('image_model');
            $this->load->model('address_model');
            $this->load->model('ad_model');
            $this->load->model('adstatistics_model');
            $this->load->model('user_model');
            $this->load->model('userstatistics_model');
            $this->load->model('category_model');
            
            $this->initialized = true;
        }
    }
    
    private function getCurrentStep() {
        if ( $_POST ) {
            return $this->input->post('step');
        } else {
            $step = $this->session->flashdata($this->unique_id . '_step');
            if ( $step ) {
                return $step;
            }
        }
        return Post_member::STEP_START;
    }
    
    private function getNextStep($current_step) {
        if ( $_POST ) {
            $next_step = $this->input->post('next_step');
            if ( !empty($next_step) ) {
                return $next_step;
            }
        }
        
        if ( $current_step == Post_member::STEP_START ) {
            return Post_member::STEP_DETAILS;
        } elseif ( $current_step == Post_member::STEP_DETAILS ) {
            return Post_member::STEP_MAP;
        } elseif ( $current_step == Post_member::STEP_MAP ) {
            return Post_member::STEP_PREVIEW;
        } elseif ( $current_step == Post_member::STEP_PREVIEW ) {
            return Post_member::STEP_POST;
        }
        return null;
    }
    
    private function storePostData($current_step) {
        foreach ( $this->steps as $step ) {
            $this->session->keep_flashdata($this->unique_id . '_post_' . $step);
            $this->session->keep_flashdata($this->unique_id . '_files_' . $step);
        }
        
        if ( $current_step != null ) {
            $this->session->set_flashdata($this->unique_id . '_post_' . $current_step, $_POST);
            $this->session->set_flashdata($this->unique_id . '_files_' . $current_step, $_FILES);
        }
    }
    
    private function loadPostData($step) {
        $post_array = $this->session->flashdata($this->unique_id . '_post_' . $step);
        $files_array = $this->session->flashdata($this->unique_id . '_files_' . $step);
        
        return array(
            Post_member::SESSION_KEY_POST => $post_array,
            Post_member::SESSION_KEY_FILES => $files_array
        );
    }
    
    private function process($current_step) {
        if ( $current_step == Post_member::STEP_START ) {
            return $this->post_start();
        } elseif ( $current_step == Post_member::STEP_DETAILS ) {
            return $this->post_details();
        } elseif ( $current_step == Post_member::STEP_MAP ) {
            return $this->post_map();
        } elseif ( $current_step == Post_member::STEP_PREVIEW ) {
            return $this->post_preview();
        }
        return true;
    }
    
    private function post_start() {
        $this->load->library('form_validation', null, 'post_form');
        $this->post_form->set_error_delimiters('<div class="error">', '</div>');
        
        $this->post_form->set_rules('ad_image_posted');
        
        if ( empty($_FILES['ad_image']['name']) ) {
            if ( empty($_POST['ad_image_posted']) ) {
                $this->post_form->set_rules('ad_image', 'lang:post_image', 'required');
            }
        } else {
            $this->post_form->set_rules('ad_image', 'lang:post_image', 'callback_file_upload[ad_image]');
        }
        
        $is_valid = $this->post_form->run();
        return $is_valid;
    }
    
    private function post_details() {
        $this->load->library('form_validation', null, 'post_form');
        $this->post_form->set_error_delimiters('<div class="error">', '</div>');
        
        $this->post_form->set_rules('title', 'lang:post_title', 'trim|required');
        $this->post_form->set_rules('price', 'lang:post_price', 'trim|integer');
        $this->post_form->set_rules('category', 'lang:post_category', 'required');
        $this->post_form->set_rules('description');
        $this->post_form->set_rules('zipCode');
        $this->post_form->set_rules('pickUp');
        $this->post_form->set_rules('freeShipping');
        
        $this->post_form->set_message('required', lang('validation_required'));
        
        $is_valid = $this->post_form->run();
        return $is_valid;
    }
    
    private function post_map() {
        $this->load->library('form_validation', null, 'post_form');
        $this->post_form->set_error_delimiters('<div class="error">', '</div>');
        
        $this->post_form->set_rules('longitude', 'lang:post_longitude', 'trim|required');
        $this->post_form->set_rules('latitude', 'lang:post_latitude', 'trim|required');
        
        $is_valid = $this->post_form->run();
        return $is_valid;
    }
    
    private function post_preview() {
        $this->load->library('form_validation', null, 'post_form');
        $this->post_form->set_error_delimiters('<div class="error">', '</div>');
        
        $this->post_form->set_rules('step');
        
        $is_valid = $this->post_form->run();
        return $is_valid;
    }
    
    private function create_ad() {
        $start_array = $this->loadPostData(Post_member::STEP_START);
        $details_array = $this->loadPostData(Post_member::STEP_DETAILS);
        $map_array = $this->loadPostData(Post_member::STEP_MAP);
        
        $post_start_array = $start_array[Post_member::SESSION_KEY_POST];
        $post_details_array = $details_array[Post_member::SESSION_KEY_POST];
        $post_map_array = $map_array[Post_member::SESSION_KEY_POST];
        
        $files_start_array = $start_array[Post_member::SESSION_KEY_FILES];
        //$files_details_array = $details_array[Post_member::SESSION_KEY_FILES];
        //$files_map_array = $map_array[Post_member::SESSION_KEY_FILES];
        
        if ( empty($post_start_array) ) {
            return '';
        }
        
        $image_file_name = $this->getImageFileName($post_start_array, $files_start_array, 'ad_image');
        $ad_image = Image_model::createImageModel($image_file_name);
        
        $address = new Address_model();
        $address->zipCode = $post_details_array['zipCode'];
        $address->longitude = $post_map_array['longitude'];
        $address->latitude = $post_map_array['latitude'];
        
        $ad = new Ad_model();
        $ad->type = Ad_model::ADTYPE_MEMBER;
        $ad->title = $post_details_array['title'];
        $ad->categoryId = $post_details_array['category'];
        $ad->price = hasElement($post_details_array, 'price') ? getElement($post_details_array, 'price') * 1 : 0;
        $ad->description = $post_details_array['description'];
        $ad->quantity = 1;
        $ad->pickUp = hasElement($post_details_array, 'pickUp') && $post_details_array['pickUp'] == '1' ? true : false;
        $ad->freeShipping = hasElement($post_details_array, 'freeShipping') && $post_details_array['freeShipping'] == '1' ? true : false;
        $ad->image = $ad_image;
        $ad->address = $address;
        
        //print_r($ad);
        
        $errors = '';
        try {
            $adId = $this->ad_service->placeAd($ad);
            log_message(INFO, 'Message created: ' . $adId);
            
            $this->usermanagement_service->refreshUser();
        } catch ( Exception $ex ) {
            $errors .= $ex->getMessage();
        }
        return $errors;
    }
    
    private function getImageFileName($post_array, $files_array, $field) {
        if (hasElement($files_array[$field], 'data') ) {
            $image_file_name = $files_array[$field]['data']['file_name'];
        } else if (hasElement($post_array, $field.'_posted') ) {
            $image_file_name = $post_array[$field.'_posted'];
        } else {
            $image_file_name = null;
        }
        return $image_file_name;
    }
    
    // internal validation methods
    
    public function file_upload($value, $field) {
        if ( !isset($_FILES[$field]) || $_FILES[$field]['error'] == 4 ) {
            //no file selected for upload
            return TRUE;
        }
        
        $config['upload_path'] = TEMP_FOLDER;
        $config['allowed_types'] = 'gif|jpg|png|jpeg';
        $config['encrypt_name'] = true;
        
        $this->load->library('upload', $config);
        if ( !$this->upload->do_upload($field)) {
            $error = $this->upload->display_errors();
            $this->post_form->set_message('file_upload', $error);
            return FALSE;
        }
        
        $data = $this->upload->data();
        $_FILES[$field]['data'] = $data; //extending super global to store CI upload data in the session
        
        return TRUE;
    }
}