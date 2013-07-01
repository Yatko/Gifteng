<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * Invitation service
 *
 * @author gyuszi
 */
class Invitation_service {
    
    const GENERAL_ERROR            = 0;
    //email sending related errors
    const INVALID_FROM_ADDRESS     = 1;
    const INVALID_TO_ADDRESS       = 2;
    const INVALID_EMAIL_MESSAGE    = 3;
    const EMAIL_SEND_ERROR         = 4;
    //MailJimp related errors
    const ALREADY_SUBSCRIBED       = 11;

    public function __construct() {
        log_message(DEBUG, "Initializing Invitation_service");
    }
    
    /**
     * Requests an invitation from the server.
     * 
     * @param Invitation_model the invitation model
     * @return long the invitation ID
     * @throws Exception in case of WS invocation error
     */
    public function requestInvitation($invitation) {
        try {
            $invitationService = new SoapClient(INVITATION_SERVICE_WSDL, getSoapOptions());
            $result = $invitationService->requestInvitation(array("invitation" => $invitation));
            $invitationId = $result->invitationId;
            return $invitationId;
        } catch ( Exception $ex ) {
            $errorCode = Invitation_service::$GENERAL_ERROR;
            if (
                hasField($ex, 'detail') &&
                hasField($ex->detail, 'InvitationError') &&
                hasField($ex->detail->InvitationError, 'errorCode')
            ) {
                $errorCode = $ex->detail->InvitationError->errorCode;
            }
            
            log_message(ERROR, 'Invitation request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring, $errorCode);
        }
    }
    
    /**
     * Validates the given invitation code. If validation pass the flow can continue
     * with user registrtation
     * 
     * @param string $code the invitation code
     * @return boolean true if the given invitation code exists and is valid
     * @throws Exception
     */
    public function isInvitationValid($code) {
        try {
            $invitationService = new SoapClient(INVITATION_SERVICE_WSDL, getSoapOptions());
            $result = $invitationService->isInvitationValid(array("code" => $code));
            $valid = $result->valid;
            return $valid;
        } catch ( Exception $ex ) {
            log_message(INFO, 'Invitation validation failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
}
