<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * Ad service
 *
 * @author gyuszi
 */
class Ad_service {
    
    public function __construct() {
        log_message(DEBUG, "Initializing Ad_service");
    }
    
    //*************************
    //* ads listing/filtering *
    //*************************
    
    /**
     * Requests ads for the given user. If the user is invalid user not found
     * exception will be thrown by ws.
     * 
     * @param long $userId
     * @return array of Ad_model
     * @throws Exception
     */
    public function getUserAds($userId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getUserAds(array("userId" => $userId));
            
            $ads = array();
            if ( hasField($result, 'ad') && $result->ad ) {
                $ads = Ad_model::convertAds($result->ad);
            }
            return $ads;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'User ads (userId: ' . $userId . ') request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * Gets the ads for the given user requests. If the user is invalid user not found
     * exception will be thrown by ws.
     * 
     * @param long $userId
     * @return array of Ad_model
     * @throws Exception
     */
    public function getUserRequestedAds($userId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getUserRequestedAds(array("userId" => $userId));
            
            $ads = array();
            if ( hasField($result, 'ad') && $result->ad ) {
                $ads = Ad_model::convertAds($result->ad);
            }
            return $ads;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'User ads (userId: ' . $userId . ') request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * Gets a list of listings. The order of listing is by its id, the $lastAdId
     * parameter specifies this start point and the $numberAds the limit.
     * 
     * @param long $lastAdId
     * @param int $numberAds
     * @return array of Ad_model
     * @throws Exception
     */
    public function getAds($lastAdId, $numberAds) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getAds(array(
                "lastAdId" => $lastAdId,
                "numberAds" => $numberAds
            ));
            
            $ads = array();
            if ( hasField($result, 'ad') && $result->ad ) {
                $ads = Ad_model::convertAds($result->ad);
            }
            return $ads;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Ads request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * Gets a list of listings that satifies filter.
     * 
     * @param long $lastAdId
     * @param int $numberAds
     * @param Filter_model $filter
     * @return array of Ad_model
     * @throws Exception
     */
    public function getAdsEx($lastAdId, $numberAds, $filter = null) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getAdsEx(array(
                "lastAdId" => $lastAdId,
                "numberAds" => $numberAds,
                "filter" => $filter
            ));
            
            $ads = array();
            if ( hasField($result, 'ad') && $result->ad ) {
                $ads = Ad_model::convertAds($result->ad);
            }
            return $ads;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Ads extended request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * Gets a list of listings that satifies filter. The include flag specifies
     * extra data to include into response.
     * 
     * @param long $lastAdId
     * @param int $numberAds
     * @param Filter_model $filter
     * @param boolean $includeImages
     * @param boolean $includeCreator
     * @param boolean $includeCommentsNumber
     * @return array of Ad_model
     * @throws Exception
     */
    public function getAdsExDetail($lastAdId, $numberAds, $filter, $includeImages, $includeCreator, $includeCommentsNumber) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getAdsExDetail(array(
                "lastAdId" => $lastAdId,
                "numberAds" => $numberAds,
                "filter" => $filter,
                "includeImages" => $includeImages,
                "includeCreator" => $includeCreator,
                "includeCommentsNumber" => $includeCommentsNumber
            ));
            
            $ads = array();
            if ( hasField($result, 'ad') && $result->ad ) {
                $ads = Ad_model::convertAds($result->ad);
            }
            return $ads;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Ads extended detail request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * Return the listing by its id.
     * 
     * @param long $adId
     * @return Ad_model
     * @throws Exception
     */
    public function getAdById($adId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getAdById(array("adId" => $adId));
            $ad = Ad_model::convertAd($result->ad);
            return $ad;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Ad (id: '.$adId.') request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    //***************
    //* ad requests *
    //***************
    
    /**
     * Creates a new request on the given ad.
     * 
     * @param long $adId
     * @return long the request id
     * @throws Exception
     */
    public function requestAd($adId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->requestAd(array("adId" => $adId));
            $requestId = $result->requestId;
            return $requestId;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Request ad (adId: ' . $adId . ') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * Cancels (removes) an existing request.
     * 
     * @param long $requestId
     * @throws Exception
     */
    public function cancelRequest($requestId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $adService->cancelRequest(array("requestId" => $requestId));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Cancel request (requestId: ' . $requestId . ') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * Selects the given request as the choosed one (as the 'winner').
     * 
     * @param long $requestId
     * @throws Exception
     */
    public function selectRequest($requestId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $adService->selectRequest(array("requestId" => $requestId));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Select request (requestId: ' . $requestId . ') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $adId
     * @return array of Request_model
     * @throws Exception
     */
    public function getRequests($adId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getRequests(array("adId" => $adId));
            
            $requests = array();
            if ( hasField($result, 'request') && $result->request ) {
                $requests = Request_model::convertRequests($result->request);
            }
            return $requests;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Requests (adId: ' . $adId . ') request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $userId
     * @return array of Request_model
     * @throws Exception
     */
    public function getRequestsByUser($userId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getRequestsByUser(array("userId" => $userId));
            
            $requests = array();
            if ( hasField($result, 'request') && $result->request ) {
                $requests = Request_model::convertRequests($result->request);
            }
            return $requests;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Requests (adId: ' . $adId . ') request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $userId
     * @return array of Request_model
     * @throws Exception
     */
    public function getRequestsForUserWithoutReview($userId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getRequestsForUserWithoutReview(array("userId" => $userId));
            
            $requests = array();
            if ( hasField($result, 'request') && $result->request ) {
                $requests = Request_model::convertRequests($result->request);
            }
            return $requests;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Requests (adId: ' . $adId . ') request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $requestId
     * @throws Exception
     */
    public function markAsSent($requestId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $adService->markAsSent(array("requestId" => $requestId));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Mark request as sent (requestId: ' . $requestId . ') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $requestId
     * @throws Exception
     */
    public function markAsReceived($requestId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $adService->markAsReceived(array("requestId" => $requestId));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Mark request as received (requestId: ' . $requestId . ') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    //***************
    //* ad bookmark *
    //***************
    
    /**
     * Requests the list of bookmarked ads by the given user.
     * 
     * @param long $userId
     * @return array of Ad_model
     * @throws Exception
     */
    public function getBookmarkedAds($userId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getBookmarkedAdsForUser(array("userId" => $userId));
            
            $ads = array();
            if ( hasField($result, 'ad') && $result->ad ) {
                $ads = Ad_model::convertAds($result->ad);
            }
            return $ads;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'User bookmarked ads (userId: ' . $userId . ') request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    //***********
    //* reviews *
    //***********
    
    /**
     * 
     * @param Review_model $review
     * @throws Exception
     */
    public function addReview($review) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $adService->addReview(array("review" => $review));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Add review failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $userId
     * @return array of Review_model
     * @throws Exception
     */
    public function getReceivedReviews($userId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getReceivedReviews(array("userId" => $userId));
            
            $reviews = array();
            if ( hasField($result, 'review') && $result->review ) {
                $reviews = Review_model::convertReviews($result->review);
            }
            return $reviews;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'User received reviews (userId: ' . $userId . ') request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $userId
     * @return array of Review_model
     * @throws Exception
     */
    public function getSentReviews($userId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getSentReviews(array("userId" => $userId));
            
            $reviews = array();
            if ( hasField($result, 'review') && $result->review ) {
                $reviews = Review_model::convertReviews($result->review);
            }
            return $reviews;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'User sent reviews (userId: ' . $userId . ') request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    //**********************
    //* categories related *
    //**********************
    
    //TODO: not yet finished
    public function getAllCategories() {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getAllCategories();
            $categories = $result->category;
            
            //TODO:
            print_r($categories);
            
            return $categories;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Getting all categories failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
}
