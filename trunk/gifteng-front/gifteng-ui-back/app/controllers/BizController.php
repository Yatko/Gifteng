<?php

class BizController extends \BaseController {
	
	public function register() {
		
        $address = new Address();
        $address->zipCode = Input::get('zipCode');
		
		$password = Input::get('password');
		
        $user = new UserModel();
        $user->businessName = Input::get('businessName');
        $user->contactName = Input::get('contactName');
        $user->phoneNumber = Input::get('phoneNumber');
        $user->email = Input::get('email');
        $user->address = $address;
        $user->businessCategoryId = Input::get('businessCategory');
		
        try {
			$userService = new SoapClient(Config::get('wsdl.user'));
            $result = $userService->registerBusinessUser(array(
                "user" => $user,
                "password" => $password
            ));
			return Response::json($result);
        } catch ( Exception $ex ) {
            throw new Exception($ex->faultstring);
        }
	}
	
	
	/**
	 * Update profile
	 * 
	 * @return Response
	 */
	public function updateProfile() {
		$session = Session::get('user');
		$userService = new SoapClient(Config::get('wsdl.user'),array());
		$geoService = new SoapClient(Config::get('wsdl.utility'), array());
		$currentUser = $userService->getUserByEmail(array('email'=>$session['data']->email))->user;
		
		$currentUser->businessName = Input::get('businessName');
		$currentUser->about = Input::get('about');
		
		$currentUser->address = $geoService -> getAddressByZipcode(array("zipcode" => Input::get('zipCode'))) -> address;
		
		$currentUser->website = Input::get('website');
		$currentUser->contactName = Input::get('contactName');
		$currentUser->contactNumber = Input::get('contactNumber');
		$currentUser->email = Input::get('email');
		
		try {
            $userService->updateUser(array('user'=>$currentUser));
        } catch ( Exception $ex ) {
        }
		
	}
	
}
