<script language="javascript">
    var requestReceiveCallerElement;
    
    function startRequestReceiveModal(callerElement, requestId, adId, userId) {
        requestReceiveCallerElement = callerElement;
        
        $("#request_receive_form input[name=requestId]").val(requestId);
        $("#request_receive_form input[name=adId]").val(adId);
        $("#request_receive_form input[name=userId]").val(userId);
        
        if ( $('#requestReceiveContainer').length > 0 ) {
            $('#requestReceiveContainer').removeData("modal").modal('show');
        }
    }
    
    function request_receive_modal(callerElement) {
        if ( $("#request_receive_form").length === 0 ) {
            return;
        }
        if ( callerElement !== null ) {
            $(callerElement).attr("disabled", true);
        }
        
        var $requestId = $("#request_receive_form input[name=requestId]");
        var $adId = $("#request_receive_form input[name=adId]");
        var $userId = $("#request_receive_form input[name=userId]");
        
        var requestId = $requestId.val();
        var adId = $adId.val();
        var userId = $userId.val();
        
        request_receive(requestReceiveCallerElement, requestId, adId, userId, function() {
            if ( callerElement !== null ) {
                $(callerElement).removeAttr("disabled");
            }
            
            $requestId.val('');
            $adId.val('');
            $userId.val('');
            
            if ( $('#requestReceiveContainer').length > 0 ) {
                $('#requestReceiveContainer').modal('hide');
            }
        });
    }
</script>

<div id="requestReceiveContainer" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-header">
    	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <div class="modal-header-content">
            <div class="ge-modal_header">
                <label class="control-label" for="fieldset">
                    <h3>If you received the gift, click on CONFIRM.</h3>
                </label>
            </div>
        </div>
    </div>
    
    <div class="modal-footer">
    	<div class="ge-modal_footer">
            <form id="request_receive_form">
                <input type="hidden" name="requestId"/>
                <input type="hidden" name="adId"/>
                <input type="hidden" name="userId"/>

                <fieldset>
                    <div class="row-fluid">
                        <div class="span12">
                            <div class="control-group control-form">
                                <div class="controls">
                                    <button type="button" onclick="request_receive_modal(this);" class="span6 btn mobile-two">CONFIRM</button>
                                    <button type="button" data-dismiss="modal" class="span6 btn btn-ge mobile-two">CANCEL</button>
                                </div>
                            </div>
                        </div>
                    </div><!--./submit-->
                </fieldset>
            </form>
        </div>
    </div>
</div>