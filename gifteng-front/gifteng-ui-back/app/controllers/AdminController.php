<?php

class AdminController extends \BaseController {
	 
	    //***************
    //* approval    *
    //***************
    
    /**
     * 
     * @return array of Ads
     * @throws Exception
     */
    public function getUnapprovedAds() {
        try {
			$adminService = new SoapClient(Config::get('wsdl.admin'), array());
			$result = $adminService->getUnapprovedAds(); 
			
			if(!is_array($result->ad)) {
				$result->ad = array($result->ad);
			}
			
			return array('ads' => $result);
		} catch ( Exception $ex ) {
			return array('role' => 'user');
		}
    }
	  /**
     * 
     * @param long $adId
     * @throws Exception
     */
    public function approveAd() {
        try {
             $adminService = new SoapClient(Config::get('wsdl.admin'), array());
            $adminService->approveAd(array(
			 "adId" => Input::get('ad_Id')
			));
			 return array('status'=>'valid');
	   } catch ( Exception $ex ) {
             return array('status'=>'failed');
        }
    }
    
    /**
     * 
     * @param long $adId
     * @param string $message
     * @throws Exception
     */
    public function unapproveAd() {
        try {
             $adminService = new SoapClient(Config::get('wsdl.admin'), array());
            $adminService->unapproveAd(array(
                "adId" => Input::get('ad_Id'),
                "message" => Input::get('reason')
            ));
			 return array('status'=>'valid');
        } catch ( Exception $ex ) {
             return array('status'=>'failed');
        }
    }
    
    /**
     * 
     * @param long $adId
     * @throws Exception
     */
    public function onlineAd() {
        try {
            $adminService = new SoapClient(Config::get('wsdl.admin'), array());
            $adminService->onlineAd(array(
			 "adId" => Input::get('ad_Id')
			));
			 return array('status'=>'valid');
        } catch ( Exception $ex ) {
            return array('status'=>'failed');
        }
    }
	
	public function getShippingBoxes(){
		try{
			$adService = new SoapClient(Config::get('wsdl.ad'), array());
			$shippingBoxes = $adService -> getShippingBoxes();
			return array("shippingBoxes"=> $shippingBoxes);	
		} catch(Exception $ex){
		}
	}
     public function getShippings() {
        try {
            $adminService = new SoapClient(Config::get('wsdl.admin'), array());
            $result = $adminService->getShippings();         
           
			return array('shippings' => $result);
		} catch ( Exception $ex ) {
			Log::error($ex);
			return array('role' => 'user');
		}
    }
	public function sendEmail() {
       try {
            $adminService = new SoapClient(Config::get('wsdl.admin'), array());
			$to = Input::get('to');
			if($to == "creator"){
				$adminService->sendMailToCreator(array(
					"shippingId" => Input::get('shippingId')
				));
			}
			else{
				$adminService->sendMailToReceiver(array(
					"shippingId" => Input::get('shippingId')
				));
			}
            
			
			
			return array('status'=>'valid');
        } catch ( Exception $ex ) {
            return array('status'=>'failed');
        }
    }
    public function deleteShipping() {
       try {
            $adminService = new SoapClient(Config::get('wsdl.admin'), array());
            $adminService->deleteShipping(array(
				"shippingId" => Input::get('shippingId')
			));			
			return array('status'=>'valid');
        } catch ( Exception $ex ) {
            return array('status'=>'failed');
        }
    }
	 public function updateShipping() {
       try {
            $adminService = new SoapClient(Config::get('wsdl.admin'), array());	
			
			$shipping = new ShippingModel();
			$shipping -> id = Input::get('shippingId');
			$shipping -> trackingNumber = Input::get('trackingNumber');
			$shipping -> receivedAmount = Input::get('receivedAmount');
			if(Input::get('barcodeImage') != null)
			{
				$shipping -> image = Input::get('barcodeImage');
				$shipping -> barcodeImage = ImageModel::createImageModel(str_replace('api/image/', '', $shipping ->image['url']));
			}
			
			$result = $adminService->updateShipping(array("shipping" => $shipping));	
			return array('status'=>'valid');
        } catch ( Exception $ex ) {
            return array('status'=>'failed');
        }
    }
}