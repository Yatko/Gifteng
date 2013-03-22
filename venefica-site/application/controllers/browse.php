<?php

class Browse extends CI_Controller {
    
    public function view($category = null) {
        try {
            $authService = new SoapClient("http://veneficalabs.com:8080/venefica/services/AuthService?wsdl");
            $result = $authService->authenticate(array("name" => "123456", "password" => "123456a"));
            $token = $result->AuthToken;
        } catch ( Exception $ex ) {
            echo $ex->faultstring;
            //print_r($ex);
        }
        
        $context = array('http' => array('header' => "AuthToken: $token"));
        $soap_options = array('stream_context' => stream_context_create($context));
        $adService = new SoapClient("http://veneficalabs.com:8080/venefica/services/AdService?wsdl", $soap_options);
        $result = $adService->getAllCategories();
        
        $data['categories'] = $result->category;
        
        //load translations
        $this->lang->load('main');
        
        $this->load->view('templates/header');
        $this->load->view('pages/browse', $data);
        $this->load->view('templates/footer');
    }
}
