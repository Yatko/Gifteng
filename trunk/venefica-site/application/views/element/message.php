<?php

/**
 * Input params:
 * 
 * message: Message_model
 */

$profile_link = $message->getFromProfileUrl();
$img = $message->getFromAvatarUrl();
$name = $message->fromFullName;
$since = $message->getCreateDateHumanTiming();
$text = trim($message->text);
$id = $message->id;

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
