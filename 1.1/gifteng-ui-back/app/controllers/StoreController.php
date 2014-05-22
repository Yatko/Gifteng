<?php

class StoreController extends \BaseController {
	 
	 /**
	  * List stores
	  * 
	  * @return Response
	  */ 
	  public function index() {
	  }
	 
	 /**
	  * Add store
	  * 
	  * @return Response
	  */
	  public function store() {
		$session = Session::get('user');
		$userService = new SoapClient(Config::get('wsdl.user'),array());
		$currentUser = $userService->getUserByEmail(array('email'=>$session['data']->email))->user;
		
        $address = new Address();
        $address->name = Input::get('name');
        $address->address1 = Input::get('address1');
        $address->address2 = Input::get('address2');
        $address->city = Input::get('city');
        $address->state = Input::get('state');
        $address->zipCode = Input::get('zipCode');
		if(isset($currentUser->addresses) && is_object($currentUser->addresses)) {
			$currentUser->addresses = array($currentUser->addresses);
		}
		if(isset($currentUser->addresses))
			$addresses = $currentUser->addresses;
		else
			$addresses = array();
		
        array_push($addresses, $address);
		$currentUser->addresses = $addresses;
		
		try {
            $userService->updateUser(array('user'=>$currentUser));
        } catch ( Exception $ex ) {
        }
	  }
	  

	  /**
	   * Update a specified store.
	   *
	   * @param int $id
	   * @return Response
	   */
	  public function update($id) {
		$session = Session::get('user');
		$userService = new SoapClient(Config::get('wsdl.user'),array());
		$currentUser = $userService->getUserByEmail(array('email'=>$session['data']->email))->user;
		
		
		if(is_object($currentUser->addresses)) {
			$currentUser->addresses = array($currentUser->addresses);
		}
		$addresses = $currentUser->addresses;
		
        $address = new Address();
        $address->name = Input::get('name');
        $address->address1 = Input::get('address1');
        $address->address2 = Input::get('address2');
        $address->city = Input::get('city');
        $address->state = Input::get('state');
        $address->zipCode = Input::get('zipCode');
		
		foreach($addresses as $k=>$val) {
			if($val->id==$id) {
				$addresses[$k]=$address;
				break;
			}
		}
		
		$currentUser->addresses = $addresses;
		
		try {
            $userService->updateUser(array('user'=>$currentUser));
        } catch ( Exception $ex ) {
        }
	  }
	  
	  
	  /**
	   * Delete store
	   * 
	   * @return Response
	   */
	   public function destroy($id) {
		$session = Session::get('user');
		$userService = new SoapClient(Config::get('wsdl.user'),array());
		$currentUser = $userService->getUserByEmail(array('email'=>$session['data']->email))->user;
		
		if(is_object($currentUser->addresses)) {
			$currentUser->addresses = array($currentUser->addresses);
		}
		$addresses = $currentUser->addresses;
		
		foreach($addresses as $k=>$val) {
			if($val->id==$id) {
				$val->deleted =true;
				$addresses[$k] = $val;
			}
		}
		
		$currentUser->addresses = $addresses;
		
		try {
            $userService->updateUser(array('user'=>$currentUser));
        } catch ( Exception $ex ) {
        }
	   }
		 
}