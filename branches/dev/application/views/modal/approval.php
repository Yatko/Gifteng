<script language="javascript">
    function startApprovalModal(adId) {
        $.getJSON('<?=base_url()?>ajax/approval?adId=' + adId, function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                $('#approvalContainer > .modal-body').html(response.<?=AJAX_STATUS_RESULT?>);
                $('#approvalContainer').removeData("modal").modal('show');
            } else {
                //TODO: unknown response received
            }
        });
    }
</script>

<div id="approvalContainer" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-header">
        Approval status
        <button type="button" class="close" data-dismiss="modal">×</button>
    </div>
    <div class="modal-body"></div>
    <div class="modal-footer">
        <button type="button" data-dismiss="modal" class="btn btn-large btn-ge"><i class="fui-cross pull-right"></i>OK</button>
    </div>
</div>
