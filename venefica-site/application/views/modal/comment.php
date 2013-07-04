<script langauge="javascript">
    function startComment(callerElement, adId) {
        var $commentAdId = $("#comment_post_form input[name=commentAdId]");
        $commentAdId.val(adId);

        $('#commentContainer').modal('show');
    }
</script>

<div id="commentContainer" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-body">
        
        <label class="control-label" for="fieldset">
            <blockquote>
                <p>
                    Comment
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                </p>
            </blockquote>
        </label>
        
        <?=form_open('/ajax/comment', array('id' => 'comment_post_form'))?>
        
            <input type="hidden" name="commentAdId" value=""/>

            <div class="row-fluid ge-message ge-input ge-text">
                <div class="span9">
                    <textarea name="commentText" placeholder="Your comment ..."></textarea>
                </div>
                <div class="span3">
                    <a id="addCommentBtn" class="btn btn-mini btn-block">Add</a>
                </div>
            </div>
        
        <?=form_close()?>
        
    </div>
</div>
