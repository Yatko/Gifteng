<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * Utility service
 *
 * @author gyuszi
 */
class Utility_service {
    
    public function __construct() {
        log_message(DEBUG, "Initializing Utility_service");
    }
    
    public function getAddressByZipcode($zipcode) {
        try {
            $utilityService = new SoapClient(UTILITY_SERVICE_WSDL, getSoapOptions());
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
    
}
