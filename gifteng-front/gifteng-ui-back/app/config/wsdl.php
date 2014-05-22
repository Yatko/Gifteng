<?php
$services = "http://veneficalabs.com:8080/venefica/services/";
return array(
	'auth' => $services.'AuthService?wsdl',
	'ad' => $services.'AdService?wsdl',
	'user' => $services.'UserManagementService?wsdl',
	'message' => $services."MessageService?wsdl",
	'utility' => $services."UtilityService?wsdl",
	'invite' => $services."InvitationService?wsdl",
	'admin' => $services."AdminService?wsdl",
	'social' => $services."SocialService?wsdl"
);