<?php

class ImageController extends \BaseController {
	 
	public function show($id) {
		$data = file_get_contents(app_path().'/storage/uploads/'.$id);
		$img = Image::raw($data);
		$response = Response::make($img, 200);
		$response->header('Content-Type', 'image/jpeg');
		return $response;
	}
	
	public function cropnsave($id) {	
		$basepath = app_path().'/storage/uploads/';	
		
		$jpeg_quality = 90;

		$src = $basepath.'resize_'.$id;
		if(ImageModel::getImgTypeByExtension($id) == ImageModel::IMGTYPE_PNG){
			$img_r = imagecreatefrompng($src);
		}
		else{
			$img_r = imagecreatefromjpeg($src);		
		}
		$dst_r = ImageCreateTrueColor( Input::get('w'),Input::get('h') );

		imagecopyresampled($dst_r, $img_r 
						  ,0,0,
						  Input::get('x'),Input::get('y'),
						  Input::get('w'),Input::get('h'),
						  Input::get('w'),Input::get('h'));
		

		$filename = $basepath.'crop_'.$id;
		imagejpeg($dst_r, $filename ,$jpeg_quality);
		
		
			
		$image = ImageModel::createImageModel('crop_'.$id);
		
		try {
			$session = Session::get('user');
			$userService = new SoapClient(Config::get('wsdl.user'));
			$currentUser = $userService->getUserById(array("userId" => $session['data']->id));
			$currentUser=$currentUser->user;
            $currentUser->avatar = $image;			
            $result = $userService->updateUser(array("user" => $currentUser));
			
			// Cleanup
			File::delete($src);
			File::delete($filename);
			File::delete($basepath.$id);			
			
            return Response::json($result->complete);
        } catch ( Exception $ex ) {
			error_log($ex);
        }
		
		
	}
	public function wipscaled($id) {	
		$basepath = app_path().'/storage/uploads/';	
		$filepath = $basepath.$id;
		$jpeg_quality = 90;			
		
		$img = Image::make($filepath)->resize(450, 350, true)->save($basepath.'resize_'.$id, $jpeg_quality);
		
		$response = Response::make($img);
		// set content-type
		$response->header('Content-Type', 'image/png');

		return $response;
		//return $img;
	}
	

	public function storeInWIP() {
		// store the profile images a temp location using unique file id, get it cropped from client and save to profile later
        $file = Input::file('uploader');
		
		
        $destinationPath = app_path().'/storage/uploads/';
        $filename = uniqid().'_'.$file->getClientOriginalName();
		
        $file->move($destinationPath, $filename);
		
		if(exif_imagetype($destinationPath.$filename)==2) {
			$layer = PHPImageWorkshop\ImageWorkshop::initFromPath($destinationPath.$filename);
			
		    $exif = exif_read_data($destinationPath.$filename);
		 
		    if(isset($exif['Orientation'])&& $exif['Orientation'] == '6'){
		    	$layer->rotate(90);
		    }
		 
		    if(isset($exif['Orientation'])&& $exif['Orientation'] == '3'){
		    	$layer->rotate(180);
		    }
			
	        $layer->save($destinationPath, $filename, false, null, 95);
		}
		
        list($width, $height, $type, $attr) = getimagesize($destinationPath.$filename);
       // return Response::json(array('filename' => $filename, 'width' => $width, 'height' => $height, 'type' => $type, 'attr' => $attr));
		if($width<500 || $height <500 || filesize($destinationPath.$filename) > 5242880) {
			return array('error'=>"Picture width and height must be at least 500px. Please select a larger image or limit the photo size to 5MB.");
		}
		else {
			return Response::json(array('filename' => $filename, 'width' => $width, 'height' => $height, 'type' => $type, 'attr' => $attr));
		}
    }

	public function store() {
		$file = Input::file('uploader');
		$destinationPath = app_path().'/storage/uploads/';
		$filename = $file->getClientOriginalName();
		
        $destinationPath = app_path().'/storage/uploads/';
        $filename = $file->getClientOriginalName();
		
        $file->move($destinationPath, $filename);

		if(exif_imagetype($destinationPath.$filename)==2) {
			$layer = PHPImageWorkshop\ImageWorkshop::initFromPath($destinationPath.$filename);
			
				$exif = exif_read_data($destinationPath.$filename);
		 
		    if(isset($exif['Orientation'])&& $exif['Orientation'] == '6'){
		    	$layer->rotate(90);
		    }
		 
		    if(isset($exif['Orientation'])&& $exif['Orientation'] == '3'){
		    	$layer->rotate(180);
		    }
			
	        $layer->save($destinationPath, $filename, false, null, 95);
		}
		
		if( ImageModel::getImgTypeByExtension($filename) == ImageModel::IMGTYPE_PDF){
			return array();
		}
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
	public function storeFile() {
		$file = Input::file('uploader');
		$destinationPath = app_path().'/storage/uploads/';
		$filename = $file->getClientOriginalName();
		Input::file('uploader')->move($destinationPath, $filename);	
			return array();
		
	}
	public function showFile($id) {
		$data = file_get_contents(app_path().'/storage/uploads/'.$id);
		$img = Image::raw($data);
		return $img;
	}
}