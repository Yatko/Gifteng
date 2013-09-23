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

    <div class="row-fluid ge-comments" id="ad_<?=$ad_id?>_comments">
        <div class="span12">
            
            <div class="row-fluid ge-messagelist">
                <div class="span12">
                
                <? if( isset($comments) && is_array($comments) && count($comments) > 0 ): ?>
                    <? foreach ($comments as $comment): ?>

                        <? $this->load->view('element/comment', array('comment' => $comment)); ?>

                    <? endforeach; ?>
                <? endif; ?>
                    
                </div>
            </div><!--./ge-messagelist-->
            
            <? if( $canComment ): ?>
            
                <form id="comment_post_form">
                    <input type="hidden" name="commentAdId" value="<?= $ad_id ?>"/>
                    
                    <div class="row-fluid ge-input">
                        <div class="span12">
                            <div class="row-fluid ge-message">
                                <div class="span9 ge-text">
                                    <textarea name="commentText" maxlength="<?=COMMENT_MAX_SIZE?>" placeholder="Your comment ..."></textarea>
                                </div>
                                <div class="span3 ge-text">
                                    <button type="button" onclick="add_comment(this);" class="btn btn-mini btn-block">Add</button>
                                </div>
                            </div>
                        </div>
                    </div><!--./ge-input-->
                </form>
            
            <? endif; ?>
        </div>
    </div><!--./ge-comments-->
<? endif; ?>
