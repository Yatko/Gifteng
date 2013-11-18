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
			
		
			if(!isset($follow_users->following->type))
				$follow_users = $follow_users->following;
			
			$follow_ads = array();
            foreach ($follow_users as $following) {
                try {
					$adService = new SoapClient(Config::get('wsdl.ad'),array());
					$ads = $adService->getUserAds(array(
		                "userId" => $following->id,
		                "numberAds"=>3,
		                "includeRequests" => false,
		                "includeUnapproved" => false
		            ));
					
					if(!isset($ads->ad->type)) {
						$ads=$ads->ad;
					} else {
						$ads=array($ads->ad);
					}
					
                    $follow_ads[$following->id] = $ads;
                } catch ( Exception $ex ) {
                }
            }
		}
		catch ( Exception $ex ) {
            $follow_users = array();
            $follow_ads = array();
		}
		return Response::json(array('following'=>$follow_users,'ads'=>$follow_ads));
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
		
			if(!isset($follow_users->follower->type))
				$follow_users = $follow_users->follower;
			
			$follow_ads = array();
            foreach ($follow_users as $follower) {
                try {
					$adService = new SoapClient(Config::get('wsdl.ad'),array());
					$ads = $adService->getUserAds(array(
		                "userId" => $follower->id,
		                "numberAds"=>3,
		                "includeRequests" => false,
		                "includeUnapproved" => false
		            ));
					
					if(!isset($ads->ad->type)) {
						$ads=$ads->ad;
					}

                    $follow_ads[$follower->id] = $ads;
                } catch ( Exception $ex ) {
                }
            }
		}
		catch ( Exception $ex ) {
            $follow_users = array();
            $follow_ads = array();
		}
		
		return Response::json(array('follower'=>$follow_users,'ads'=>$follow_ads));
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
			if(isset($result->message->type)) {
				$messages = array($result->message);
			}
			else {
				$messages = $result->message;
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
		
		$address=$geoService -> getAddressByZipcode(array("zipcode" => Input::get('zipCode')));
		
		$currentUser->address = $address->address;
		
		try {
            $userService->updateUser(array('user'=>$currentUser));
        } catch ( Exception $ex ) {
        }
		
	}
	
}	