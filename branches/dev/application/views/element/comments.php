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
                            
                            <? $this->load->view('element/comment', array('comment' => $comment)); ?>
                            
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
                                <textarea name="commentText" placeholder="Your comment ..."></textarea>
                            </div>
                            <div class="span3 ge-text">
                                <button type="button" onclick="add_comment();" class="btn btn-mini btn-block">Add</button>
                            </div>
                        </div>
                    </div>
                </div><!--./ge-input-->

                <?=form_close()?>
            
            <? endif; ?>
        </div>
    </div><!--./ge-comments-->
<? endif; ?>
