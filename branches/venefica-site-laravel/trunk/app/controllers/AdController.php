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
            $result = $adService->getAds(array(
                "lastAdId" => -1,
                "numberAds" => 20
            ));
			
            $result = get_object_vars($result);
			return $result['ad'];
        } catch ( Exception $ex ) {
            throw new Exception($ex->faultstring);
		}
	 }
	 
	 /**
	  * Store a newly created ad.
	  * 
	  * @return Response
	  */
	  public function store() {
	  	
	  }
	  
	  /**
	   * Display a specified ad.
	   * 
	   * @param int $id
	   * @return Response 
	   */
	   public function show($id) {
	   	
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