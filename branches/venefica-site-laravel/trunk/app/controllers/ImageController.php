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