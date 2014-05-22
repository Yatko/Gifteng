<?php

class SocialController extends \BaseController {
	 
	 public function getConnectedNetworks() {
        try {
				$socialService = new SoapClient(Config::get('wsdl.social'));       
	             $result = $socialService->getConnectedSocialNetworks();			 
				 return array('userConnection'=>$result);
        } catch ( Exception $ex ) {
        }
    }
}