<?php
$services = "http://gifteng-ws.jelastic.servint.net/gifteng/services/";
return array(
	'auth' => $services.'AuthService?wsdl',
	'ad' => $services.'AdService?wsdl',
	'user' => $services.'UserManagementService?wsdl',
	'message' => $services."MessageService?wsdl",
	'utility' => $services."UtilityService?wsdl",
	'invite' => $services."InvitationService?wsdl",
	'admin' => $services."AdminService?wsdl"
);