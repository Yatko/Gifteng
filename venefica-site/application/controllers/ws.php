<?php

class WS extends CI_Controller {

    public function __construct() {
        parent::__construct();
    }

    public function index() {
        $authService = new SoapClient("http://veneficalabs.com:8080/venefica/services/AuthService?wsdl");
        $result = $authService->authenticate(array(
            "name" => "123456",
            "password" => "123456"
        ));
        $token = $result->AuthToken;
        
        
        $context = array(
            'http' => array(
                'header' => "AuthToken: $token"
            )
        );
        $soap_options = array(
            'stream_context' => stream_context_create($context)
        );
        $adService = new SoapClient("http://veneficalabs.com:8080/venefica/services/AdService?wsdl", $soap_options);
        $result = $adService->getAllCategories();
        print_r($result->category);
        
        
        $data['title'] = 'WS test to venefica';

        $this->load->view('templates/header', $data);
        $this->load->view('ws/index', $data);
        $this->load->view('templates/footer');
    }

}
