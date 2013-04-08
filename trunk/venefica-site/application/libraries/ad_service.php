<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * Ad service
 *
 * @author gyuszi
 */
class Ad_service {
    
    public function getAds($lastAdId, $numberAds) {
        try {
            $adService = new SoapClient(AD_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adService->getAds(array("lastAdId" => $lastAdId, "numberAds" => $numberAds));
            
            $ads = array();
            if ( property_exists($result, 'ad') && $result->ad ) {
                if ( is_array($result->ad) && count($result->ad) > 0 ) {
                    foreach ( $result->ad as $ad ) {
                        array_push($ads, new Ad_model($ad));
                    }
                } else {
                    $ad = $result->ad;
                    array_push($ads, new Ad_model($ad));
                }
            }
            
            return $ads;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Ads request failed! '.$ex->faultstring);
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
    
}
