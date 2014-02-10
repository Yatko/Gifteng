<script langauge="javascript">
    function startCommentModal(adId) {
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
        <form id="comment_post_form">
            <input type="hidden" name="commentAdId" value=""/>

            <div class="row-fluid ge-message ge-input ge-text">
                <div class="span9">
                    <textarea name="commentText" maxlength="<?=COMMENT_MAX_SIZE?>" placeholder="Your comment ..."></textarea>
                </div>
                <div class="span3">
                    <button type="button" style="height:60px;" onclick="add_comment(this);" class="btn btn-mini btn-block">Add</button>
                </div>
            </div>
        </form>
    </div>
</div>
