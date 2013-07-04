<script langauge="javascript">
    function startMessage(callerElement, adId) {
        var $messageAdId = $("#message_post_form input[name=messageAdId]");
        $messageAdId.val(adId);

        $('#messageContainer').modal('show');
    }
</script>

<div id="messageContainer" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-body">
        
        <label class="control-label" for="fieldset">
            <blockquote>
                <p>
                    Message
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                </p>
            </blockquote>
        </label>
        
        <?=form_open('/ajax/message', array('id' => 'message_post_form'))?>
        
            <input type="hidden" name="messageAdId" value=""/>

            <div class="row-fluid ge-message ge-input ge-text">
                <div class="span9">
                    <textarea name="messageText" placeholder="Your message ..."></textarea>
                </div>
                <div class="span3">
                    <a id="addMessageBtn" class="btn btn-mini btn-block">Add</a>
                </div>
            </div>
        
        <?=form_close()?>
        
    </div>
</div>
