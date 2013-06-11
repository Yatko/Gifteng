<?php

class Post extends CI_Controller {
    
    private $initialized = false;
    
    const STEP_START = 'start';
    const STEP_ONLINE = 'online';
    const STEP_LOCATION = 'location';
    const STEP_PREVIEW = 'preview';
    const STEP_POST = 'post';
    
    var $user;
    
    public function view() {
        $this->init();
        
        if ( !validate_login() ) return;
        
        $this->user = $this->usermanagement_service->loadUser();
        
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
        $data['user'] = $this->user;
        $data['step'] = $step;
        
        if ( $step == Post::STEP_POST ) {
            $error = $this->create_ad();
            $data['error'] = $error;
        }
        
        if ( $step == Post::STEP_START ) {
            try {
                $categories = $this->ad_service->getAllCategories();
            } catch ( Exception $ex ) {
                $categories = array();
            }
            
            $data['categories'] = $categories;
        } else if ( $step == Post::STEP_ONLINE ) {
            $post_online_array = $this->session->flashdata("post_".Post::STEP_ONLINE);
            $files_online_array = $this->session->flashdata("files_".Post::STEP_ONLINE);
            
            $image = $this->getImageFileName($post_online_array, $files_online_array, 'image', '');
            if ( is_empty($image) ) {
                $image = null;
            }
            
            $data['image'] = $image;
        } else if ( $step == Post::STEP_LOCATION ) {
            $post_location_array = $this->session->flashdata("post_".Post::STEP_LOCATION);
            $files_location_array = $this->session->flashdata("files_".Post::STEP_LOCATION);
            
            if ( $this->user->addresses && sizeof($this->user->addresses) > 0 ) {
                foreach ( $this->user->addresses as $address ) {
                    $address_id = $address->id;
                    
                    $image = $this->getImageFileName($post_location_array, $files_location_array, 'image', $address_id);
                    if ( is_empty($image) ) {
                        $image = null;
                    }
                    $data['image_'.$address_id] = $image;
                    
                    $bar_code = $this->getImageFileName($post_location_array, $files_location_array, 'bar_code', $address_id);
                    if ( is_empty($bar_code) ) {
                        $bar_code = null;
                    }
                    $data['bar_code_'.$address_id] = $bar_code;
                }
            }
        }
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        if ($this->user->businessAccount ) {
            $this->load->view('pages/business_post', $data);
        } else {
            $this->load->view('pages/member_post', $data);
        }
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    public function new_address() {
        $this->init();
        
        if ( !isLogged() ) {
            return;
        } else if ( !$_POST ) {
            return;
        }
        
        $this->user = $this->usermanagement_service->loadUser();
        $this->keepPostData(null);
        
        $address = new Address_model();
        $address->name = $this->input->post('new_address_name');
        $address->address1 = $this->input->post('new_address_address');
        $address->address2 = $this->input->post('new_address_address_2');
        $address->city = $this->input->post('new_address_city');
        $address->zipCode = $this->input->post('new_address_zipCode');
        
        try {
            $this->user->addAddress($address);
            $this->usermanagement_service->updateUser($this->user);
            $this->usermanagement_service->refreshUser();
            $this->user = $this->usermanagement_service->loadUser();
            
            $data['obj'] = $this->user->getLastAddress();
            $this->load->view('json', $data);
        } catch ( Exception $ex ) {
        }
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
        if ( $current_step != null ) {
            $this->session->set_flashdata('post_'.$current_step, $_POST);
        }
        
        $this->session->keep_flashdata('files_'.Post::STEP_START);
        $this->session->keep_flashdata('files_'.Post::STEP_ONLINE);
        $this->session->keep_flashdata('files_'.Post::STEP_LOCATION);
        if ( $current_step != null ) {
            $this->session->set_flashdata('files_'.$current_step, $_FILES);
        }
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
        $this->post_form->set_rules('image', 'lang:post_image', 'callback_file_upload[image]');
        
        $this->post_form->set_message('required', lang('validation_required'));
        $this->post_form->set_message('is_natural_no_zero', lang('validation_is_natural_no_zero'));
        
        $is_valid = $this->post_form->run();
        return $is_valid;
    }
    
    private function post_location() {
        $this->load->library('form_validation', null, 'post_form');
        $this->post_form->set_error_delimiters('<div class="error">', '</div>');
        
        $this->post_form->set_rules('description');
        $this->post_form->set_rules('availableDays');
        $this->post_form->set_rules('availableFromTime');
        $this->post_form->set_rules('availableToTime');
        $this->post_form->set_rules('availableAllDay');
        $this->post_form->set_rules('website');
        
        if ( $this->user->addresses && sizeof($this->user->addresses) > 0 ) {
            foreach ( $this->user->addresses as $address ) {
                $address_id = $address->id;
                
                if ( $_POST['quantity_'.$address_id] * 1 <= 0 ) {
                    //positive (non zero) quantities will mark the active address
                    continue;
                }
                
                $this->post_form->set_rules('image_'.$address_id, 'lang:post_image', 'callback_file_upload[image_'.$address_id.']');
                $this->post_form->set_rules('bar_code_'.$address_id, 'lang:post_bar_code', 'callback_file_upload[bar_code_'.$address_id.']');
            }
        }
        
        $this->post_form->set_message('required', lang('validation_required'));
        $this->post_form->set_message('is_natural_no_zero', lang('validation_is_natural_no_zero'));
        
        $is_valid = $this->post_form->run();
        return $is_valid;
    }
    
    private function create_ad() {
        $post_start_array = $this->session->flashdata("post_".Post::STEP_START);
        $post_online_array = $this->session->flashdata("post_".Post::STEP_ONLINE);
        $post_location_array = $this->session->flashdata("post_".Post::STEP_LOCATION);
        
        $files_start_array = $this->session->flashdata("files_".Post::STEP_START);
        $files_online_array = $this->session->flashdata("files_".Post::STEP_ONLINE);
        $files_location_array = $this->session->flashdata("files_".Post::STEP_LOCATION);
        
        $ads = array();
        
        if ( $post_start_array['place'] == Ad_model::PLACE_ONLINE ) {
            $image_file_name = $this->getImageFileName($post_online_array, $files_online_array, 'image', '');
            $image = Image_model::createImageModel($image_file_name);
            
            $ad = new Ad_model();
            $this->populateAdWithStart($post_start_array, $files_start_array, $ad);
            $ad->description = $post_online_array['description'];
            $ad->quantity = $post_online_array['quantity'];
            $ad->promoCode = $post_online_array['promocode'];
            $ad->website = $post_online_array['website'];
            $ad->image = $image;
            
            array_push($ads, $ad);
        } elseif ( $post_start_array['place'] == Ad_model::PLACE_LOCATION ) {
            if ( $this->user->addresses && sizeof($this->user->addresses) > 0 ) {
                foreach ( $this->user->addresses as $address ) {
                    $address_id = $address->id;
                    
                    if ( $_POST['quantity_'.$address_id] * 1 <= 0 ) {
                        //positive (non zero) quantities will mark the active address
                        continue;
                    }
                    
                    $address = $this->user->getAddressById($address_id);
                    
                    $image_file_name = $this->getImageFileName($post_location_array, $files_location_array, 'image', $address_id);
                    $image = Image_model::createImageModel($image_file_name);
                    
                    $bar_code_file_name = $this->getImageFileName($post_location_array, $files_location_array, 'bar_code', $address_id);
                    $bar_code = Image_model::createImageModel($bar_code_file_name);
                    
                    if ( $post_location_array['availableFromTime'] == "*" ) {
                        $availableAllDay = true;
                        $availableFromTime = null;
                        $availableToTime = null;
                    } else {
                        $availableAllDay = false;
                        $availableFromTime = convertHourToTimestamp($post_location_array['availableFromTime'] * 1);
                        $availableToTime = convertHourToTimestamp($post_location_array['availableToTime'] * 1);
                    }
                    
                    $ad = new Ad_model();
                    $this->populateAdWithStart($post_start_array, $files_start_array, $ad);
                    $ad->description = $post_location_array['description'];
                    $ad->needsReservation = hasElement($post_location_array, 'needsReservation') ? true : false;
                    $ad->availableFromTime = $availableFromTime;
                    $ad->availableToTime = $availableToTime;
                    $ad->availableAllDay = $availableAllDay;
                    $ad->availableDays = hasElement($post_location_array, 'availableDays') ? $post_location_array['availableDays'] : null;
                    $ad->address = $address;
                    $ad->quantity = $post_location_array['quantity_'.$address_id];
                    $ad->image = $image;
                    $ad->imageBarcode = $bar_code;
                    
                    array_push($ads, $ad);
                }
            }
        }
        
        if (sizeof($ads) == 0 ) {
            return 'No ad to create !!!';
        }
        
        //print_r($this->session->all_userdata());
        //print_r($ads);
        //print_r($_POST);
        //print_r($_FILES);
        //print_r($post_location_array);
        //print_r($files_location_array);
        //return;
        
        $errors = '';
        $adId = -1;
        foreach ( $ads as $ad ) {
            try {
                $adId = $this->ad_service->placeAd($ad);
            } catch ( Exception $ex ) {
                $errors .= $ex->getMessage();
                $errors .= '<br/>';
            }
        }
        
        if ( empty($errors) ) {
            redirect("/view/".$adId);
        }
        return $errors;
    }
    
    private function populateAdWithStart($post_start_array, $files_start_array, Ad_model $ad) {
        $ad->type = Ad_model::ADTYPE_BUSINESS;
        $ad->title = $post_start_array['title'];
        $ad->subtitle = $post_start_array['subtitle'];
        $ad->categoryId = $post_start_array['category'];
        $ad->price = hasElement($post_start_array, 'price') ? getElement($post_start_array, 'price') * 1 : 0;
        $ad->place = $post_start_array['place'];
        $ad->availableAt = convertDateToTimestamp($post_start_array['availableAt']) * 1;
        $ad->expiresAt = convertDateToTimestamp($post_start_array['expiresAt']) * 1;
        $ad->expires = hasElement($post_start_array, 'neverExpires') ? false : true;
    }
    
    private function getImageFileName($post_array, $files_array, $field, $extra) {
        if ( !empty($extra) ) {
            $extra = '_' . $extra;
        }
        
        if (hasElement($files_array[$field.$extra], 'data') ) {
            $image_file_name = $files_array[$field.$extra]['data']['file_name'];
        } else if (hasElement($post_array, $field.$extra.'_posted') ) {
            $image_file_name = $post_array[$field.$extra.'_posted'];
        } else {
            $image_file_name = null;
        }
        return $image_file_name;
    }
    
    // internal valodation methods
    
    public function file_upload($null, $field) {
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
        $_FILES[$field]['data'] = $data; //extending super global to store CI data in the session
        
        return TRUE;
    }
}