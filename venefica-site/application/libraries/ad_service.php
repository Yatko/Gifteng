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
    
    public function getAds($lastAdId, $numberAds) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getAds(array(
                "lastAdId" => $lastAdId,
                "numberAds" => $numberAds
            ));
            
            $ads = array();
            if ( hasField($result, 'ad') && $result->ad ) {
                $ads = $this->populateAds($result->ad);
            }
            return $ads;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Ads request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
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
                $ads = $this->populateAds($result->ad);
            }
            return $ads;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Ads extended request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
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
                $ads = $this->populateAds($result->ad);
            }
            return $ads;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Ads extended detail request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    public function getAdById($adId) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getAdById(array("adId" => $adId));
            $ad = new Ad_model($result->ad);
            return $ad;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Ad (id: '.$adId.') request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    public function getAllCategories() {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getAllCategories();
            $categories = $result->category;
            
            print_r($categories);
            
            return $categories;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Getting all categories failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    // internal methods
    
    private function populateAds($adsResult) {
        $ads = array();
        if ( is_array($adsResult) && count($adsResult) > 0 ) {
            foreach ( $adsResult as $ad ) {
                array_push($ads, new Ad_model($ad));
            }
        } else {
            $ad = $adsResult;
            array_push($ads, new Ad_model($ad));
        }
        return $ads;
    }
    
}
