<?php

class GeoController extends \BaseController {

	/**
	 * Display a specified ad.
	 *
	 * @param int $id
	 * @return Response
	 */
	public function show($zip) {
		try {
			$geoService = new SoapClient(Config::get('wsdl.utility'), array());
			$result = $geoService -> getAddressByZipcode(array("zipcode" => $zip));
			return get_object_vars($result);
		} catch ( Exception $ex ) {
			throw new Exception($ex -> getMessage());
		}
	}

}
