<script langauge="javascript">
    var adRelistCallerElement;
    
    function startAdRelistModal(callerElement, adId, canProlong) {
        adRelistCallerElement = callerElement;
        
        var $adId = $("#ad_relist_form input[name=adId]");
        $adId.val(adId);
        
        if ( $('#adRelistContainer').length > 0 ) {
            $('#adRelistContainer').removeData("modal").modal('show');
            $("#adRelistContainer .modal-body").html('<div class="well ge-well ge-form">Please wait...</div>');
            $("#adRelistContainer #relistBtn").attr("disabled", !canProlong);
            
            $("#adRelistContainer .modal-body").load('<?=base_url()?>preview/' + adId + '?modal', function() {
                init_select();
                init_checkbox();
                init_files();
            });
        }
    }
    
    function ad_relist_modal() {
        if ( $("#ad_relist_form").length === 0 ) {
            return;
        }
        
        disable_buttons('#ad_relist_form button');
        var $adId = $("#ad_relist_form input[name=adId]");
        
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/relist',
            dataType: 'json',
            cache: false,
            data: {
                adId: $adId.val()
            }
        }).done(function(response) {
            enable_buttons('#ad_relist_form button');
            
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                window.location.reload();
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            enable_buttons('#ad_relist_form button');
        });
    }
    function ad_edit_modal() {
        if ( $("#ad_relist_form").length === 0 ) {
            return;
        }
        
        disable_buttons('#ad_relist_form button');
        var $adId = $("#ad_relist_form input[name=adId]");
        
        window.location.href = "<?=base_url()?>post/clone/" + $adId.val();
        
        //enable_buttons('#ad_relist_form button');
    }
</script>

<div id="adRelistContainer" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
    <div class="modal-header">
    	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <div class="modal-header-content">
            <div class="ge-modal_header">
                <label class="control-label" for="fieldset">
                    <h3>Choose to relist or edit as new gift</h3>
                </label>
            </div>
        </div>
    </div>
    
    <div class="modal-body"></div>
    
    <div class="modal-footer">
        <div class="ge-modal_footer">
            <form id="ad_relist_form">
                <input type="hidden" name="adId"/>
                
                <fieldset>
                    <div class="row-fluid">
                        <div class="span12">
                            <div class="control-group control-form">
                                <div class="controls">
                                    <button type="button" onclick="ad_relist_modal();" id="relistBtn" class="span6 mobile-two btn btn-ge">RELIST</button>
                                    <button type="button" onclick="ad_edit_modal()" class="span6 mobile-two btn pull-right">EDIT</button>
                                </div>
                            </div>
                        </div>
                    </div><!--./submit-->
                </fieldset>
            </form>
        </div>
    </div>
</div>
