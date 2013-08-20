<?php

/**
 * Input params:
 * 
 * message: Message_model
 * showTitle: boolean
 * showDelete: boolean
 */

if ( !isset($showTitle) ) $showTitle = false;
if ( !isset($showDelete) ) $showDelete = false;
$profile_link = $message->getFromProfileUrl();
$img = $message->getFromAvatarUrl();
$name = $message->fromFullName;
$since = $message->getCreateDateHumanTiming();
$text = $message->getSafeText();
$id = $message->id;
$ad_title = $message->getSafeAdTitle();
$request_id = $message->requestId;

if ( trim($ad_title) == '' ) $ad_title = '-';

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
        
        <? /** ?>
        <? if( $showDelete ): ?>
            <a class="fui-trash"></a>
        <? endif; ?>
        <? /**/ ?>
        
        <? if( $showTitle ): ?>
            <a href="<?=base_url()?>profile?message&<?=$request_id?>" class="ge-title ge-block"><?= $ad_title ?></a>
        <? endif; ?>
        
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
