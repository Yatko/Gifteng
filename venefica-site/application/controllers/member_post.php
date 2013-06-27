<?php

class Member_post extends CI_Controller {
    
    private $initialized = false;
    
    const STEP_START = 'start';
    const STEP_DETAILS = 'details';
    const STEP_MAP = 'map';
    const STEP_PREVIEW = 'preview';
    const STEP_POST = 'post';
    
    var $steps = array(
        Member_post::STEP_START,
        Member_post::STEP_DETAILS,
        Member_post::STEP_MAP,
        Member_post::STEP_PREVIEW,
        Member_post::STEP_POST
    );
    
    var $user;
    
    public function view() {
        $this->init();
        
        if ( !validate_login() ) return;
        
        $this->user = $this->usermanagement_service->loadUser();
        if ( $this->user->businessAccount ) {
            redirect("/post/business");
        }
        
        $current_step = $this->getCurrentStep();
        $next_step = $this->getNextStep($current_step);
        $is_valid = $this->process($current_step);
        
        if ( $is_valid ) {
            //form data valid after post
            $step = $next_step;
            if ( $step == Member_post::STEP_POST ) {
                //do not keep flash data on last step - to avoid retransmit upon page reload
            } else {
                $this->keepPostData($current_step);
            }
        } elseif ( $_POST ) {
            //form data not valid after post
            $step = $current_step;
            $this->keepPostData($current_step);
        } else {
            //direct access of the page
            $step = $current_step;
        }
        
        $data = array();
        $data['user'] = $this->user;
        $data['step'] = $step;
        
        if ( $step == Member_post::STEP_START ) {
            $post_start_array = $this->session->flashdata("post_".Member_post::STEP_START);
            $files_start_array = $this->session->flashdata("files_".Member_post::STEP_START);
            
            $image = $this->getImageFileName($post_start_array, $files_start_array, 'image');
            if ( is_empty($image) ) {
                $image = null;
            }
            
            $data['image'] = $image;
        } else if ( $step == Member_post::STEP_DETAILS ) {
            try {
                $categories = $this->ad_service->getAllCategories();
            } catch ( Exception $ex ) {
                $categories = array();
            }
            
            $post_details_array = $this->session->flashdata("post_".Member_post::STEP_DETAILS);
            $files_details_array = $this->session->flashdata("files_".Member_post::STEP_DETAILS);
            
            $data['title'] = $post_details_array['title'];
            $data['description'] = $post_details_array['description'];
            $data['category'] = $post_details_array['category'];
            $data['price'] = $post_details_array['price'];
            $data['zipCode'] = $post_details_array['zipCode'];
            $data['pickUp'] = hasElement($post_details_array, 'pickUp') ? $post_details_array['pickUp'] : '0';
            $data['freeShipping'] = hasElement($post_details_array, 'freeShipping') ? $post_details_array['freeShipping'] : '0';
            $data['categories'] = $categories;
        } else if ( $step == Member_post::STEP_MAP ) {
            $post_details_array = $this->session->flashdata("post_".Member_post::STEP_DETAILS);
            $files_details_array = $this->session->flashdata("files_".Member_post::STEP_DETAILS);
            
            $post_map_array = $this->session->flashdata("post_".Member_post::STEP_MAP);
            $files_map_array = $this->session->flashdata("files_".Member_post::STEP_MAP);
            
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
        } else if ( $step == Member_post::STEP_PREVIEW ) {
            try {
                $categories = $this->ad_service->getAllCategories();
            } catch ( Exception $ex ) {
                $categories = array();
            }
            
            $post_start_array = $this->session->flashdata("post_".Member_post::STEP_START);
            $files_start_array = $this->session->flashdata("files_".Member_post::STEP_START);
            
            $post_details_array = $this->session->flashdata("post_".Member_post::STEP_DETAILS);
            $files_details_array = $this->session->flashdata("files_".Member_post::STEP_DETAILS);
            
            $image = $this->getImageFileName($post_start_array, $files_start_array, 'image');
            if ( is_empty($image) ) {
                $image = null;
            }
            
            $category = '';
            $categoryId = $post_details_array['category'];
            foreach ($categories as $cat) {
                if ( $categoryId == $cat->id ) {
                    $category = $cat->name;
                    break;
                }
            }
            
            $data['image'] = $image;
            $data['title'] = $post_details_array['title'];
            $data['description'] = $post_details_array['description'];
            $data['price'] = $post_details_array['price'];
            $data['pickUp'] = hasElement($post_details_array, 'pickUp') ? $post_details_array['pickUp'] : '0';
            $data['freeShipping'] = hasElement($post_details_array, 'freeShipping') ? $post_details_array['freeShipping'] : '0';
            $data['categoryId'] = $categoryId;
            $data['category'] = $category;
        } else if ( $step == Member_post::STEP_POST ) {
            $error = $this->create_ad();
            $data['error'] = $error;
        }
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/member_post', $data);
        $this->load->view('templates/'.TEMPLATES.'/footer');
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
            $step = $this->session->flashdata('step');
            if ( $step ) {
                return $step;
            }
        }
        return Member_post::STEP_START;
    }
    
    private function getNextStep($current_step) {
        if ( $_POST ) {
            $next_step = $this->input->post('next_step');
            if ( !empty($next_step) ) {
                return $next_step;
            }
        }
        
        if ( $current_step == Member_post::STEP_START ) {
            return Member_post::STEP_DETAILS;
        } elseif ( $current_step == Member_post::STEP_DETAILS ) {
            return Member_post::STEP_MAP;
        } elseif ( $current_step == Member_post::STEP_MAP ) {
            return Member_post::STEP_PREVIEW;
        } elseif ( $current_step == Member_post::STEP_PREVIEW ) {
            return Member_post::STEP_POST;
        }
        return null;
    }
    
    private function keepPostData($current_step) {
        foreach ( $this->steps as $step ) {
            $this->session->keep_flashdata('post_'.$step);
            $this->session->keep_flashdata('files_'.$step);
        }
        if ( $current_step != null ) {
            $this->session->set_flashdata('post_'.$current_step, $_POST);
            $this->session->set_flashdata('files_'.$current_step, $_FILES);
        }
    }
    
    private function process($current_step) {
        if ( $current_step == Member_post::STEP_START ) {
            return $this->post_start();
        } elseif ( $current_step == Member_post::STEP_DETAILS ) {
            return $this->post_details();
        } elseif ( $current_step == Member_post::STEP_MAP ) {
            return $this->post_map();
        } elseif ( $current_step == Member_post::STEP_PREVIEW ) {
            return $this->post_preview();
        }
        return true;
    }
    
    private function post_start() {
        $this->load->library('form_validation', null, 'post_form');
        $this->post_form->set_error_delimiters('<div class="error">', '</div>');
        
        $this->post_form->set_rules('image_posted');
        
        if ( empty($_FILES['image']['name']) ) {
            if ( empty($_POST['image_posted']) ) {
                $this->post_form->set_rules('image', 'lang:post_image', 'required');
            }
        } else {
            $this->post_form->set_rules('image', 'lang:post_image', 'callback_file_upload[image]');
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
        $post_start_array = $this->session->flashdata("post_".Member_post::STEP_START);
        $post_details_array = $this->session->flashdata("post_".Member_post::STEP_DETAILS);
        $post_map_array = $this->session->flashdata("post_".Member_post::STEP_MAP);
        
        $files_start_array = $this->session->flashdata("files_".Member_post::STEP_START);
        $files_details_array = $this->session->flashdata("files_".Member_post::STEP_DETAILS);
        $files_map_array = $this->session->flashdata("files_".Member_post::STEP_MAP);
        
        if ( empty($post_start_array) ) {
            return '';
        }
        
        $image_file_name = $this->getImageFileName($post_start_array, $files_start_array, 'image');
        $image = Image_model::createImageModel($image_file_name);
        
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
        $ad->image = $image;
        $ad->address = $address;
        
        //print_r($ad);
        
        $errors = '';
        try {
            $adId = $this->ad_service->placeAd($ad);
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