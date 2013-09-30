<?php

class AuthController extends \BaseController {
	 
	 /**
	  * Provides user details
	  * 
	  * @return Response
	  */ 
	  public function index() {
	  	if(Session::get('user.token')) {
	  		return array('logged'=>true);
	  	}
		else {
			return array('logged'=>false);
		}
	  }
	 
	 /**
	  * Authenticate the user
	  * 
	  * @return Response
	  */
	  public function store() {
	  	try {
		  	$authService = new SoapClient(Config::get('wsdl.auth'));
	        $token = $authService->authenticateEmail(
	        	array(
	        		"email" => Input::get('email'),
	            	"password" => Input::get('password')
				)
			);
	        ini_set('soap.wsdl_cache_enabled', '0');
			ini_set('user_agent', "PHP-SOAP/".PHP_VERSION."\r\n"."AuthToken: ".$token->AuthToken);
			Session::put('user.token', $token);
			return array('success'=>'true');
	    } catch ( Exception $ex ) {
	        return array('error'=>$ex->faultstring);
	    }
	  }
	  
	  /**
	   * Destroy user session
	   * 
	   * @return Response
	   */
	   public function destroy($id) {
	  	if(Session::get('user.token')) {
	  		Session::flush();
	  	}
		return array('logged'=>false);
	   }
		 
}