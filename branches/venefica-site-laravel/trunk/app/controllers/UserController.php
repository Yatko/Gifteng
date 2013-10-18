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
		$session = Session::get('user');
        $user_setting['userId'] = $session['data']->id;
        $user_setting['notifiableTypes'] = Input::get('notifiableTypes');
		try {
			$userService = new SoapClient(Config::get('wsdl.user'),array());
            $result = $userService->saveUserSetting($user_setting);
			
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

            return Response::json($result->message);
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
	
}	