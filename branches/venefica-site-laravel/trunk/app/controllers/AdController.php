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
			$type = Input::get('order');

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

			$ads = array();
			$creators = array();

			foreach ($result->ad as $k => $v) {
				$creators[$v -> creator -> id] = $v -> creator;
				$v -> creatorname = $v -> creator -> firstName . " " . $v -> creator -> lastName;
				$v -> creator = $v -> creator -> id;
				if ($v -> creator == $session['data'] -> id) {
					$v -> self = true;
				}
				$ads[] = $v;
			}
			return array('ads' => $ads, 'creators' => $creators);
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
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$result = $adService -> getAdsExDetail(array("lastIndex" => $last, "numberAds" => 15, "filter" => $filter, "includeImages" => true, "includeCreator" => true, "includeCommentsNumber" => 2));

			$ads = array();
			$creators = array();

			foreach ($result->ad as $k => $v) {
				$creators[$v -> creator -> id] = $v -> creator;
				$v -> creatorname = $v -> creator -> firstName . " " . $v -> creator -> lastName;
				$v -> creator = $v -> creator -> id;
				$ads[] = $v;
			}
			return Response::json(array('ads' => $ads, 'creators' => $creators));
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
			else
				$ads = $result -> ad;

			$return = array();
			foreach ($ads as $k => $ad) {

				$ad = new Ad($ad);

				if ($ad -> requested) {
					if (is_array($ad -> requests -> item)) {
						foreach ($ad->requests->item as $k => $v) {
							$ad -> requests -> item[$k] = new Requested($v);
						}

					} else {
						$ad -> requests -> item = new Requested($ad -> requests -> item);
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
				} elseif ($ad -> sold) {
					$requestor = $ad -> getAcceptedRequest() -> user;
					if (isset($requestor -> avatar))
						$ad -> requestor = $requestor -> id;
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
						$ad -> requestor = $requestor -> id;
					else
						$ad -> requestor = null;

					$ad -> status = "SENT";
				} elseif ($ad -> hasAcceptedRequest()) {
					$requestor = $ad -> getAcceptedRequest() -> user;
					if (isset($requestor -> avatar))
						$ad -> requestor = $requestor -> id;
					else
						$ad -> requestor = null;

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
			else
				$result = $result -> ad;

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
			$result = $adService -> getBookmarkedAds(array("userId" => $user_id));

			if (isset($result -> ad -> type))
				$result = $result;
			else
				$result = $result -> ad;

			$ads = array();
			$creators = array();

			foreach ($result as $k => $row) {
				$ad = $row;
				if ($ad -> status == 'FINALIZED') {
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
				$creators[$ad -> creator -> id] = $ad -> creator;
				$ad -> creator = $ad -> creator -> id;
				$ads[] = $ad;
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

			$ad -> image = ImageModel::createImageModel(str_replace('api/image/', '', $ad -> image['url']));

			$result = $adService -> placeAd(array("ad" => $ad));
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

			$ad -> canEdit = $ad -> owner && !$ad -> hasActiveRequest() && $ad -> statistics -> numBookmarks == 0 && $ad -> statistics -> numComments == 0 && $ad -> statistics -> numShares == 0;
			$ad -> canDelete = $ad -> owner && (!$ad -> hasActiveRequest() || $ad -> expired);

			$comments = $messageService -> getCommentsByAd(array("adId" => $ad -> id, "lastCommentId" => -1, "numComments" => 11));

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

}
