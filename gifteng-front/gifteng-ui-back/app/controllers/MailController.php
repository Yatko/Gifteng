<?php
class MailController extends \BaseController{
	public function sendMail(){
		try {
			$body = Input::get('body');
			$recipients = Input::get('recepiants');
			
			$uService = new SoapClient(Config::get('wsdl.utility'), array());
			$uService->sendInvitationEmail(
				array(
					'text'=>$body,
					'emails'=>$recipients
				)
			);
			
			return array('status'=>'valid');
		} catch ( Exception $ex ) {			
			return array('status'=>'invalid');
		}
	}
	
	public function report() {
		$user = Session::get('user');
		$data = array(
			'user'=>$user['data']->email,
			'description'=>Input::get('description'),
			'type'=>Input::get('type'),
			'url'=>Input::get('url')
		);
		
		ob_start();
		var_dump($data);
		$data = ob_get_clean();
		
		
		try {
			$uService = new SoapClient(Config::get('wsdl.utility'), array());
			$result = $uService -> sendEmail(array("text" => $data));
			return array('success'=>true);
		} catch ( Exception $ex ) {
			throw new Exception($ex -> getMessage());
		}
	}

}
