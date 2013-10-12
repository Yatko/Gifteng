<script langauge="javascript">
    var adDeleteCallerElement;
    
    function startAdDeleteModal(callerElement, adId) {
        adDeleteCallerElement = callerElement;
        
        var $adId = $("#ad_delete_form input[name=adId]");
        $adId.val(adId);
        
        if ( $('#adDeleteContainer').length > 0 ) {
            $('#adDeleteContainer').removeData("modal").modal('show');
        }
    }
    
    function ad_delete_modal() {
        if ( $("#ad_delete_form").length === 0 ) {
            return;
        }
        
        disable_buttons('#ad_delete_form button');
        var $adId = $("#ad_delete_form input[name=adId]");
        
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/delete_ad',
            dataType: 'json',
            cache: false,
            data: {
                adId: $adId.val()
            }
        }).done(function(response) {
            enable_buttons('#ad_delete_form button');
            
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                var adId = $adId.val();
                $adId.val('');
                
                if ( adDeleteCallerElement !== null ) {
                    var $element = $(adDeleteCallerElement);
                    $element.trigger('ad_deleted', [adId]);
                }
                
                if ( $('#adDeleteContainer').length > 0 ) {
                    $('#adDeleteContainer').modal('hide');
                }
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            enable_buttons('#ad_delete_form button');
        });
    }
</script>

<div id="adDeleteContainer" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
    <div class="modal-header">
    	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <div class="modal-header-content">
            <div class="ge-modal_header">
                <label class="control-label" for="fieldset">
                    <h3>Are you sure you want to delete it?</h3>
                </label>
            </div>
        </div>
    </div>
    
    <div class="modal-footer">
    	<div class="ge-modal_footer">
            <form id="ad_delete_form">
                <input type="hidden" name="adId"/>

                <fieldset>
                    <div class="row-fluid">
                        <div class="span12">
                            <div class="control-group control-form">
                                <div class="controls">
                                    <button type="button" onclick="ad_delete_modal();" class="span6 mobile-two btn">YES</button>
                                    <button type="button" data-dismiss="modal" class="span6 mobile-two btn btn-ge pull-right">NO</button>
                                </div>
                            </div>
                        </div>
                    </div><!--./submit-->
                </fieldset>
            </form>
        </div>
    </div>
</div>
