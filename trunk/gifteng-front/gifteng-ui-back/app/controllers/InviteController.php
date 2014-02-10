<?php

class InviteController extends \BaseController {

	/**
	 * Requests an invitation from the server.
	 *
	 * @return integer
	 * @throws Exception in case of WS invocation error
	 */
	public function requestInvitation() {
		try {
			$invitationService = new SoapClient(Config::get('wsdl.invite'));
			$invitation = new Invitation();
			$invitation -> email = Input::get('invite_email');
			$invitation -> ipAddress = Input::get('ipAddress');
			$invitation -> country = Input::get('country');
			$invitation -> zipCode = Input::get('zipCode');
			$invitation -> source = Input::get('source');
			$invitation -> otherSource = Input::get('otherSource');

			$result = $invitationService -> requestInvitation(array("invitation" => $invitation));
			$invitationId = $result -> invitationId;
			return $invitationId;
		} catch ( Exception $ex ) {
			return array('status' => 'invalid');
		}
	}

	/**
	 * Validates the given invitation code. If validation pass the flow can continue
	 * with user registrtation
	 *
	 * @return Response
	 * @throws Exception
	 */
	public function isInvitationValid() {
		try {
			$invitationService = new SoapClient(Config::get('wsdl.invite'));
			$result = $invitationService -> isInvitationValid(array("code" => Input::get('invitation-code')));
			$valid = $result -> valid;
			return array('status' => $valid);
		} catch ( Exception $ex ) {
			return array('status' => 'invalid');
		}
	}

	/**
	 * Invokes the user registration on WS.
	 *
	 * @return response
	 * @throws Exception in case of WS error
	 */
	public function registerUser() {
		try {
			$userService = new SoapClient(Config::get('wsdl.user'));

			$user = new UserModel(Input::all());

			$result = $userService -> registerUser(array("user" => $user, "password" => Input::get('password'), "invitationCode" => Input::get('code')));

			$authService = new SoapClient(Config::get('wsdl.auth'));
			$token = $authService -> authenticateEmail(array("email" => Input::get('email'), "password" => Input::get('password'), "userAgent" => NULL));
			ini_set('soap.wsdl_cache_enabled', '0');
			ini_set('user_agent', "PHP-SOAP/" . PHP_VERSION . "\r\n" . "AuthToken: " . $token -> AuthToken);
			Session::put('user.token', $token);

			try {
				$userService = new SoapClient(Config::get('wsdl.user'));
				$result = $userService -> getUserByEmail(array("email" => Input::get('email')));
				$user = $result -> user;
			/*	if ($user -> businessAccount == true) {
					if (isset($user -> addresses) && is_object($user -> addresses)) {
						$user -> addresses = array($user -> addresses);
					}
				}  */
				Session::put('user.data', $user);

				return array('success' => true);

			} catch ( InnerException $ex ) {
				//throw new Exception($ex -> faultstring);
				return array('success'=>false, 'faultstring'=>$ex->faultstring);
			}

		} catch ( Exception $ex ) {
			return array('success'=>false, 'faultstring'=>$ex->faultstring);
		}
	}

}
