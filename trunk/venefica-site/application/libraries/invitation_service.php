<?php

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
     * @return the invitation ID
     * @throws Exception in case of WS invocation error
     */
    public function requestInvitation($invitation) {
        try {
            $invitationService = new SoapClient(INVITATION_SERVICE_WSDL, getSoapOptions());
            $result = $invitationService->requestInvitation(array("invitation" => $invitation));
            $invitationId = $result->invitationId;
            return $invitationId;
        } catch ( Exception $ex ) {
            log_message(INFO, 'Invitation request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
}

?>
