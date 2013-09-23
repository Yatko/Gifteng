<?php

/**
 * Input params:
 * 
 * message: Message_model
 * showTitle: boolean
 * showDelete: boolean
 * showProfileLinks: boolean
 * currentUser: User_model
 */

if ( !isset($showTitle) ) $showTitle = false;
if ( !isset($showDelete) ) $showDelete = false;
if ( !isset($showProfileLinks)) $showProfileLinks = false;

if ( !$showProfileLinks && $message->fromId == $currentUser->id ) {
    $profile_link = $message->getToProfileUrl();
    $name = $message->toFullName;
    $img = $message->getToAvatarUrl();
    $text = '&crarr; ' . $message->getSafeText();
    $read = true;
} else {
    $profile_link = $message->getFromProfileUrl();
    $name = $message->fromFullName;
    $img = $message->getFromAvatarUrl();
    $text = $message->getSafeText();
    $read = $message->read;
}

$since = $message->getCreateDateHumanTiming();
$id = $message->id;
$ad_title = $message->getSafeAdTitle();
$request_id = $message->requestId;
$message_link = base_url().'profile?message&'.$request_id;

if ( trim($ad_title) == '' ) $ad_title = '-';

/*
if (strlen($text) > MESSAGE_MAX_LENGTH) {
    $text_rest = substr($text, MESSAGE_MAX_LENGTH);
    $text = substr($text, 0, MESSAGE_MAX_LENGTH);
} else {
    $text_rest = '';
}
*/
$text_rest = '';

?>

<div class="row-fluid ge-message">
    
    <? if( !$showProfileLinks ): ?>
        <a href="<?=$message_link?>">
    <? endif; ?>
    
    <span class="ge-user-image">
        <? if( $showProfileLinks ): ?>
            <a href="<?= $profile_link ?>"><img src="<?= $img ?>" class="img img-rounded"></a>
        <? else: ?>
            <img src="<?= $img ?>" class="img img-rounded">
        <? endif; ?>
    </span>
    <span class="ge-text">
        <? if( $showProfileLinks ): ?>
            <a class="ge-name" href="<?= $profile_link ?>"><?= $name ?></a>
        <? else: ?>
            <span class="ge-name"><?= $name ?></span>
        <? endif; ?>
        <span class="ge-date"><?= $since ?></span>
        
        <? /** ?>
        <? if( $showDelete ): ?>
            <a class="fui-trash"></a>
        <? endif; ?>
        <? /**/ ?>
        
        <? if( $showTitle ): ?>
            <? if( $showProfileLinks ): ?>
                <a href="<?=$message_link?>" class="ge-title ge-block" <?=$read ? '' : 'style="color:#00bebe;"'?>><?= $ad_title ?></a>
            <? else: ?>
                <span class="ge-title ge-block" <?=$read ? '' : 'style="color:#00bebe;"'?>><?= $ad_title ?></span>
            <? endif; ?>
        <? endif; ?>
        
        <span class="ge-block">
            <?= $text ?>

            <? if ($text_rest != ''): ?>
                <span class="message_<?= $id ?>_separator">...</span>
                <span class="message_<?= $id ?>_rest hide"><?= $text_rest ?></span>

                <a class="text-note link" onclick="show_message_rest(this, <?= $id ?>);">read more</a>
            <? endif; ?>
        </span>
    </span>
            
    <? if( !$showProfileLinks ): ?>
        </a>
    <? endif; ?>
    
</div>
