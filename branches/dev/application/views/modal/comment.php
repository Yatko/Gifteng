<script langauge="javascript">
    function startCommentModal(callerElement, adId) {
        var $commentAdId = $("#comment_post_form input[name=commentAdId]");
        $commentAdId.val(adId);
        
        $('#commentContainer').removeData("modal").modal('show');
    }
    
    $(function() {
        $('#commentContainer').on('shown', function() {
            $("#comment_post_form textarea[name=commentText]").focus();
        });
    });
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
                    <button type="button" onclick="add_comment();" class="btn btn-mini btn-block">Add</button>
                </div>
            </div>
        
        <?=form_close()?>
        
    </div>
</div>
