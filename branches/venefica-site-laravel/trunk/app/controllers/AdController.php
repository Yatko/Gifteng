<?php

class AdController extends \BaseController {

	/**
	 * Ad listing.
	 *
	 * @return Response
	 */
	public function index() {
		try {
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$result = $adService -> getAdsExDetail(array("lastAdId" => -1, "numberAds" => 20, "filter" => null, "includeImages" => true, "includeCreator" => true, "includeCommentsNumber" => 5));

			return $result->ad;
		} catch ( Exception $ex ) {
			throw new Exception($ex -> getMessage());
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
			$result = $adService -> getUserAds(array(
                "userId" => $user_id,
                'numberAds'=>-1,
                "includeRequests" => true,
                "includeUnapproved" => false
            ));

			if(isset($result->ad->type))
				return Response::json($result);
			else
				return Response::json($result->ad);
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
			foreach($result as $k=>$row) {
				$ad = get_object_vars($row);
				if($ad['status'] == 'FINALIZED') {
					$requests = get_object_vars($ad['requests']);
					
					if(is_array($requests['item'])) {
						foreach($requests['item'] as $request) {
				 			$request = get_object_vars($request);
							$usr = get_object_vars($request['user']);
							if($usr['id']==$user_id) {
								$ad['status']=$request['status'];
							}
						}
					}
					else {
						$ad['status'] = $ad['requests']->item->status;
					}
				}
				$ads[]=$ad;
			}
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
	public function bookmarkedByUser($user_id) {
		try {
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$result = $adService -> getBookmarkedAds(array(
                "userId" => $user_id
            ));

			
			if(isset($result->ad->type))
				return Response::json($result);
			else
				return Response::json($result->ad);
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
			$ad=array(
				'owner'=>$session['data']->id,
				'inBookmarks'=>false,
				'sold'=>false,
				'expired'=>false,
				'requested'=>false,
				'online'=>false,
				'approved'=>false
			);
			$ad = array_merge($ad,Input::get('ad'));
            $result = $adService->placeAd(array("ad" => $ad));
            $adId = $result->adId;
            return $adId;
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
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$result = $adService -> getAdById(array("adId" => $id));
			return get_object_vars($result->ad);
		} catch ( Exception $ex ) {
			throw new Exception($ex -> getMessage());
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

	}
}
