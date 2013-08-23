<script language="javascript">
    var requestCallerElement;
    
    function startRequestCancelModal(callerElement, requestType, requestId, adId, userId) {
        requestCallerElement = callerElement;
        
        $("#request_cancel_form input[name=requestType]").val(requestType);
        $("#request_cancel_form input[name=requestId]").val(requestId);
        $("#request_cancel_form input[name=adId]").val(adId);
        $("#request_cancel_form input[name=userId]").val(userId);
        
        if ( $('#requestCancelContainer').length > 0 ) {
            $('#requestCancelContainer').removeData("modal").modal('show');
        }
    }
    
    function request_cancel_modal() {
        if ( $("#request_cancel_form").length === 0 ) {
            return;
        }
        
        var $requestType = $("#request_cancel_form input[name=requestType]");
        var $requestId = $("#request_cancel_form input[name=requestId]");
        var $adId = $("#request_cancel_form input[name=adId]");
        var $userId = $("#request_cancel_form input[name=userId]");
        
        var requestType = $requestType.val();
        var requestId = $requestId.val();
        var adId = $adId.val();
        var userId = $userId.val();
        
        request_cancel(requestCallerElement, requestType, requestId, adId, userId, function() {
            $requestType.val('');
            $requestId.val('');
            $adId.val('');
            $userId.val('');
            
            if ( $('#requestCancelContainer').length > 0 ) {
                $('#requestCancelContainer').modal('hide');
            }
        });
    }
</script>

<div id="requestCancelContainer" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-header">
    	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <div class="modal-header-content">
        	<div class="ge-modal_header">
		        <label class="control-label" for="fieldset">
	                <h3>
	                    Are you sure you want to cancel the request?
	                </h3>
		        </label>
        	</div>
        </div>
    </div>
    
    <div class="modal-footer">
    	<div class="ge-modal_footer">
	        <form id="request_cancel_form">
	            <input type="hidden" name="requestType"/>
	            <input type="hidden" name="requestId"/>
	            <input type="hidden" name="adId"/>
	            <input type="hidden" name="userId"/>
	
	            <fieldset>
	                <div class="row-fluid">
	                    <div class="span12">
	                        <div class="control-group control-form">
	                            <div class="controls">
	                                <button type="button" onclick="request_cancel_modal();" class="span6 btn">YES</button>
	                                <button type="button" data-dismiss="modal" class="span6 btn btn-ge">NO</button>
	                            </div>
	                        </div>
	                    </div>
	                </div><!--./submit-->
	            </fieldset>
	        </form>
        </div>
    </div>
</div>