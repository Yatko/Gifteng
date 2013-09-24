<?php
$services = "http://veneficalabs.com:8080/venefica/services/";
return array(
	'auth' => $services.'AuthService?wsdl',
	'ad' => $services.'AdService?wsdl',
);