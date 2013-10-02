<?php

class ImageController extends \BaseController {
	 
	public function show($id) {
		$data = file_get_contents(base_path().'/../../gifteng/cache/'.$id);
		$img = Image::raw($data);
		return $img;
	}
	
}