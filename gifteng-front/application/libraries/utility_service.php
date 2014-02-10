<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * Utility service
 *
 * @author gyuszi
 */
class Utility_service {
    
    var $utilityService;
    
    public function __construct() {
        log_message(DEBUG, "Initializing Utility_service");
    }
    
    public function getAddressByZipcode($zipcode) {
        try {
            $utilityService = $this->getService();
            $result = $utilityService->getAddressByZipcode(array("zipcode" => $zipcode));
            
            $address = null;
            if ( hasField($result, 'address') && $result->address ) {
                $address = Address_model::convertAddress($result->address);
            }
            return $address;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Getting address by zipcode (zipcode: '.$zipcode.') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    // internal methods
    
    private function getService() {
        if ( $this->utilityService == null ) {
            $this->utilityService = new SoapClient(UTILITY_SERVICE_WSDL, getSoapOptions(loadToken()));
        }
        return $this->utilityService;
    }
}
