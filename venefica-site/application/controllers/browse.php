<?php

class Browse extends CI_Controller {
    
    public function view($category = null) {
        $token = $this->session->userdata('token');
        if ( $token ) {
            $is_logged = true;
        } else {
            $is_logged = false;
        }

        if ( $is_logged ) {
            $context = array('http' => array('header' => "AuthToken: $token"));
            $soap_options = array('stream_context' => stream_context_create($context));
            $adService = new SoapClient(AD_SERVICE_WSDL, $soap_options);
            $result = $adService->getAllCategories();

            $data['categories'] = $result->category;

            //load translations
            $this->lang->load('main');

            $this->load->view('templates/header');
            $this->load->view('pages/browse', $data);
            $this->load->view('templates/footer');
        } else {
            $this->load->view('templates/header');
            $this->load->view('templates/footer');
        }
    }
}
