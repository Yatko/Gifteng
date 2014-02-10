<script language="javascript">
    function startApprovalModal(adId, revision) {
        $("#approval_modal_view_gift").attr('href', '<?=base_url()?>view/' + adId);
        
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/approval',
            dataType: 'json',
            cache: false,
            data: {
                adId: adId,
                revision: revision
            }
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                $('#approvalContainer > .modal-body p').html(response.<?=AJAX_STATUS_RESULT?>);
                $('#approvalContainer').removeData("modal").modal('show');
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //
        });
    }
</script>

<div id="approvalContainer" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <div class="modal-header-content">
            <div class="ge-modal_header">
                <label class="control-label">
                    <h3>Unfortunately your gift can’t be posted because...</h3>
                </label>
            </div>
        </div>
    </div>
    <div class="modal-body">
    	<div class="well ge-well ge-form">
            <p></p>
        </div>
    </div>
    <div class="modal-footer">
        <div class="ge-modal_footer">
            <button type="button" data-dismiss="modal" class="btn btn-large"><i class="fui-cross pull-right"></i>OK</button>
            <a id="approval_modal_view_gift" class="btn btn-large btn-ge">View Gift</a>
        </div>
    </div>
</div>
