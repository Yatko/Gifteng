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
    
    //**********************
    //* categories related *
    //**********************
    
    /**
     * 
     * @param long $categoryId
     * @return array of Category_model
     * @throws Exception
     */
    public function getCategories($categoryId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getCategories(array("categoryId" => $categoryId));
            
            $categories = array();
            if ( hasField($result, 'category') && $result->category ) {
                $categories = Category_model::convertCategories($result->category);
            }
            return $categories;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Getting subcategories (categoryId: ' . $categoryId . ') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @return array of Category_model
     * @throws Exception
     */
    public function getAllCategories() {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getAllCategories();
            
            $categories = array();
            if ( hasField($result, 'category') && $result->category ) {
                $categories = Category_model::convertCategories($result->category);
            }
            return $categories;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Getting all categories failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    //***********************************
    //* ad cruds (create/update/delete) *
    //***********************************
    
    /**
     * 
     * @param Ad_model $ad
     * @return long ad ID
     * @throws Exception
     */
    public function placeAd($ad) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->placeAd(array("ad" => $ad));
            $adId = $result->adId;
            return $adId;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Ad creation failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param Ad_model $ad
     * @throws Exception
     */
    public function updateAd($ad) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $adService->updateAd(array("ad" => $ad));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Ad update failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $adId
     * @throws Exception
     */
    public function deleteAd($adId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $adService->deleteAd(array("adId" => $adId));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Ad removal (adId: ' . $adId . ') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $adId
     * @param Image_model $image
     * @return long image ID
     * @throws Exception
     */
    public function addImageToAd($adId, $image) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->addImageToAd(array(
                "adId" => $adId,
                "image" => $image
            ));
            $imageId = $result->imgId;
            return $imageId;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Add image to ad (adId: ' . $adId . ') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $adId
     * @param long $imageId
     * @throws Exception
     */
    public function deleteImageFromAd($adId, $imageId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $adService->deleteImageFromAd(array(
                "adId" => $adId,
                "imageId" => $imageId
            ));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Remove image from ad (adId: ' . $adId . ') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $adId
     * @param array of long $imageIds
     * @throws Exception
     */
    public function deleteImagesFromAd($adId, $imageIds) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $adService->deleteImagesFromAd(array(
                "adId" => $adId,
                "imageIds" => $imageIds
            ));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Remove images from ad (adId: ' . $adId . ') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    //*********************
    //* approvals related *
    //*********************
    
    /**
     * 
     * @param long $adId
     * @return array of Approval_model
     * @throws Exception
     */
    public function getApprovals($adId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getApprovals(array("adId" => $adId));
            
            $approvals = array();
            if ( hasField($result, 'approval') && $result->approval ) {
                $approvals = Approval_model::convertApprovals($result->approval);
            }
            return $approvals;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Getting approval for ad (adId: '.$adId.') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $adId
     * @param integer $revision
     * @return Approval_model
     * @throws Exception
     */
    public function getApproval($adId, $revision) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getApproval(array(
                "adId" => $adId,
                "revision" => $revision
            ));
            $approval = Approval_model::convertApproval($result->approval);
            return $approval;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Getting approval for ad (adId: '.$adId.', revision: '.$revision.') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    //*************************
    //* ads listing/filtering *
    //*************************
    
    /**
     * Requests ads for the given user. If the user is invalid user not found
     * exception will be thrown by ws.
     * 
     * @param long $userId
     * @param boolean $includeRequests
     * @return array of Ad_model
     * @throws Exception
     */
    public function getUserAds($userId, $includeRequests) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getUserAds(array(
                "userId" => $userId,
                "includeRequests" => $includeRequests
            ));
            
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
     * @param boolean $includeRequests
     * @return array of Ad_model
     * @throws Exception
     */
    public function getUserRequestedAds($userId, $includeRequests) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getUserRequestedAds(array(
                "userId" => $userId,
                "includeRequests" => $includeRequests
            ));
            
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
    
    //*****************
    //* ad statistics *
    //*****************
    
    public function getStatistics($adId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getStatistics(array("adId" => $adId));
            $statistics = AdStatistics_model::convertAdStatistics($result->statistics);
            return $statistics;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Ad statistics (id: '.$adId.') request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    //***************
    //* ad requests *
    //***************
    
    /**
     * 
     * @param long $requestId
     * @throws Exception
     */
    public function hideRequest($requestId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $adService->hideRequest(array("requestId" => $requestId));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Hide request (requestId: ' . $requestId . ') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * Creates a new request on the given ad.
     * 
     * @param long $adId
     * @param string $text
     * @return long the request id
     * @throws Exception
     */
    public function requestAd($adId, $text) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->requestAd(array(
                "adId" => $adId,
                "text" => $text
            ));
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
     * @param long $requestId
     * @return Request_model
     * @throws Exception
     */
    public function getRequestById($requestId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getRequestById(array("requestId" => $requestId));
            $request = Request_model::convertRequest($result->request);
            return $request;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Getting request (requestId: ' . $requestId . ') failed! '.$ex->faultstring);
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
            log_message(ERROR, 'Requests of user (userId: ' . $userId . ') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $userId
     * @return array of Request_model
     * @throws Exception
     */
    public function getRequestsForUserWithoutRating($userId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getRequestsForUserWithoutRating(array("userId" => $userId));
            
            $requests = array();
            if ( hasField($result, 'request') && $result->request ) {
                $requests = Request_model::convertRequests($result->request);
            }
            return $requests;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Requests user without rating (userId: ' . $userId . ') failed! '.$ex->faultstring);
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
     * 
     * @param long $adId
     * @return long the bookmark id
     * @throws Exception
     */
    public function bookmarkAd($adId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->bookmarkAd(array("adId" => $adId));
            $bookmarkId = $result->bookmarkId;
            return $bookmarkId;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Bookmark ad (adId: ' . $adId . ') request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $adId
     * @throws Exception
     */
    public function removeBookmark($adId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $adService->removeBookmark(array("adId" => $adId));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Remove bookmark ad (adId: ' . $adId . ') request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }

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
    
    //*************
    //* ad rating *
    //*************
    
    /**
     * 
     * @param Rating_model $rating
     * @return float
     * @throws Exception
     */
    public function rateAd($rating) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->rateAd(array("rating" => $rating));
            return $result->rating;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Add rating failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $userId
     * @return array of Rating_model
     * @throws Exception
     */
    public function getReceivedRatings($userId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getReceivedRatings(array("userId" => $userId));
            
            $ratings = array();
            if ( hasField($result, 'rating') && $result->rating ) {
                $ratings = Rating_model::convertRatings($result->rating);
            }
            return $ratings;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'User received ratings (userId: ' . $userId . ') request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $userId
     * @return array of Rating_model
     * @throws Exception
     */
    public function getSentRatings($userId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getSentRatings(array("userId" => $userId));
            
            $ratings = array();
            if ( hasField($result, 'rating') && $result->rating ) {
                $ratings = Rating_model::convertRatings($result->rating);
            }
            return $ratings;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'User sent ratings (userId: ' . $userId . ') request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
}
