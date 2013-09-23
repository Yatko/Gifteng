<script langauge="javascript">
    function startMessageModal(requestId, toId) {
        var $messageRequestId = $("#message_post_form input[name=messageRequestId]");
        var $messageToId = $("#message_post_form input[name=messageToId]");
        
        $messageRequestId.val(requestId);
        $messageToId.val(toId);

        $('#messageContainer').removeData("modal").modal('show');
    }
    
    $(function() {
        $('#messageContainer').on('shown', function() {
            $("#message_post_form textarea[name=messageText]").focus();
        });
    });
</script>

<div id="messageContainer" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="model-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <div class="modal-header-content">
            <div class="ge-modal_header">
                <label class="control-label" for="fieldset">
                    <blockquote>
                        <p>Message</p>
                    </blockquote>
                </label>
            </div>
        </div>
    </div>
    <div class="modal-body">
        <form id="message_post_form">
            <input type="hidden" name="messageRequestId" value="" />
            <input type="hidden" name="messageToId" value="" />

            <div class="row-fluid ge-message ge-input ge-text">
                <div class="span9">
                    <textarea name="messageText" maxlength="<?=MESSAGE_MAX_SIZE?>" placeholder="Your message ..."></textarea>
                </div>
                <div class="span3">
                    <button type="button" onclick="add_message(this);" class="btn btn-mini btn-block">Add</button>
                </div>
            </div>
        </form>
    </div>
</div>
