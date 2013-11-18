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
		list($width, $height, $type, $attr) = getimagesize($destinationPath.$filename);
		if($width<500 || $height <500 || filesize($destinationPath.$filename) > 5242880) {
			return array('error'=>"Picture width and height must be at least 500px. Please select a larger image or limit the photo size to 5MB.");
		}
		else {
			return array();
		}
	}
	
	public function profile() {
		
		$file = Input::file('uploader');
		$destinationPath = app_path().'/storage/uploads/';
		$filename = $file->getClientOriginalName();
		Input::file('uploader')->move($destinationPath, $filename);
        $image = ImageModel::createImageModel($filename);
		
		
        try {
			$session = Session::get('user');
			$userService = new SoapClient(Config::get('wsdl.user'));
			$currentUser = $userService->getUserById(array("userId" => $session['data']->id));
			$currentUser=$currentUser->user;
            $currentUser->avatar = $image;
            $result = $userService->updateUser(array("user" => $currentUser));
            return Response::json($result->complete);
        } catch ( Exception $ex ) {
        }
	}
	
}