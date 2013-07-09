<?

/**
 * Input params:
 * 
 * messages: array of Message_model
 * ad: Ad_model
 * to: User_model
 * canMessage: boolean (default: true)
 */

if ( !isset($canMessage) ) $canMessage = false;

if ( $ad != null ) {
    $ad_id = $ad->id;
} else {
    $ad_id = '';
}

if ( $to != null ) {
    $to_id = $to->id;
} else {
    $to_id = '';
}

?>

<? if( (isset($messages) && is_array($messages) && count($messages) > 0) || $canMessage ): ?>
    
    <div class="row-fluid ge-messages">
        <div class="span12">

            <? if( isset($messages) && is_array($messages) && count($messages) > 0 ): ?>
            
                <div class="row-fluid ge-conversation">
                    <div class="span12">
                        
                        <? foreach ($messages as $message): ?>
                            <?
                            $img = $message->getFromAvatarUrl();
                            $name = $message->fromFullName;
                            $profile_link = $message->getFromProfileUrl();
                            $id = $message->id;
                            $text = trim($message->text);
                            $since = $message->getCreateDateHumanTiming();
                            
                            if (trim($since) != '') $since = $since . ' ago';
                            
                            if (strlen($text) > MESSAGE_MAX_LENGTH) {
                                $text_rest = substr($text, MESSAGE_MAX_LENGTH);
                                $text = substr($text, 0, MESSAGE_MAX_LENGTH);
                            } else {
                                $text_rest = '';
                            }
                            ?>
                            
                            <div class="row-fluid ge-message">
                                <div class="ge-user-image">
                                    <a href="<?= $profile_link ?>"><img src="<?= $img ?>" class="img img-rounded"></a>
                                </div>
                                <div class="ge-text">
                                    <a class="ge-name" href="<?= $profile_link ?>"><?= $name ?></a>
                                    <span class="ge-date"><?= $since ?></span>
                                    <span class="ge-block">
                                        <?= $text ?>

                                        <? if ($text_rest != ''): ?>
                                            <span class="message_<?= $id ?>_separator">...</span>
                                            <span class="message_<?= $id ?>_rest hide"><?= $text_rest ?></span>

                                            <a class="text-note link" onclick="show_message_rest(this, <?= $id ?>);">read more</a>
                                        <? endif; ?>
                                    </span>
                                </div>
                            </div><!--./ge-message-->
                        <? endforeach; ?>
                        
                    </div>
                </div><!--./ge-conversation-->
            
            <? endif; ?>

            <? if( $canMessage ): ?>
            
                <?=form_open('/ajax/message', array('id' => 'message_post_form'))?>
                
                <input type="hidden" name="messageAdId" value="<?= $ad_id ?>"/>
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
