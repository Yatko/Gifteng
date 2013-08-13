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
                $('#approvalContainer > .modal-body h3').html(response.<?=AJAX_STATUS_RESULT?>);
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
        Unfortunately your gift can’t be posted
        <button type="button" class="close" data-dismiss="modal">×</button>
    </div>
    <div class="modal-body">
        <h3></h3>
    </div>
    <div class="modal-footer">
        <a id="approval_modal_view_gift" class="btn btn-large btn-ge">View Gift</a>
        <button type="button" data-dismiss="modal" class="btn btn-large btn-ge"><i class="fui-cross pull-right"></i>OK</button>
    </div>
</div>
