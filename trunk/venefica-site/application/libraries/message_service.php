<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * Comment and message service
 *
 * @author gyuszi
 */
class Message_service {
    
    public function __construct() {
        log_message(DEBUG, "Initializing Message_service");
    }
    
    //**************
    //* commenting *
    //**************
    
    /**
     * 
     * @param long $adId
     * @param Comment_model $comment
     * @return long comment id
     * @throws Exception
     */
    public function addCommentToAd($adId, $comment) {
        try {
            $messageService = new SoapClient(MESSAGE_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $messageService->addCommentToAd(array(
                "adId" => $adId,
                "comment" => $comment
            ));
            return $result->commentId;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Adding comment to ad (adId: ' . $adId . ') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param Comment_model $comment
     * @throws Exception
     */
    public function updateComment($comment) {
        try {
            $messageService = new SoapClient(MESSAGE_SERVICE_WSDL, getSoapOptions(loadToken()));
            $messageService->addCommentToAd(array("comment" => $comment));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Update comment failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $adId
     * @param long $lastCommentId
     * @param int $numComments
     * @return array of Comment_model
     * @throws Exception
     */
    public function getCommentsByAd($adId, $lastCommentId, $numComments) {
        try {
            $messageService = new SoapClient(MESSAGE_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $messageService->getCommentsByAd(array(
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
    
    //*************
    //* messaging *
    //*************
    
    /**
     * 
     * @param Message_model $message
     * @return long message id
     * @throws Exception
     */
    public function sendMessage($message) {
        try {
            $messageService = new SoapClient(MESSAGE_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $messageService->sendMessage(array("message" => $message));
            return $result->messageId;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Adding message failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }

    /**
     * 
     * @param Message_model $message
     * @throws Exception
     */
    public function updateMessage($message) {
        try {
            $messageService = new SoapClient(MESSAGE_SERVICE_WSDL, getSoapOptions(loadToken()));
            $messageService->updateMessage(array("message" => $message));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Update message failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @return array of Message_model
     * @throws Exception
     */
    public function getAllMessages() {
        try {
            $messageService = new SoapClient(MESSAGE_SERVICE_WSDL, getSoapOptions(loadToken()));
            $result = $messageService->getCommentsByAd();
            
            $messages = array();
            if ( hasField($result, 'message') && $result->message ) {
                $messages = Message_model::convertMessages($result->message);
            }
            return $messages;
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Getting all messages failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $messageId
     * @throws Exception
     */
    public function hideMessage($messageId) {
        try {
            $messageService = new SoapClient(MESSAGE_SERVICE_WSDL, getSoapOptions(loadToken()));
            $messageService->hideMessage(array("messageId" => $messageId));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Hide message (messageId: ' . $messageId . ') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }
    
    /**
     * 
     * @param long $messageId
     * @throws Exception
     */
    public function deleteMessage($messageId) {
        try {
            $messageService = new SoapClient(MESSAGE_SERVICE_WSDL, getSoapOptions(loadToken()));
            $messageService->deleteMessage(array("messageId" => $messageId));
        } catch ( Exception $ex ) {
            log_message(ERROR, 'Delete message (messageId: ' . $messageId . ') failed! '.$ex->faultstring);
            throw new Exception($ex->faultstring);
        }
    }

    //*********
    //* share *
    //*********
    
    //...
}
