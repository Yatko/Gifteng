<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * Invitation service
 *
 * @author gyuszi
 */
class Invitation_service {
    
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
            log_message(ERROR, 'Invitation request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
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

?>
