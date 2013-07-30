<?php

class Post_member extends CI_Controller {
    
    private $initialized = false;
    
    const STEP_START = 'start';
    const STEP_DETAILS = 'details';
    const STEP_MAP = 'map';
    const STEP_PREVIEW = 'preview';
    const STEP_POST = 'post';
    
    var $steps = array(
        Post_member::STEP_START,
        Post_member::STEP_DETAILS,
        Post_member::STEP_MAP,
        Post_member::STEP_PREVIEW,
        Post_member::STEP_POST
    );
    
    var $ad;
    var $is_new;
    var $adId;
    var $is_first_page;
    var $unique_id;
    
    public function create() {
        $this->init();
        
        if ( !validate_login() ) return;
        
        if ( isBusinessAccount() ) {
            safe_redirect("/post/business");
        }
        
        $this->is_new = true;
        $this->adId = null;
        
        $this->render();
    }
    
    public function edit($adId) {
        $this->init();
        
        if ( !validate_login() ) return;
        
        if ( isBusinessAccount() ) {
            safe_redirect("/edit_post/business/" . $adId);
        }
        
        $this->is_new = false;
        $this->adId = $adId;
        
        if ( $this->is_first_page ) {
            $ad = $this->ad_service->getAdById($adId);
            $this->storeAd($ad);
        }
        
        $this->render();
    }
    
    public function remove() {
        if ( !isLogged() ) {
            return;
        } else if ( !$_POST ) {
            return;
        }
        
        $this->unique_id = $this->input->post('unique_id');
        $this->removeAd();
        
        respond_ajax(AJAX_STATUS_RESULT, 'OK');
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
            
            $this->is_first_page = ($_POST || $_FILES ? false : true);
            if ( $this->is_first_page ) {
                $ad = new Ad_model();
                $ad->type = Ad_model::ADTYPE_MEMBER;
                $ad->quantity = 1;
                $ad->address = new Address_model();
                $ad->image = new Image_model();
                
                $this->unique_id = uniqid('gifteng_');
                $this->storeAd($ad);
            } else {
                $this->unique_id = $this->input->post('unique_id');
                $this->loadAd(); //loading Ad_model from the session
            }
            
            $this->initialized = true;
        }
    }
    
    private function render() {
        $is_modal = key_exists('modal', $_GET);
        $current_step = $this->getCurrentStep();
        $next_step = $this->getNextStep($current_step);
        $is_valid = $this->process($current_step);
        
        if ( $is_valid ) {
            //form data valid after post
            $step = $next_step;
            
            if ( $step == Post_member::STEP_POST ) {
                $this->removeAd();
            } else {
                $this->storeAd();
            }
        } elseif ( $_POST ) {
            //form data not valid after post
            $step = $current_step;
            $this->storeAd();
        } else {
            //direct access of the page
            $step = $current_step;
        }
        
        //print_r($this->ad);
        
        $data = array();
        $data['step'] = $step;
        $data['is_modal'] = $is_modal;
        $data['unique_id'] = $this->unique_id;
        $data['is_new'] = $this->is_new;
        $data['adId'] = $this->adId;
        
        if ( $step == Post_member::STEP_START ) {
            $data['image'] = $this->ad->image;
        } else if ( $step == Post_member::STEP_DETAILS ) {
            try {
                $categories = $this->ad_service->getAllCategories();
            } catch ( Exception $ex ) {
                $categories = array();
            }
            
            $data['title'] = $this->ad->getSafeTitle();
            $data['description'] = $this->ad->getSafeDescription();
            $data['category'] = $this->ad->categoryId;
            $data['price'] = $this->ad->price;
            $data['zipCode'] = $this->ad->address->zipCode;
            $data['pickUp'] = $this->ad->pickUp ? '1' : '0';
            $data['freeShipping'] = $this->ad->freeShipping ? '1' : '0';
            $data['categories'] = $categories;
        } else if ( $step == Post_member::STEP_MAP ) {
            $zipCode = $this->ad->address->zipCode;
            $longitude = $this->ad->address->longitude;
            $latitude = $this->ad->address->latitude;
            
            if ( empty($longitude) || empty($latitude) ) {
                //$location = getLocationByZipCode($zipCode);
                //$marker_longitude = $location['longitude'];
                //$marker_latitude = $location['latitude'];
                $addressLocation = getAddressByZipCode($zipCode);
                $marker_longitude = $addressLocation != null ? $addressLocation->longitude : 0;
                $marker_latitude = $addressLocation != null ? $addressLocation->latitude : 0;
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
            
            $category = '';
            $categoryId = $this->ad->categoryId;
            foreach ($categories as $cat) {
                if ( $categoryId == $cat->id ) {
                    $category = $cat->name;
                    break;
                }
            }
            
            $data['image'] = $this->ad->image;
            $data['title'] = $this->ad->getSafeTitle();
            $data['description'] = $this->ad->getSafeDescription();
            $data['price'] = $this->ad->price;
            $data['pickUp'] = $this->ad->pickUp ? '1' : '0';
            $data['freeShipping'] = $this->ad->freeShipping ? '1' : '0';
            $data['category'] = $category;
        } else if ( $step == Post_member::STEP_POST ) {
            if ( $this->is_new ) {
                $error = $this->create_ad();
            } else {
                $error = $this->update_ad();
            }
            $data['error'] = $error;
        }
        
        if ( $is_modal ) {
            if ( $this->is_first_page ) {
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
    
    private function getCurrentStep() {
        if ( $_POST ) {
            return $this->input->post('step');
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
    
    private function storeAd($ad = null) {
        if ( $ad == null ) {
            $ad = $this->ad;
        } else {
            $this->ad = $ad;
        }
        
        //log_message(ERROR, 'storing ad: ' . print_r($ad, true));
        $key = $this->unique_id . '_ad';
        storeIntoSession($key, $ad);
        
        //$this->session->set_flashdata($this->unique_id . '_ad', $ad);
    }
    
    private function loadAd() {
        //$ad = $this->session->userdata($this->unique_id . '_ad');
        $key = $this->unique_id . '_ad';
        $ad = loadFromSession($key);
        //log_message(ERROR, 'loading ad: ' . print_r($ad, true));
        $this->ad = $ad;
    }
    
    private function removeAd() {
        $key = $this->unique_id . '_ad';
        removeFromSession($key);
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
        
        $this->post_form->set_rules('image', 'lang:post_image', 'callback_file_upload[image]');
        
        $is_valid = $this->post_form->run();
        return $is_valid;
    }
    
    private function post_details() {
        $is_valid = true;
        $this->load->library('form_validation', null, 'post_form');
        $this->post_form->set_error_delimiters('<div class="error">', '</div>');
        
        if ( $this->input->post('pickUp') || $this->input->post('freeShipping') ) {
            //we got at least one value
        } else {
            $this->post_form->setError(lang('post_member_delivery_missing'));
            $is_valid = false;
        }
        
        $this->post_form->set_rules('title', 'lang:post_title', 'trim|required');
        $this->post_form->set_rules('price', 'lang:post_price', 'trim|numeric');
        $this->post_form->set_rules('category', 'lang:post_category', 'required');
        $this->post_form->set_rules('description');
        $this->post_form->set_rules('zipCode', 'lang:post_zipCode', 'required');
        $this->post_form->set_rules('pickUp');
        $this->post_form->set_rules('freeShipping');
        
        $this->post_form->set_message('required', lang('validation_required'));
        
        if ( $is_valid ) {
            $is_valid = $this->post_form->run();
        }
        
        if ( $is_valid ) {
            $this->ad->title = $this->input->post('title');
            $this->ad->description = $this->input->post('description');
            $this->ad->categoryId = $this->input->post('category');
            $this->ad->price = $this->input->post('price');
            $this->ad->address->zipCode = $this->input->post('zipCode');
            $this->ad->pickUp = hasElement($_POST, 'pickUp') && $this->input->post('pickUp') == '1' ? true : false;
            $this->ad->freeShipping = hasElement($_POST, 'freeShipping') && $this->input->post('freeShipping') == '1' ? true : false;;
        }
        
        return $is_valid;
    }
    
    private function post_map() {
        $this->load->library('form_validation', null, 'post_form');
        $this->post_form->set_error_delimiters('<div class="error">', '</div>');
        
        $this->post_form->set_rules('longitude', 'lang:post_longitude', 'trim|required');
        $this->post_form->set_rules('latitude', 'lang:post_latitude', 'trim|required');
        
        $is_valid = $this->post_form->run();
        
        if ( $is_valid ) {
            $this->ad->address->longitude = $this->input->post('longitude');
            $this->ad->address->latitude = $this->input->post('latitude');
        }
        
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
        $this->fixAd();
        
        $errors = '';
        try {
            $adId = $this->ad_service->placeAd($this->ad);
            log_message(INFO, 'Ad created: ' . $adId);
            
            $this->usermanagement_service->refreshUser();
        } catch ( Exception $ex ) {
            $errors .= $ex->getMessage();
        }
        return $errors;
    }
    
    private function update_ad() {
        $this->fixAd();
        
        $errors = '';
        try {
            $this->ad_service->updateAd($this->ad);
            log_message(INFO, 'Ad updated');
            
            $this->usermanagement_service->refreshUser();
        } catch ( Exception $ex ) {
            $errors .= $ex->getMessage();
        }
        return $errors;
    }
    
    private function fixAd() {
        $this->ad->imageSafeLoadData();
        $this->ad->creator = null;
        $this->ad->statistics = null;
    }
    
    // internal validation methods
    
    public function file_upload($value, $field) {
        if ( !isset($_FILES[$field]) || $_FILES[$field]['error'] == 4 ) {
            //no file selected for upload
            
            if ( empty($this->ad->$field) || empty($this->ad->$field->url) ) {
                //ad image should not be empty
                $this->post_form->set_message('file_upload', 'Image is required');
                return FALSE;
            }
            return TRUE;
        }
        
        $config['upload_path'] = TEMP_FOLDER;
        $config['allowed_types'] = 'gif|jpg|png|jpeg';
        $config['encrypt_name'] = true;
        $config['max_size'] = UPLOAD_FILE_MAX_SIZE;
        
        $this->load->library('upload', $config);
        if ( !$this->upload->do_upload($field)) {
            $error = $this->upload->display_errors();
            $this->post_form->set_message('file_upload', $error);
            return FALSE;
        }
        
        $data = $this->upload->data();
        $file_name = $data['file_name'];
        
        $image = new Image_model();
        $image->url = $file_name;
        
        $this->ad->$field = $image;
        
        return TRUE;
    }
}