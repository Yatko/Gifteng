<?

/**
 * Input params:
 * 
 * messages: array of Message_model
 * request: Request_model
 * to: User_model
 * canMessage: boolean (default: true)
 * showProfileLinks: boolean
 */

if ( !isset($canMessage) ) $canMessage = false;
if ( !isset($showProfileLinks)) $showProfileLinks = false;

if ( $request != null ) {
    $request_id = $request->id;
} else {
    $request_id = '';
}

if ( $to != null ) {
    $to_id = $to->id;
} else {
    $to_id = '';
}

?>

<? if( (isset($messages) && is_array($messages) && count($messages) > 0) || $canMessage ): ?>
    
    <div class="row-fluid ge-messages" id="messages">
        <div class="span12">

            <? if( isset($messages) && is_array($messages) && count($messages) > 0 ): ?>
            
                <div class="row-fluid ge-conversation">
                    <div class="span12">
                        
                        <? foreach ($messages as $message): ?>
                            
                            <? $this->load->view('element/message', array('message' => $message, 'showTitle' => false, 'showDelete' => false)); ?>

                        <? endforeach; ?>
                        
                    </div>
                </div><!--./ge-conversation-->
            
            <? endif; ?>

            <? if( $canMessage ): ?>
            
                <?=form_open('/ajax/message', array('id' => 'message_post_form'))?>
                
                <input type="hidden" name="messageRequestId" value="<?= $request_id ?>"/>
                <input type="hidden" name="messageToId" value="<?= $to_id ?>"/>

                <div class="row-fluid ge-input">
                    <div class="span12">
                        <div class="row-fluid ge-message">
                            <div class="span9 ge-text">
                                <textarea name="messageText" placeholder="Your message ..."></textarea>
                            </div>
                            <div class="span3 ge-text">
                                <button type="button" onclick="add_message();" class="btn btn-mini btn-block">Add</button>
                            </div>
                        </div>
                    </div>
                </div><!--./ge-input-->

                <?=form_close()?>
            
            <? endif; ?>
        </div>
    </div><!--./ge-messages-->
<? endif; ?>
