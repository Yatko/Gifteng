<?php

class AdController extends \BaseController {

	/**
	 * Ad listing.
	 *
	 * @return Response
	 */
	public function index() {
		try {
			$session = Session::get('user');
			$filter = new Filter;
			$filter->includeOwned=1;
			$filter->includeCannotRequest=1;
			$filter->includeInactive=0;
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$result = $adService -> getAdsExDetail(array("lastIndex" => -1, "numberAds" => 15, "filter" => $filter, "includeImages" => true, "includeCreator" => true, "includeCommentsNumber" => 2));
			
			$ads = array();
			$creators = array();

			foreach($result->ad as $k=>$v) {
				$creators[$v->creator->id]=$v->creator;
				$v->creatorname=$v->creator->firstName." ".$v->creator->lastName;
				$v->creator=$v->creator->id;
				if($v->creator==$session['data']->id) {
					$v->self=true;
				}
				$ads[]=$v;
			}
			return array('ads'=>$ads,'creators'=>$creators);
		} catch ( Exception $ex ) {
			throw new Exception($ex -> getMessage());
		}
	}
	
	/**
	 * Load more
	 * 
	 * @param int $last
	 * @return Response
	 */ 
	public function loadMore($last=-1) {
		try {
			$filter = new Filter;
			$filter->includeOwned=1;
			$filter->includeCannotRequest=1;
			$filter->includeInactive=0;
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$result = $adService -> getAdsExDetail(array("lastIndex" => $last, "numberAds" => 15, "filter" => $filter, "includeImages" => true, "includeCreator" => true, "includeCommentsNumber" => 2));
			
			$ads = array();
			$creators = array();

			foreach($result->ad as $k=>$v) {
				$creators[$v->creator->id]=$v->creator;
				$v->creatorname=$v->creator->firstName." ".$v->creator->lastName;
				$v->creator=$v->creator->id;
				$ads[]=$v;
			}
			return Response::json(array('ads'=>$ads,'creators'=>$creators));
		} catch ( Exception $ex ) {
			return Response::json(array());
		}
	}
	
	

	/**
	 * Ads giving by user
	 *
	 * @return Response
	 */
	public function byUser($user_id) {
		try {
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$userService = new SoapClient(Config::get('wsdl.user'), array());
			
			$result = $adService -> getUserAds(array(
                "userId" => $user_id,
                'numberAds'=>0,
                "includeRequests" => true,
                "includeUnapproved" => false
            ));

			if(isset($result->ad->type))
				$ads = $result;
			else
				$ads = $result->ad;
			
			return Response::json($ads);
		} catch ( Exception $ex ) {
			return Response::json(array());
		}
	}
	
	/**
	 * Ads request by user
	 * 
	 * @return Response
	 */
	public function requestedByUser($user_id) {
		try {
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$result = $adService -> getUserRequestedAds(array(
                "userId" => $user_id,
                'numberAds'=>-1,
                "includeRequests" => true,
                "includeUnapproved" => false
            ));

			if(isset($result->ad->type))
				$result=$result;
			else
				$result=$result->ad;

			$ads = array();
			$creators = array();

			foreach($result as $k=>$row) {
				$ad = $row;
				
				$requests = get_object_vars($ad->requests);
				
				if(is_array($requests['item'])) {
					foreach($requests['item'] as $request) {
			 			$request = get_object_vars($request);
						$usr = get_object_vars($request['user']);
						if($usr['id']==$user_id) {
							$ad->status=$request['status'];
							$ad->requestId=$request['id'];
						}
					}
				}
				else {
					$ad->status = $ad->requests->item->status;
							$ad->requestId=$ad->requests->item->id;
				}
				
				$creators[$ad->creator->id]=$ad->creator;
				$ad->creator=$ad->creator->id;
				$ads[]=$ad;
			}
			return Response::json(array('ads'=>$ads,'creators'=>$creators));
		} catch ( Exception $ex ) {
			return Response::json(array());
		}
	}
	
	/**
	 * Ads request by user
	 * 
	 * @return Response
	 */
	public function bookmarkedByUser($user_id) {
		try {
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$result = $adService -> getBookmarkedAds(array(
                "userId" => $user_id
            ));

			if(isset($result->ad->type))
				$result=$result;
			else
				$result=$result->ad;

			$ads = array();
			$creators = array();

			foreach($result as $k=>$row) {
				$ad = $row;
				if($ad->status == 'FINALIZED') {
					$requests = get_object_vars($ad->requests);
					
					if(is_array($requests['item'])) {
						foreach($requests['item'] as $request) {
				 			$request = get_object_vars($request);
							$usr = get_object_vars($request['user']);
							if($usr['id']==$user_id) {
								$ad->status=$request['status'];
							}
						}
					}
					else {
						$ad->status = $ad->requests->item->status;
					}
				}
				$creators[$ad->creator->id]=$ad->creator;
				$ad->creator=$ad->creator->id;
				$ads[]=$ad;
			}
			return Response::json(array('ads'=>$ads,'creators'=>$creators));
		} catch ( Exception $ex ) {
			return Response::json(array());
		}
	}

	/**
	 * Store a newly created ad.
	 *
	 * @return Response
	 */
	public function store() {
        try {
			$session = Session::get('user');
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$ad = new Ad(Input::get('ad'));
			
	        $ad->image = ImageModel::createImageModel(str_replace('api/image/','',$ad->image['url']));
			
            $result = $adService->placeAd(array("ad" => $ad));
        } catch ( Exception $ex ) {
			throw new Exception($ex -> getMessage());
        }
	}

	/**
	 * Display a specified ad.
	 *
	 * @param int $id
	 * @return Response
	 */
	public function show($id) {
		try {
			$session = Session::get('user');
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$messageService = new SoapClient(Config::get('wsdl.message'), array());
			$result = $adService -> getAdById(array("adId" => $id));
			$ad = new Ad($result->ad);
			
			if($ad->requested) {
				$request = $ad->getRequestByUser($session['data']->id);
				if(isset($request)) {
					$ad->status = $request->status;
					$ad->user_request = $request;
					$ad->user_requested = true;
				}
				else {
					$ad->user_requested = false;
				}
			}
			
			$ad->canEdit = $ad->owner && !$ad->hasActiveRequest() && $ad->statistics->numBookmarks == 0 && $ad->statistics->numComments == 0 && $ad->statistics->numShares == 0;
			$ad->canDelete = $ad->owner && (!$ad->hasActiveRequest() || $ad->expired);
			
        	$comments = $messageService->getCommentsByAd(array(
                "adId" => $ad->id,
                "lastCommentId" => -1,
                "numComments" => 11));
				
			return array('ad'=>$ad,'comments'=>$comments);
		} catch ( Exception $ex ) {
			return $ex;
		}
	}

	/**
	 * Update a specified ad.
	 *
	 * @param int $id
	 * @return Response
	 */
	public function update($id) {

	}

	/**
	 * Remove a specified ad.
	 *
	 * @param int $id
	 * @return Response
	 */
	public function destroy($id) {
        try {
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
            $adService->deleteAd(array("adId" => $id));
        } catch ( Exception $ex ) {
        }
	}
	
	/**
	 * Bookmark an ad
	 * 
	 * @param int $id
	 * @return Response
	 */
	public function bookmark($id) {
        try {
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
            $adService->bookmarkAd(array("adId" => $id));
        } catch ( Exception $ex ) {
        }
	}
	
	/**
	 * Unbookmark an ad
	 * 
	 * @param int $id
	 * @return Response
	 */
	public function unbookmark($id) {
        try {
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
            $adService->removeBookmark(array("adId" => $id));
        } catch ( Exception $ex ) {
        }
	}
	
	/**
	 * Comment ad
	 * 
	 * @param int $id
	 * @return Response
	 */
	public function comment($id) {
        try {
            $messageService = new SoapClient(Config::get('wsdl.message'),array());
            $comment = new Comment;
            $comment->text = Input::get('text');
			
            $result = $messageService->addCommentToAd(array(
                "adId" => $id,
                "comment" => $comment
            ));
        } catch ( Exception $ex ) {
        }
	}
	
	/**
	 * Relist ad
	 * 
	 * @param int $id
	 * @return Response
	 */
	public function relist($id) {
        try {
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
            $adService->relistAd(array("adId" => $id));
        } catch ( Exception $ex ) {
        }
	}
	
	/**
	 * Request create
	 * 
	 * @param int $id
	 * @return Response
	 */
	public function request($id) {
        try {
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
            $adService->requestAd(array("adId" => $id,"text" => Input::get('text')));
        } catch ( Exception $ex ) {
        }
	}
	
	/**
	 * Request cancel
	 * 
	 * @param int $id
	 * @return Response
	 */
	public function request_cancel($id) {
        try {
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
            $adService->cancelRequest(array("requestId" => $id));
        } catch ( Exception $ex ) {
        }
	}
	
	/**
	 * Request select
	 * 
	 * @param int $id
	 * @return Response
	 */
	public function request_select($id) {
        try {
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
            $adService->selectRequest(array("requestId" => $id));
        } catch ( Exception $ex ) {
        }
	}
	
	/**
	 * Request send
	 * 
	 * @param int $id
	 * @return Response
	 */
	public function request_send($id) {
        try {
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
            $adService->markAsSent(array("requestId" => $id));
        } catch ( Exception $ex ) {
        }
	}
	
	/**
	 * Request receive
	 * 
	 * @param int $id
	 * @return Response
	 */
	public function request_receive($id) {
        try {
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
            $adService->markAsReceived(array("requestId" => $id));
        } catch ( Exception $ex ) {
        }
	}
}
