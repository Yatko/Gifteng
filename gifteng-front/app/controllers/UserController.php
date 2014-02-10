<?php

class UserController extends \BaseController {

	/**
	 * Display a specified user.
	 *
	 * @param int $id
	 * @return Response
	 */
	public function getProfile($id) {
		try {
			$userService = new SoapClient(Config::get('wsdl.user'));
			$result = $userService->getUserById(array("userId" => $id));
			return Response::json($result->user);
		} catch ( InnerException $ex ) {
		    throw new Exception($ex->faultstring);
		}
	}

	/**
	 * Following listing
	 *
	 * @return Response
	 */
	public function getFollowing($id) {
		try {
			$userService = new SoapClient(Config::get('wsdl.user'),array());
			$follow_users = $userService->getFollowings(array("userId" => $id));
			
		
			if(!isset($follow_users->following->type) && isset($follow_users->following))
				$follow_users = $follow_users->following;
		}
		catch ( Exception $ex ) {
            $follow_users = array();
		}
		return Response::json(array('following'=>$follow_users));
	}
	 
	/**
	 * Followers listing
	 *
	 * @return Response
	 */
	public function getFollowers($id) {
		try {
			$userService = new SoapClient(Config::get('wsdl.user'),array());
			$follow_users = $userService->getFollowers(array("userId" => $id));
		
			if(!isset($follow_users->follower->type) && isset($follow_users->follower))
				$follow_users = $follow_users->follower;
		}
		catch ( Exception $ex ) {
            $follow_users = array();
		}
		
		return Response::json(array('follower'=>$follow_users));
	}
	
	/**
	 * Notifications listing
	 *
	 * @return Response
	 */
	public function getNotifications() {
		try {
			$userService = new SoapClient(Config::get('wsdl.user'),array());
			$result = $userService->getUserSetting();
			
			return Response::json($result->setting);
		}
		catch ( Exception $ex ) {
			return Response::json(array());
		}
	}
	
	/**
	 * Ratings listing
	 *
	 * @return Response
	 */
	public function getRatings($id) {
			//TODO: This is poorly written. Can be optimized
			//__krasi
		
		try {
			$AdService = new SoapClient(Config::get('wsdl.ad'),array());
			
			$ratings['giver'] = $AdService->getReceivedRatingsAsOwner(array("userId" => $id));
			if(isset($ratings['giver']->rating)) {
				if(is_array($ratings['giver']->rating)) {
					$ratings['giver']=$ratings['giver']->rating;
				}
				else {
					$ratings['giver']=array($ratings['giver']->rating);
				}
			}
			
			$ratings['receiver'] = $AdService->getReceivedRatingsAsReceiver(array("userId" => $id));
			if(isset($ratings['receiver']->rating)) {
				if(is_array($ratings['receiver']->rating)) {
					$ratings['receiver']=$ratings['receiver']->rating;
				}
				else {
					$ratings['receiver']=array($ratings['receiver']->rating);
				}
			}

			$all_ratings=array();
			$stats = array('all'=>array('neutral'=>0,'positive'=>0,'negative'=>0),'giver'=>array('neutral'=>0,'positive'=>0,'negative'=>0),'receiver'=>array('neutral'=>0,'positive'=>0,'negative'=>0));
			foreach($ratings['receiver'] as $k=>$v) {
				$v->owner=false;
				$all_ratings[] = $v;
				switch($v->value) {
					case 0:
						$stats['all']['neutral']++;
						$stats['receiver']['neutral']++;
						break;
					case -1:
						$stats['all']['negative']++;
						$stats['receiver']['negative']++;
						break;
					case 1:
						$stats['all']['positive']++;
						$stats['receiver']['positive']++;
						break;
				}
			}
			foreach($ratings['giver'] as $k=>$v) {
				$v->owner=true;
				$all_ratings[] = $v;
				switch($v->value) {
					case 0:
						$stats['all']['neutral']++;
						$stats['giver']['neutral']++;
						break;
					case -1:
						$stats['all']['negative']++;
						$stats['giver']['negative']++;
						break;
					case 1:
						$stats['all']['positive']++;
						$stats['giver']['positive']++;
						break;
				}
			}

			return Response::json(array('ratings'=>$all_ratings,'stats'=>$stats));
		}
		catch ( Exception $ex ) {
			return Response::json(array('ex'=>$ex));
		}
	}
	
	/**
	 * Notifications saving
	 * 
	 * @return Response
	 */
	public function setNotifications() {
		try {
			$session = Session::get('user');
			$userService = new SoapClient(Config::get('wsdl.user'),array());
			$user_setting = new UserSetting;
			$user_setting->userId = $session['data']->id;
			$user_setting->notifiableTypes = Input::get('notifiableTypes');
            $result = $userService->saveUserSetting(array("setting" => $user_setting));
			
			return Response::json($result);
        } catch ( Exception $ex ) {
			return Response::json(array('error'=>$ex->faultstring));
        }
	}
	
	
	/**
	 * Messages listing
	 *
	 * @return Response
	 */
	public function getMessages() {
		try {
            $messageService = new SoapClient(Config::get('wsdl.message'),array());
            $result = $messageService->getLastMessagePerRequest();
			
			$messages=array();
			if(isset($result->message)) {
				if(isset($result->message->type)) {
					$messages = array($result->message);
				}
				else {
					$messages = $result->message;
				}
			}
			
            return Response::json($messages);
        } catch ( Exception $ex ) {
			return Response::json(array());
        }
	}
	
	/**
	 * Single Message listing
	 *
	 * @return Response
	 */
	public function getMessage($id) {
		try {
            $messageService = new SoapClient(Config::get('wsdl.message'),array());
            $result = $messageService->getMessagesByRequest(array("requestId" => $id));

			if(isset($result->message->type))
				$return = array($result->message);
			else
				$return = $result->message;
			
			return Response::json($return);
        } catch ( Exception $ex ) {
			return Response::json(array());
        }
	}
	
	/**
	 * Single Message listing
	 *
	 * @return Response
	 */
	public function hideMessage($id) {
		try {
            $messageService = new SoapClient(Config::get('wsdl.message'),array());
            $result = $messageService->hideRequestMessages(array("requestId" => $id));
        } catch ( Exception $ex ) {
        }
	}
	
	/**
	 * Send Message
	 * 
	 * @return Response
	 */
	public function sendMessage() {
		try {
			$message = new Message();
			$message->text = Input::get('text');
			$message->requestId = Input::get('requestId');
			$message->toId = Input::get('toId');
            $messageService = new SoapClient(Config::get('wsdl.message'),array());
            $messageService->sendMessage(array("message" => $message));
			return array('success'=>true);
        } catch ( Exception $ex ) {
			return Response::json($ex);
        }
	}
	
	/**
	 * Set Follow
	 *
	 * @return Response
	 */
	public function setFollow($id) {
		try {
            $userService = new SoapClient(Config::get('wsdl.user'),array());
            $userService->follow(array("userId" => $id));

            return Response::json(array('success'=>true));
        } catch ( Exception $ex ) {
			return Response::json(array());
        }
	}
	
	/**
	 * Set Unfollow
	 *
	 * @return Response
	 */
	public function setUnfollow($id) {
		try {
            $userService = new SoapClient(Config::get('wsdl.user'),array());
            $userService->unfollow(array("userId" => $id));

            return Response::json(array('success'=>true));
        } catch ( Exception $ex ) {
			return Response::json(array());
        }
	}
	
	/**
	 * Reinitialize user data
	 * 
	 * @return Response
	 */
	public function reinit() {
		try {
			$session = Session::get('user');
            $userService = new SoapClient(Config::get('wsdl.user'));
            $result = $userService->getUserByEmail(array("email" => $session['data']->email));
            $user = $result->user;
			if($user->businessAccount==true) {
				if(isset($user->addresses) && is_object($user->addresses)) {
					$user->addresses = array($user->addresses);
				}
			}
			Session::put('user.data',$user);
		
			return Response::json(array_merge(Session::get('user'),array('logged'=>true)));
			
        } catch ( InnerException $ex ) {
        }
	}
	
	/**
	 * Top Giftengers
	 * 
	 * @return Response
	 */
	public function top() {
        try {
			$session = Session::get('user');
            $userService = new SoapClient(Config::get('wsdl.user'));
            $result = $userService->getTopUsers(array("numberUsers" => 11));
			$users=array();
			foreach($result->users as $user) {
				if($user->id==$session['data']->id) {
					$user->self=true;
				}
				else {
					$user->self=false;
				}
				$users[]=$user;
			}
        } catch ( Exception $ex ) {
            $users = array();
        }
		
		return Response::json($users);
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
		
        if ( !$currentUser->address ) {
            $address = new Address;
        } else {
            $address = new Address($currentUser->address);
        }
		
		$currentUser->firstName = Input::get('first_name');
		$currentUser->lastName = Input::get('last_name');
		$currentUser->about = Input::get('about');
		
		if(Input::get('zipCode')) {
			$address=$geoService -> getAddressByZipcode(array("zipcode" => Input::get('zipCode')));
			
			$currentUser->address = $address->address;
		}
		
		try {
            $userService->updateUser(array('user'=>$currentUser));
        } catch ( Exception $ex ) {
        }
		
	}
	   
   /**
     * 
     * @param string $oldPassword
     * @param string $newPassword
     * @throws Exception
     */
    public function changePassword() {
        try {
	  		$authService = new SoapClient(Config::get('wsdl.auth'));
            $authService->changePassword(array(
                "oldPassword" => Input::get('old_password'),
                "newPassword" => Input::get('password')
            ));
			return array('status'=>'valid');
        } catch ( Exception $ex ) {
			return array('status'=>'invalid');
        }
    }
	/**
     * Sends a forgot password request to the server. The server generates a link
     * that will enable to change user password.
     * 
     * @param string $email
     * @param string $ipAddress
     * @throws Exception incorrect email or in case of WS error
     */
    public function forgotPasswordEmail() {
        try {
            $authService = new SoapClient(Config::get('wsdl.auth'));
            $authService->forgotPasswordEmail(array(
                "email" => Input::get('email_address'),
                "ipAddress" => Input::get('ip_address')
            ));
			  return array('status'=>'valid');
        } catch ( Exception $ex ) {
            return array('status'=>'invalid');
			
        }
    }
	/**
     * 
     * @throws Exception
     */
    public function resendVerification() {
        try {
            $userService = new SoapClient(Config::get('wsdl.user'));
            $userService->resendVerification();
        } catch ( Exception $ex ) {
            
        }
    }
	/**
     * 
     * @throws Exception
     */
    public function verifyUser() {
        try {
            $userService = new SoapClient(Config::get('wsdl.user'));
            $userService->verifyUser(Input::get('code'));
        } catch ( Exception $ex ) {
        }
    }
	
}	