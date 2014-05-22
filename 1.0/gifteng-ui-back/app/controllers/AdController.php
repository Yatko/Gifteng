<?php

class AdController extends \BaseController {

	const TYPE_NEWEST = 'newest';
	const TYPE_OLDEST = 'oldest';
	const TYPE_CLOSEST = 'closest';
	const TYPE_GIFTED = 'gifted';

	/**
	 * Ad listing.
	 *
	 * @return Response
	 */
	public function index() {
		try {
			$session = Session::get('user');
			$userService = new SoapClient(Config::get('wsdl.user'),array());
			$currentUser = $userService->getUserByEmail(array('email'=>$session['data']->email))->user;
		
			$filter = new Filter;
			$filter -> includeOwned = 1;
			$filter -> includeCannotRequest = 1;
			$filter -> includeInactive = 0;
			$filter -> includePickUp = null;
			$filter -> includeShipping = null;
			$type = Input::get('order');
        	$filter->searchString = Input::get('keywords');
			if(Input::get('category')) {
				 $category = array(Input::get('category'));
			}
			else {
				$category = null;
			}
			
			if($delivery = Input::get('delivery')) {
				if($delivery=="pickup") {
					$filter -> includePickUp = 1;
					$filter -> includeShipping = 0;
				} else if($delivery=="shipping") {
					$filter -> includePickUp = 0;
					$filter -> includeShipping = 1;
				}
			}
			$filter->categories = $category;

			if (!$type)
				$type = AdController::TYPE_NEWEST;

			if ($type == AdController::TYPE_NEWEST) {
				$filter -> orderAsc = false;
				$filter -> orderClosest = false;
				$filter -> filterType = Filter::FILTER_TYPE_ACTIVE;
			} else if ($type == AdController::TYPE_OLDEST) {
				$filter -> orderAsc = true;
				$filter -> orderClosest = false;
				$filter -> filterType = Filter::FILTER_TYPE_ACTIVE;
			} else if ($type == AdController::TYPE_CLOSEST) {
				$filter -> orderAsc = true;
				$filter -> orderClosest = true;
				$filter -> filterType = Filter::FILTER_TYPE_ACTIVE;
				$filter -> latitude = ($currentUser != null && $currentUser -> address != null) ? $currentUser -> address -> latitude : null;
				$filter -> longitude = ($currentUser != null && $currentUser -> address != null) ? $currentUser -> address -> longitude : null;
			} else if ($type == AdController::TYPE_GIFTED) {
				$filter -> orderAsc = false;
				$filter -> orderClosest = false;
				$filter -> filterType = Filter::FILTER_TYPE_GIFTED;
			}

			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$result = $adService -> getAdsExDetail(array("lastIndex" => -1, "numberAds" => 15, "filter" => $filter, "includeImages" => true, "includeCreator" => true, "includeCommentsNumber" => 2));

			$creators = array();
			
			if (isset($result -> ad -> type))
				$ads = $result;
			elseif(isset($result->ad))
				$ads = $result -> ad;
			else
				$ads = array();
			
			$results = array();
			
			foreach ($ads as $k => $ad) {
				
				$ad = new Ad($ad);
				
				$creators[$ad -> creator -> id] = $ad -> creator;
				
				if (
				            isset($currentUser->address) &&
				            isset($currentUser->address->latitude) &&
				            isset($currentUser->address->longitude) &&
				            isset($ad->address) &&
				            isset($ad->address->latitude) &&
				            isset($ad->address->longitude)
				        ) {
					$ad->distance = $this->distance_haversine(
		                $currentUser->address->latitude,
		                $currentUser->address->longitude,
		                $ad->address->latitude,
		                $ad->address->longitude
		            );
				}
				
				if(isset($ad -> requests)) {
					if (is_array($ad -> requests -> item)) {
						foreach ($ad->requests->item as $k => $v) {
							$v = new Requested($v);
							if ($v -> user -> id == $user_id) {
								$ad -> request = $v;
							}
						}
	
					} else {
						$v = new Requested($ad -> requests -> item);
						if ($v -> user -> id == $user_id) {
							$ad -> request = $v;
						}
					}
				}

				if (!$ad -> online) {
					if ($ad -> approved) {
						$ad -> status = "APPROVED";
					} else if ($ad -> approval == null) {
						$ad -> status = "PENDING";
					} else {
						$ad -> status = "DECLINED";
					}
				} elseif ($ad -> sold && $ad->getAcceptedRequest()) {
					$requestor = $ad -> getAcceptedRequest() -> user;
					$request = $ad -> getAcceptedRequest();
					if (isset($requestor -> avatar))
						$ad -> requestor = $requestor -> avatar -> id;
					else
						$ad -> requestor = null;
					
					if(isset($request))
						$ad -> request = $request;
					else
						$ad -> request = null;

					$ad -> status = "SOLD";
				} elseif ($ad -> expired) {
					$ad -> status = "EXPIRED";
				} elseif (!$ad -> hasActiveRequest()) {
					$ad -> status = "NO_REQUESTS";
				} elseif ($ad -> hasSentRequest()) {
					$requestor = $ad -> getAcceptedRequest() -> user;
					if (isset($requestor -> avatar))
						$ad -> requestor = $requestor -> avatar -> id;
					else
						$ad -> requestor = null;
					
						if(isset($request))
							$ad -> request = $request;
						else
							$ad -> request = null;

					$ad -> status = "SENT";
				} elseif ($ad -> hasAcceptedRequest()) {
					$requestor = $ad -> getAcceptedRequest() -> user;
					if (isset($requestor -> avatar))
						$ad -> requestor = $requestor -> avatar -> id;
					else
						$ad -> requestor = null;
					
					if(isset($request))
						$ad -> request = $request;
					else
						$ad -> request = null;

					$ad -> status = "SELECTED";
				} else {
					$ad -> status = "ACTIVE";
				}
				
				if(isset($ad -> request)) {
					$request = $ad -> request;
					if ($ad -> expired || $request -> isExpired) {
						if ($ad -> request -> adStatus == "EXPIRED") {
							$ad -> reqStat = "EXPIRED";
						} else if ($ad -> request -> isCanceled) {
							$ad -> reqStat = "CANCELED";
						} else {
							$ad -> reqStat = "DECLINED";
						}
					} else if ($ad -> sold) {
						$ad -> reqStat = "RECEIVED";
					} else if ($request -> isPending) {
						$ad -> reqStat = "PENDING";
					} else if ($request -> accepted) {
						$ad -> reqStat = "ACCEPTED";
					}
				}

				if($filter -> filterType == Filter::FILTER_TYPE_GIFTED) {
					$ad->sold=true;
				}
				
				$results[] = $ad;
			}
			
			
			return array('ads' => $results, 'creators' => $creators);
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
	public function loadMore($last = -1) {
		try {
			$filter = new Filter;
			$filter -> includeOwned = 1;
			$filter -> includeCannotRequest = 1;
			$filter -> includeInactive = 0;
			$filter -> includePickUp = null;
			$filter -> includeShipping = null;
			$type = Input::get('order');
        	$filter->searchString = Input::get('keywords');
			if(Input::get('category')) {
				 $category = array(Input::get('category'));
			}
			else {
				$category = null;
			}
			
			if($delivery = Input::get('delivery')) {
				if($delivery=="pickup") {
					$filter -> includePickUp = 1;
					$filter -> includeShipping = 0;
				} else if($delivery=="shipping") {
					$filter -> includePickUp = 0;
					$filter -> includeShipping = 1;
				}
			}
			$filter->categories = $category;

			if (!$type)
				$type = AdController::TYPE_NEWEST;

			if ($type == AdController::TYPE_NEWEST) {
				$filter -> orderAsc = false;
				$filter -> orderClosest = false;
				$filter -> filterType = Filter::FILTER_TYPE_ACTIVE;
			} else if ($type == AdController::TYPE_OLDEST) {
				$filter -> orderAsc = true;
				$filter -> orderClosest = false;
				$filter -> filterType = Filter::FILTER_TYPE_ACTIVE;
			} else if ($type == AdController::TYPE_CLOSEST) {
				$filter -> orderAsc = true;
				$filter -> orderClosest = true;
				$filter -> filterType = Filter::FILTER_TYPE_ACTIVE;
				$filter -> latitude = ($currentUser != null && $currentUser -> address != null) ? $currentUser -> address -> latitude : null;
				$filter -> longitude = ($currentUser != null && $currentUser -> address != null) ? $currentUser -> address -> longitude : null;
			} else if ($type == AdController::TYPE_GIFTED) {
				$filter -> orderAsc = false;
				$filter -> orderClosest = false;
				$filter -> filterType = Filter::FILTER_TYPE_GIFTED;
			}
			
			
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$result = $adService -> getAdsExDetail(array("lastIndex" => $last, "numberAds" => 15, "filter" => $filter, "includeImages" => true, "includeCreator" => true, "includeCommentsNumber" => 2));

			$creators = array();
			
			if (isset($result -> ad -> type))
				$ads = $result;
			elseif(isset($result->ad))
				$ads = $result -> ad;
			else
				$ads = array();
			
			$results = array();
			
			foreach ($ads as $k => $ad) {
				
				$ad = new Ad($ad);
				
				$creators[$ad -> creator -> id] = $ad -> creator;
				
				if (
				            isset($currentUser->address) &&
				            isset($currentUser->address->latitude) &&
				            isset($currentUser->address->longitude) &&
				            isset($ad->address) &&
				            isset($ad->address->latitude) &&
				            isset($ad->address->longitude)
				        ) {
					$ad->distance = $this->distance_haversine(
		                $currentUser->address->latitude,
		                $currentUser->address->longitude,
		                $ad->address->latitude,
		                $ad->address->longitude
		            );
				}
				
				if(isset($ad -> requests)) {
					if (is_array($ad -> requests -> item)) {
						foreach ($ad->requests->item as $k => $v) {
							$v = new Requested($v);
							if ($v -> user -> id == $user_id) {
								$ad -> request = $v;
							}
						}
	
					} else {
						$v = new Requested($ad -> requests -> item);
						if ($v -> user -> id == $user_id) {
							$ad -> request = $v;
						}
					}
				}

				if (!$ad -> online) {
					if ($ad -> approved) {
						$ad -> status = "APPROVED";
					} else if ($ad -> approval == null) {
						$ad -> status = "PENDING";
					} else {
						$ad -> status = "DECLINED";
					}
				} elseif ($ad -> sold && $ad->getAcceptedRequest()) {
					$requestor = $ad -> getAcceptedRequest() -> user;
					$request = $ad -> getAcceptedRequest();
					if (isset($requestor -> avatar))
						$ad -> requestor = $requestor -> avatar -> id;
					else
						$ad -> requestor = null;
					
					if(isset($request))
						$ad -> request = $request;
					else
						$ad -> request = null;

					$ad -> status = "SOLD";
				} elseif ($ad -> expired) {
					$ad -> status = "EXPIRED";
				} elseif (!$ad -> hasActiveRequest()) {
					$ad -> status = "NO_REQUESTS";
				} elseif ($ad -> status == 'FINALIZED') {
					if(isset($ad->requests)) {
						$requests = get_object_vars($ad -> requests);

						if (is_array($requests['item'])) {
							foreach ($requests['item'] as $request) {
								$request = get_object_vars($request);
								$usr = get_object_vars($request['user']);
								if ($usr['id'] == $user_id) {
									$ad -> status = $request['status'];
								}
							}
						} else {
							$ad -> status = $ad -> requests -> item -> status;
						}
					}
				} elseif ($ad -> hasSentRequest()) {
					$requestor = $ad -> getAcceptedRequest() -> user;
					if (isset($requestor -> avatar))
						$ad -> requestor = $requestor -> avatar -> id;
					else
						$ad -> requestor = null;
					
						if(isset($request))
							$ad -> request = $request;
						else
							$ad -> request = null;

					$ad -> status = "SENT";
				} elseif ($ad -> hasAcceptedRequest()) {
					$requestor = $ad -> getAcceptedRequest() -> user;
					if (isset($requestor -> avatar))
						$ad -> requestor = $requestor -> avatar -> id;
					else
						$ad -> requestor = null;
					
					if(isset($request))
						$ad -> request = $request;
					else
						$ad -> request = null;

					$ad -> status = "SELECTED";
				}  else {
					$ad -> status = "ACTIVE";
				}
				
				if(isset($ad -> request)) {
					$request = $ad -> request;
					if ($ad -> expired || $request -> isExpired) {
						if ($ad -> request -> adStatus == "EXPIRED") {
							$ad -> reqStat = "EXPIRED";
						} else if ($ad -> request -> isCanceled) {
							$ad -> reqStat = "CANCELED";
						} else {
							$ad -> reqStat = "DECLINED";
						}
					} else if ($ad -> sold) {
						$ad -> reqStat = "RECEIVED";
					} else if ($request -> isPending) {
						$ad -> reqStat = "PENDING";
					} else if ($request -> accepted) {
						$ad -> reqStat = "ACCEPTED";
					}
				}

				$results[] = $ad;
			}
			return Response::json(array('ads' => $results, 'creators' => $creators));
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
			$session = Session::get('user');
			if ($user_id == $session['data'] -> id) {
				$includeUnapproved = true;
			} else {
				$includeUnapproved = false;
			}

			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$userService = new SoapClient(Config::get('wsdl.user'), array());

			$result = $adService -> getUserAds(array("userId" => $user_id, 'numberAds' => 0, "includeRequests" => true, "includeUnapproved" => $includeUnapproved));

			if (isset($result -> ad -> type))
				$ads = $result;
			elseif(isset($result->ad))
				$ads = $result -> ad;
			else
				$ads = array();

			$return = array();
			foreach ($ads as $k => $ad) {

				$ad = new Ad($ad);
				
				if (isset($ad->requests->item) && is_array($ad -> requests -> item)) {
					foreach ($ad->requests->item as $k => $v) {
						$ad -> requests -> item[$k] = new Requested($v);
					}

				} else {
					if(isset($ad->requests->item->type))
						$ad -> requests -> item = array(new Requested($ad -> requests -> item));
				}

				if (!$ad -> online) {
					if ($ad -> approved) {
						$ad -> status = "APPROVED";
					} else if ($ad -> approval == null) {
						$ad -> status = "PENDING";
					} else {
						$ad -> status = "DECLINED";
					}
				} elseif ($ad -> sold) {
					$requestor = $ad -> getAcceptedRequest() -> user;
					$request = $ad -> getAcceptedRequest();
					if (isset($requestor -> avatar))
						$ad -> requestor = $requestor -> avatar -> id;
					else
						$ad -> requestor = null;
					
					if(isset($request))
						$ad -> request = $request;
					else
						$ad -> request = null;

					$ad -> status = "SOLD";
				} elseif ($ad -> expired) {
					$ad -> status = "EXPIRED";
				} elseif (!$ad -> hasActiveRequest()) {
					$ad -> status = "NO_REQUESTS";
				} elseif ($ad -> hasSentRequest()) {
					$requestor = $ad -> getAcceptedRequest() -> user;
					if (isset($requestor -> avatar))
						$ad -> requestor = $requestor -> avatar -> id;
					else
						$ad -> requestor = null;
					
						if(isset($request))
							$ad -> request = $request;
						else
							$ad -> request = null;

					$ad -> status = "SENT";
				} elseif ($ad -> hasAcceptedRequest()) {
					$requestor = $ad -> getAcceptedRequest() -> user;
					if (isset($requestor -> avatar))
						$ad -> requestor = $requestor -> avatar -> id;
					else
						$ad -> requestor = null;
					
					if(isset($request))
						$ad -> request = $request;
					else
						$ad -> request = null;

					$ad -> status = "SELECTED";
				} else {
					$ad -> status = "ACTIVE";
				}

				$return[] = $ad;
			}

			return Response::json($return);
		} catch ( Exception $ex ) {
			throw new Exception($ex -> getMessage());
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
			$result = $adService -> getUserRequestedAds(array("userId" => $user_id, 'numberAds' => -1, "includeRequests" => true, "includeUnapproved" => false));

			if (isset($result -> ad -> type))
				$result = $result;
			elseif(isset($result->ad))
				$result = $result -> ad;
			else
				$result=array();
			
			$ads = array();
			$creators = array();

			foreach ($result as $k => $row) {

				if (is_array($row -> requests -> item)) {
					foreach ($row->requests->item as $k => $v) {
						$v = new Requested($v);
						if ($v -> user -> id == $user_id) {
							$row -> request = $v;
						}
					}

				} else {
					$v = new Requested($row -> requests -> item);
					if ($v -> user -> id == $user_id) {
						$row -> request = $v;
					}
				}
				$request = $row -> request;
				if ($row -> expired || $request -> isExpired) {
					if ($row -> request -> adStatus == "EXPIRED") {
						$row -> reqStat = "EXPIRED";
					} else if ($row -> request -> isCanceled) {
						$row -> reqStat = "CANCELED";
					} else {
						$row -> reqStat = "DECLINED";
					}
				} else if ($row -> sold) {
					$row -> reqStat = "RECEIVED";
				} else if ($request -> isPending) {
					$row -> reqStat = "PENDING";
				} else if ($request -> accepted) {
					$row -> reqStat = "ACCEPTED";
				}

				$creators[$row -> creator -> id] = $row -> creator;
				$row -> creator = $row -> creator -> id;
				$ads[] = $row;
			}

			return Response::json(array('ads' => $ads, 'creators' => $creators));
		} catch ( Exception $ex ) {
			throw new Exception($ex -> getMessage());
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
			$result = $adService -> getBookmarkedAds(array("userId" => $user_id, 'numberAds' => -1, "includeRequests" => true, "includeUnapproved" => false));

			if (isset($result -> ad -> type))
				$result = $result;
			elseif(isset($result->ad))
				$result = $result -> ad;
			else
				$result = array();

			$ads = array();
			$creators = array();

			foreach ($result as $k => $row) {
				if(isset($row -> requests -> item)) {
					if (is_array($row -> requests -> item)) {
						foreach ($row->requests->item as $k => $v) {
							$v = new Requested($v);
							if ($v -> user -> id == $user_id) {
								$row -> request = $v;
							}
						}
	
					} else {
						$v = new Requested($row -> requests -> item);
						if ($v -> user -> id == $user_id) {
							$row -> request = $v;
						}
					}
				}
				if(isset($row -> request)) {
					$request = $row -> request;
					if ($row -> expired || $request -> isExpired) {
						if ($row -> request -> adStatus == "EXPIRED") {
							$row -> reqStat = "EXPIRED";
						} else if ($row -> request -> isCanceled) {
							$row -> reqStat = "CANCELED";
						} else {
							$row -> reqStat = "DECLINED";
						}
					} else if ($row -> sold) {
						$row -> reqStat = "RECEIVED";
					} else if ($request -> isPending) {
						$row -> reqStat = "PENDING";
					} else if ($request -> accepted) {
						$row -> reqStat = "ACCEPTED";
					}
				}
				$creators[$row -> creator -> id] = $row -> creator;
				$row -> creator = $row -> creator -> id;
				$ads[] = $row;
			}
			return Response::json(array('ads' => $ads, 'creators' => $creators));
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
			
			$times = array('availableFromTime','availableToTime','redemptionEndDate');
			foreach($times as $time) {
				if($ad->$time) $ad->$time = strtotime($ad->$time);	
			}
			
			if(Input::get('relist')) {
				$result = $adService -> getAdById(array("adId" => Input::get('relist')));
				$relistAd = new Ad($result -> ad);
				if(isset($ad->image['url']['id'])) {
					$ad->image=$relistAd->image;
				}
				else {
					$ad -> image = ImageModel::createImageModel(str_replace('api/image/', '', $ad -> image['url']));
				}
				$ad->id=$relistAd->id;
				$result = $adService -> cloneAd(array("ad" => $ad));
			}
			else {
				$ad -> image = ImageModel::createImageModel(str_replace('api/image/', '', $ad -> image['url']));
				$result = $adService -> placeAd(array("ad" => $ad));
			}
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
			$result = $adService -> getAdById(array("adId" => $id, "includeCreatorStatistics" => true));
			$ad = new Ad($result -> ad);

			if ($ad -> requested) {
				$request = $ad -> getRequestByUser($session['data'] -> id);
				if (isset($request)) {
					$ad -> status = $request -> status;
					$ad -> user_request = $request;
					$ad -> user_requested = true;
				} else {
					$ad -> user_requested = false;
				}
			}
			
			
			if (isset($ad -> requests -> item) && !is_array($ad -> requests -> item)) {
				$ad -> requests -> item = array($ad -> requests -> item);
			} 

			if (!$ad -> online) {
				if ($ad -> approved) {
					$ad -> status = "APPROVED";
				} else if ($ad -> approval == null) {
					$ad -> status = "PENDING";
				} else {
					$ad -> status = "DECLINED";
				}
			} elseif ($ad -> sold) {
				$requestor = $ad -> getAcceptedRequest() -> user;
				if (isset($requestor -> avatar))
					$ad -> requestor = $requestor -> avatar -> id;
				else
					$ad -> requestor = null;

				$ad -> status = "SOLD";
			} elseif ($ad -> expired) {
				$ad -> status = "EXPIRED";
			} elseif (!$ad -> hasActiveRequest()) {
				$ad -> status = "NO_REQUESTS";
			} elseif ($ad -> hasSentRequest()) {
				$requestor = $ad -> getAcceptedRequest() -> user;
				if (isset($requestor -> avatar))
					$ad -> requestor = $requestor -> avatar -> id;
				else
					$ad -> requestor = null;

				$ad -> status = "SENT";
			} elseif ($ad -> hasAcceptedRequest()) {
				$requestor = $ad -> getAcceptedRequest() -> user;
				if (isset($requestor -> avatar))
					$ad -> requestor = $requestor -> avatar -> id;
				else
					$ad -> requestor = null;

				$ad -> status = "SELECTED";
			} else {
				$ad -> status = "ACTIVE";
			}

			$ad -> canEdit = $ad -> owner && !$ad -> hasActiveRequest() && $ad -> statistics -> numBookmarks == 0 && $ad -> statistics -> numComments == 0 && $ad -> statistics -> numShares == 0;
			$ad -> canDelete = $ad -> owner && (!$ad -> hasActiveRequest() || $ad -> expired);

			$comments = $messageService -> getCommentsByAd(array("adId" => $ad -> id, "lastCommentId" => -1, "numComments" => 11));
			
			if(isset($comments->comment)) {
				if(!is_array($comments->comment)) {
					$comments = array($comments->comment);
				} else {
					$comments = $comments->comment;
				}
			}
			
			return array('ad' => $ad, 'comments' => $comments);
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
		try {
			$session = Session::get('user');
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			
			$result = $adService -> getAdById(array("adId" => $id));
			$ad = new Ad($result -> ad);
			
			$update = Input::get('ad');
			
			
			foreach($update as $k=>$v) {
				$ad->$k = $v;
			}
			
			if(isset($update['image']['url']) && !is_array($update['image']['url']))
				$ad -> image = ImageModel::createImageModel(str_replace('api/image/', '', $update['image']['url']));
			else if(isset($update['image']['url'])) {
				$ad -> image = $update['image']['url'];
			}
			else {
				$ad -> image = null;
			}
			
			$times = array('availableFromTime','availableToTime','redemptionEndDate');
			foreach($times as $time) {
				if(isset($update[$time])) $ad->$time = strtotime($update[$time]);	
			}
			
			$result = $adService -> updateAd(array("ad" => $ad));
		} catch ( Exception $ex ) {
			throw new Exception($ex -> getMessage());
		}
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
			$adService -> deleteAd(array("adId" => $id));
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
			$adService -> bookmarkAd(array("adId" => $id));
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
			$adService -> removeBookmark(array("adId" => $id));
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
			$messageService = new SoapClient(Config::get('wsdl.message'), array());
			$comment = new Comment;
			$comment -> text = Input::get('text');

			$result = $messageService -> addCommentToAd(array("adId" => $id, "comment" => $comment));
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
			$adService -> relistAd(array("adId" => $id));
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
			$adService -> requestAd(array("adId" => $id, "text" => Input::get('text')));
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
			$adService -> cancelRequest(array("requestId" => $id));
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
			$adService -> selectRequest(array("requestId" => $id));
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
			$adService -> markAsSent(array("requestId" => $id));
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
			$adService -> markAsReceived(array("requestId" => $id));
		} catch ( Exception $ex ) {
		}
	}

	/**
	 * Request hide
	 *
	 * @param int $id
	 * @return Response
	 */
	public function request_hide($id) {
		try {
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$adService -> hideRequest(array("requestId" => $id));
		} catch ( Exception $ex ) {
		}
	}
	
	/**
	 * Request view
	 * 
	 * @param int $id
	 * @return Response
	 */
	public function request_view($id) {
		try {
			$session = Session::get('user');
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$messageService = new SoapClient(Config::get('wsdl.message'), array());
			
			$request = $adService -> getRequestById(array("requestId" => $id)) -> request;
			$ad = $adService -> getAdById(array("adId" => $request->adId)) -> ad;
        	$messages = $messageService -> getMessagesByAdAndUsers(array(
                "adId" => $ad->id,
                "user1Id" => $request->user->id,
                "user2Id" => $session['data']->id
			));
			
			if(isset($messages->message->type)) {
				$messages = array($messages->message);
			}
			else {
				$messages = $messages->message;
			}
			
			return Response::json(array('ads' => $ad, 'request' => $request, 'messages' => $messages));
		} catch ( Exception $ex ) {
			throw new Exception($ex -> getMessage());
		}
	}
	
	public function request_issue($id) {
		$text = Input::get('text');
		try {
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$adService -> addRequestIssue(array("requestId" => $id, "text" => $text));
		} catch ( Exception $ex ) {
		}
	}
	
	public function rateAd($id) {
		$rating = new Rating;
		$rating->requestId=$id;
		$rating->text=Input::get('text');
		$rating->value=Input::get('value');
		$rating->toUserId=Input::get('toUserId');
		try {
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$adService -> rateAd(array("rating" => $rating));
		} catch ( Exception $ex ) {
		}
	}
	
	private function distance_haversine($lat1, $lon1, $lat2, $lon2) {
		$precision = 1; //4
		$earth_radius = 3959.00; # in miles
		$delta_lat = $lat2 - $lat1 ;
		$delta_lon = $lon2 - $lon1 ;
		$alpha = $delta_lat / 2;
		$beta = $delta_lon / 2;
		$a = sin(deg2rad($alpha)) * sin(deg2rad($alpha)) + cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * sin(deg2rad($beta)) * sin(deg2rad($beta));
		$c = asin(min(1, sqrt($a)));
		$distance = 2 * $earth_radius * $c;
		$distance = round($distance, $precision);
		return $distance;
    }

}
