<?php

/**
 * Input params:
 * 
 * comment: Comment_model
 */

$img = $comment->getPublisherAvatarUrl();
$name = $comment->publisherFullName;
$profile_link = $comment->getPublisherProfileUrl();
$id = $comment->id;
$text = trim($comment->text);
$since = $comment->getCreateDateHumanTiming();

if (trim($since) != '') $since = $since . ' ago';

if (strlen($text) > COMMENT_MAX_LENGTH) {
    $text_rest = substr($text, COMMENT_MAX_LENGTH);
    $text = substr($text, 0, COMMENT_MAX_LENGTH);
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
                <span class="comment_<?= $id ?>_separator">...</span>
                <span class="comment_<?= $id ?>_rest hide"><?= $text_rest ?></span>

                <a class="text-note link" onclick="show_comment_rest(this, <?= $id ?>);">read more</a>
            <? endif; ?>
        </span>
    </div>
</div><!--./ge-message-->
