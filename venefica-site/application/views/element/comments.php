<?

/**
 * Input params:
 * 
 * comments: array of Comment_model
 * ad: Ad_model
 * canComment: boolean (default: false)
 */

if ( !isset($canComment) ) $canComment = false;

if ( $ad != null ) {
    $ad_id = $ad->id;
} else {
    $ad_id = '';
}

?>

<? if( (isset($comments) && is_array($comments) && count($comments) > 0) || $canComment ): ?>

    <div class="row-fluid ge-comments">
        <div class="span12">
            
            <? if( isset($comments) && is_array($comments) && count($comments) > 0 ): ?>
                <div class="row-fluid ge-messagelist">
                    <div class="span12">

                        <? foreach ($comments as $comment): ?>
                            <?
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
                        <? endforeach; ?>

                    </div>
                </div><!--./ge-messagelist-->
            <? endif; ?>
            
            <? if( $canComment ): ?>
            
                <?=form_open('/ajax/comment', array('id' => 'comment_post_form'))?>
                
                <input type="hidden" name="commentAdId" value="<?= $ad_id ?>"/>

                <div class="row-fluid ge-input">
                    <div class="span12">
                        <div class="row-fluid ge-message">
                            <div class="span9 ge-text">
                                <textarea name="commentText" placeholder="Your message ..."></textarea>
                            </div>
                            <div class="span3 ge-text">
                                <a id="addCommentBtn" class="btn btn-mini btn-block">Add</a>
                            </div>
                        </div>
                    </div>
                </div><!--./ge-input-->

                <?=form_close()?>
            
            <? endif; ?>
        </div>
    </div><!--./ge-comments-->
<? endif; ?>
