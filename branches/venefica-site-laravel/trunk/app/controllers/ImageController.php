<?php

class ImageController extends \BaseController {
	 
	public function show($id) {
		$data = file_get_contents(app_path().'/storage/uploads/'.$id);
		$img = Image::raw($data);
		return $img;
	}
	
	public function store() {
		$file = Input::file('uploader');
		$destinationPath = app_path().'/storage/uploads/';
		$filename = $file->getClientOriginalName();
		Input::file('uploader')->move($destinationPath, $filename);
		
		return $filename;
	}
	
}