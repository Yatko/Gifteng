<?php

class Invitation extends CI_Controller {
    
    const ACTION_REQUEST = 'request';
    const ACTION_VERIFY = 'verify';
    
    private $initialized = false;
    
    public function view($extra_data = array()) {
        $this->init();
        
        $action = $this->getActionFromUri();
        
        $data = $extra_data;
        $data['step'] = $this->getStepFromUri();
        
        $this->load->view('templates/'.TEMPLATES.'/header');
        if ( $action == Invitation::ACTION_REQUEST ) {
            $this->load->view('pages/invitation_request', $data);
        } else if ( $action == Invitation::ACTION_VERIFY ) {
            $this->load->view('pages/invitation_verify', $data);
        } else {
            redirect('/invitation/' . Invitation::ACTION_REQUEST);
        }
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    public function request() {
        $this->init();
        $extra_data = array();
        
        $is_valid = $this->process_invitation_request($extra_data);
        
        if ( $is_valid == FALSE ) {
            $this->view($extra_data);
        }
    }
    
    public function verify() {
        $this->init();
        $extra_data = array();
        
        $is_valid = $this->process_invitation_verify($extra_data);
        
        if ( $is_valid == FALSE ) {
            $this->view($extra_data);
        }
    }

    private function process_invitation_request(&$extra_data) {
        $step = $this->getStepFromUri();
        $is_valid = $this->invitation_request($step);
        if ( $is_valid ) {
            if ( $step == 1 ) {
                $email = $this->input->post('invitation_email');
                $this->session->set_flashdata('invitation_email', $email);
                
                redirect('/invitation/'. Invitation::ACTION_REQUEST . '/2');
            } elseif ( $step == 2 ) {
                $email = $this->input->post('invitation_email');
                $this->session->set_flashdata('invitation_email', $email);
                
                redirect('/invitation/' . Invitation::ACTION_REQUEST . '/3');
            } else {
                log_message(INFO, "Invalid step ($step)!");
                redirect('/invitation/' . Invitation::ACTION_REQUEST . '/1');
            }
        } elseif ( $_POST ) {
            if ( $step == 1 ) {
                //nothing special here
            } elseif ( $step == 2 ) {
                $country = $this->input->post('invitation_country');
                $source = $this->input->post('invitation_source');
                $email = $this->input->post('invitation_email');
                $this->session->set_flashdata('invitation_email', $email);
                
                $extra_data['invitation_country'] = $country;
                $extra_data['invitation_source'] = $source;
                $extra_data['invitation_email'] = $email;
            } else {
                log_message(INFO, "Invalid step ($step)!");
                redirect('/invitation/' . Invitation::ACTION_REQUEST . '/1');
            }
        } else {
            if ( $step == 1 ) {
                //nothing special here
            } elseif ( $step == 2 ) {
                $email = $this->session->flashdata('invitation_email');
                if ( $email ) {
                    $extra_data['invitation_country'] = '';
                    $extra_data['invitation_source'] = '';
                    $extra_data['invitation_email'] = $email;
                } else {
                    redirect('/invitation/' . Invitation::ACTION_REQUEST . '/1');
                }
            } elseif ( $step == 3 ) {
                $email = $this->session->flashdata('invitation_email');
                if ( $email ) {
                    //success and thank you message
                } else {
                    redirect('/invitation/' . Invitation::ACTION_REQUEST . '/1');
                }
            } else {
                log_message(INFO, "Invalid step ($step)!");
                redirect('/invitation/' . Invitation::ACTION_REQUEST . '/1');
            }
        }
        return $is_valid;
    }
    
    private function process_invitation_verify(&$extra_data) {
        $step = $this->getStepFromUri();
        $is_valid = $this->invitation_verify($step);
        if ( $is_valid ) {
            if ( $step == 1 ) {
                $code = $this->input->post('invitation_code');
                $this->session->set_flashdata('invitation_code', $code);
                
                redirect('/invitation/' . Invitation::ACTION_VERIFY . '/2');
            } else {
                log_message(INFO, "Invalid step ($step)!");
                redirect('/invitation/' . Invitation::ACTION_VERIFY . '/1');
            }
        } elseif ( $_POST ) {
            if ( $step == 1 ) {
                //nothing special here
            } elseif ( $step == 2 ) {
                $code = $this->input->post('invitation_code');
                $this->session->set_flashdata('invitation_code', $code);
                
                redirect('/registration/user');
            } else {
                log_message(INFO, "Invalid step ($step)!");
                redirect('/invitation/' . Invitation::ACTION_VERIFY . '/1');
            }
        } else {
            if ( $step == 1 ) {
                //nothing special here
            } elseif ( $step == 2 ) {
                $code = $this->session->flashdata('invitation_code');
                if ( $code ) {
                    $extra_data['invitation_code'] = $code;
                } else {
                    redirect('/invitation/' . Invitation::ACTION_VERIFY . '/1');
                }
            } else {
                log_message(INFO, "Invalid step ($step)!");
                redirect('/invitation/' . Invitation::ACTION_VERIFY . '/1');
            }
        }
        return $is_valid;
    }
    
    private function invitation_request($step) {
        $is_valid = true;
        $this->load->library('form_validation', null, 'request_invitation_form');
        $this->request_invitation_form->set_error_delimiters('<div class="error">', '</div>');
        
        if ( $step == 1 ) {
            $this->request_invitation_form->set_rules('invitation_email', 'lang:invitation_email', 'trim|required|valid_email');
            
            $this->request_invitation_form->set_message('required', lang('validation_required'));
            $this->request_invitation_form->set_message('valid_email', lang('validation_valid_email'));
        } elseif ( $step == 2 ) {
            if ( $_POST ) {
                if (
                    !$this->request_invitation_form->required($this->input->post('invitation_country')) &&
                    !$this->request_invitation_form->required($this->input->post('invitation_usertype'))
                ) {
                    $this->request_invitation_form->setFieldValue('invitation_country', $this->input->post('invitation_country'));
                    $this->request_invitation_form->setFieldValue('invitation_zipcode', $this->input->post('invitation_zipcode'));
                    $this->request_invitation_form->setFieldValue('invitation_source', $this->input->post('invitation_source'));
                    $this->request_invitation_form->setFieldValue('invitation_usertype', $this->input->post('invitation_usertype'));
                    $this->request_invitation_form->setError(lang('invitation_request_failed_empty_form'));
                    $is_valid = false;
                } else if (
                    $this->input->post('invitation_country') == 'United States' &&
                    (
                        !$this->request_invitation_form->exact_length($this->input->post('invitation_zipcode'), 5) ||
                        !$this->request_invitation_form->numeric($this->input->post('invitation_zipcode'))
                    )
                ) {
                    $this->request_invitation_form->setFieldValue('invitation_country', $this->input->post('invitation_country'));
                    $this->request_invitation_form->setFieldValue('invitation_zipcode', $this->input->post('invitation_zipcode'));
                    $this->request_invitation_form->setFieldValue('invitation_source', $this->input->post('invitation_source'));
                    $this->request_invitation_form->setFieldValue('invitation_usertype', $this->input->post('invitation_usertype'));
                    $this->request_invitation_form->setError(lang('invitation_request_failed_incorrect_zipcode'));
                    $is_valid = false;
                } else if ( !$this->request_invitation_form->required($this->input->post('invitation_source')) ) {
                    $this->request_invitation_form->setFieldValue('invitation_country', $this->input->post('invitation_country'));
                    $this->request_invitation_form->setFieldValue('invitation_zipcode', $this->input->post('invitation_zipcode'));
                    $this->request_invitation_form->setFieldValue('invitation_source', $this->input->post('invitation_source'));
                    $this->request_invitation_form->setFieldValue('invitation_usertype', $this->input->post('invitation_usertype'));
                    $this->request_invitation_form->setError(lang('invitation_request_failed_choose_source'));
                    $is_valid = false;
                } else if ( !$this->request_invitation_form->required($this->input->post('invitation_usertype')) ) {
                    $this->request_invitation_form->setFieldValue('invitation_country', $this->input->post('invitation_country'));
                    $this->request_invitation_form->setFieldValue('invitation_zipcode', $this->input->post('invitation_zipcode'));
                    $this->request_invitation_form->setFieldValue('invitation_source', $this->input->post('invitation_source'));
                    $this->request_invitation_form->setFieldValue('invitation_usertype', $this->input->post('invitation_usertype'));
                    $this->request_invitation_form->setError(lang('invitation_request_failed_choose_usertype'));
                    $is_valid = false;
                }
            }
            
            if ( $is_valid ) {
                $this->request_invitation_form->set_rules('invitation_country', 'lang:invitation_country', 'trim|required');
                $this->request_invitation_form->set_rules('invitation_zipcode', 'lang:invitation_zipcode', 'trim');
                $this->request_invitation_form->set_rules('invitation_source', 'lang:invitation_source', 'trim|required');
                $this->request_invitation_form->set_rules('invitation_source_other', 'lang:invitation_source_other', 'trim');
                $this->request_invitation_form->set_rules('invitation_usertype', 'lang:invitation_usertype', 'trim|required');
                $this->request_invitation_form->set_rules('invitation_email', 'lang:invitation_email', 'trim|required|valid_email|callback_request_invitation');

                $this->request_invitation_form->set_message('required', lang('validation_required'));
                $this->request_invitation_form->set_message('valid_email', lang('validation_valid_email'));
            }
        }
        if ( $is_valid ) {
            $is_valid = $this->request_invitation_form->run();
        }
        return $is_valid;
    }
    
    private function invitation_verify($step) {
        $this->load->library('form_validation', null, 'verify_invitation_form');
        $this->verify_invitation_form->set_error_delimiters('<div class="error">', '</div>');
        if ( $step == 1 ) {
            $this->verify_invitation_form->set_rules('invitation_code', 'lang:invitation_code', 'trim|required|callback_verify_invitation');
            
            $this->verify_invitation_form->set_message('required', lang('validation_required'));
        } elseif ( $step == 2 ) {
            //nothing special here
        }
        $is_valid = $this->verify_invitation_form->run();
        return $is_valid;
    }
    
    /**
     * Internal validation method, that will tries to create an invitation.
     * 
     * @param string $email
     * @return boolean
     */
    public function request_invitation($email) {
        if ( $this->request_invitation_form->hasErrors() ) {
            //$this->request_invitation_form->set_message('request_invitation', lang('invitation_request_failed'));
            $this->request_invitation_form->set_message('request_invitation', '');
            return FALSE;
        }
        
        $invitation = new Invitation_model();
        $invitation->ipAddress = $this->input->ip_address();
        $invitation->email = $this->input->post('invitation_email');
        $invitation->country = $this->input->post('invitation_country');
        $invitation->zipCode = $this->input->post('invitation_zipcode');
        $invitation->source = $this->input->post('invitation_source');
        $invitation->otherSource = $this->input->post('invitation_source_other');
        $invitation->userType = $this->input->post('invitation_usertype');

        try {
            $this->load->library('invitation_service');
            $this->invitation_service->requestInvitation($invitation);
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Cannot request invitation: '.$ex->getMessage());
            
            $code = $ex->getCode() != null ? $ex->getCode() : Invitation_service::GENERAL_ERROR;
            $errorMessage = lang('invitation_request_failed');
            
            if ( $code == Invitation_service::ALREADY_SUBSCRIBED ) {
                $errorMessage = lang('invitation_request_failed_already_subscribed');
            }
            
            $this->request_invitation_form->set_message('request_invitation', $errorMessage);
            return FALSE;
        }
        return TRUE;
    }
    
    /**
     * 
     * @param string $code invitation code
     * @return boolean
     */
    public function verify_invitation($code) {
        if ( $this->verify_invitation_form->hasErrors() ) {
            //$this->verify_invitation_form->set_message('verify_invitation', lang('invitation_verify_failed'));
            $this->verify_invitation_form->set_message('verify_invitation', '');
            return FALSE;
        }
        
        try {
            $this->load->library('invitation_service');
            $valid = $this->invitation_service->isInvitationValid($code);
            if ( !$valid ) {
                log_message(INFO, 'Invitation is not valid');
                $this->verify_invitation_form->set_message('verify_invitation', lang('invitation_verify_invalid'));
                return FALSE;
            }
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Cannot verify invitation: '.$ex->getMessage());
            $this->verify_invitation_form->set_message('verify_invitation', lang('invitation_verify_failed'));
            return FALSE;
        }
        return TRUE;
    }
    
    public function facebook() {
        $this->init();
            
        $this->load->view('templates/'.TEMPLATES.'/header');
        $this->load->view('pages/invitation_facebook');
        $this->load->view('templates/'.TEMPLATES.'/footer');
    }
    
    // internal functions
    
    private function init() {
        if ( !$this->initialized ) {
            //load translations
            $this->lang->load('main');
            $this->lang->load('invitation');
            
            $this->load->model('invitation_model');
            
            clear_cache();
            
            $this->initialized = true;
        }
    }
    
    private function getActionFromUri() {
        $action = $this->uri->segment(2, null);
        return $action;
    }
    
    private function getStepFromUri() {
        $step = $this->uri->segment(3, null);
        return $step;
    }
}
