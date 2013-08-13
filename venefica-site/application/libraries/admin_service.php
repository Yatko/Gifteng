<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * Admin service
 *
 * @author gyuszi
 */
class Admin_service {
    
    public function __construct() {
        log_message(DEBUG, "Initializing Admin_service");
    }
    
    //***************
    //* approval    *
    //***************
    
    /**
     * 
     * @return array of Ad_model
     * @throws Exception
     */
    public function getUnapprovedAds() {
        try {
            $adminService = new SoapClient(ADMIN_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adminService->getUnapprovedAds();
            
            $ads = array();
            if ( hasField($result, 'ad') && $result->ad ) {
                $ads = Ad_model::convertAds($result->ad);
            }
            return $ads;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Getting unapproved ads failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @return array of Ad_model
     * @throws Exception
     */
    public function getOfflineAds() {
        try {
            $adminService = new SoapClient(ADMIN_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adminService->getOfflineAds();
            
            $ads = array();
            if ( hasField($result, 'ad') && $result->ad ) {
                $ads = Ad_model::convertAds($result->ad);
            }
            return $ads;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Getting unapproved ads failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $adId
     * @return array of Approval_model
     * @throws Exception
     */
    public function getApprovals($adId) {
        try {
            $adminService = new SoapClient(ADMIN_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adminService->getApprovals(array("adId" => $adId));
            
            $approvals = array();
            if ( hasField($result, 'approval') && $result->approval ) {
                $approvals = Approval_model::convertApprovals($result->approval);
            }
            return $approvals;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Getting approvals for ad (adId: '.$adId.') failed! '.$ex->faultstring);
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
            $adminService = new SoapClient(ADMIN_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adminService->getApproval(array("adId" => $adId, "revision" => $revision));
            $approval = Approval_model::convertApproval($result->approval);
            return $approval;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Getting approval for ad (adId: '.$adId.', revision: '.$revision.') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }

    /**
     * 
     * @param long $adId
     * @throws Exception
     */
    public function approveAd($adId) {
        try {
            $adminService = new SoapClient(ADMIN_SERVICE_WSDL, getSoapOptions(loadToken()));
            $adminService->approveAd(array("adId" => $adId));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Approving ad (adId: '.$adId.') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $adId
     * @param string $message
     * @throws Exception
     */
    public function unapproveAd($adId, $message) {
        try {
            $adminService = new SoapClient(ADMIN_SERVICE_WSDL, getSoapOptions(loadToken()));
            $adminService->unapproveAd(array("adId" => $adId, "message" => $message));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Unapproving ad (adId: '.$adId.') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $adId
     * @throws Exception
     */
    public function onlineAd($adId) {
        try {
            $adminService = new SoapClient(ADMIN_SERVICE_WSDL, getSoapOptions(loadToken()));
            $adminService->onlineAd(array("adId" => $adId));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Making online ad (adId: '.$adId.') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    //***************
    //* user        *
    //***************
    
    /**
     * 
     * @return array of User_model
     * @throws Exception
     */
    public function getUsers() {
        try {
            $adminService = new SoapClient(ADMIN_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $adminService->getUsers();
            
            $users = array();
            if ( hasField($result, 'user') && $result->user ) {
                $users = User_model::convertUsers($result->user);
            }
            return $users;
        } catch ( Exception $ex ) {
            log_message(ERROR, $ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
}
