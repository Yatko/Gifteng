<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * Comment service
 *
 * @author gyuszi
 */
class Comment_service {
    
    public function __construct() {
        log_message(DEBUG, "Initializing Comment_service");
    }
    
    public function getCommentsByAd($adId, $lastCommentId, $numComments) {
        try {
            $commentService = new SoapClient(MESSAGE_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $commentService->getCommentsByAd(array(
                "adId" => $adId,
                "lastCommentId" => $lastCommentId,
                "numComments" => $numComments
            ));
            
            $comments = array();
            if ( hasField($result, 'comment') && $result->comment ) {
                $comments = Comment_model::convertComments($result->comment);
            }
            return $comments;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Comments request failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
}
