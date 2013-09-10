<script langauge="javascript">
    function startRequestViewModal(requestId, requestType, userId) {
        $('#requestContainer').removeData('modal').modal({
            remote: '<?=base_url()?>request/' + requestId + '?modal&' + requestType + '&userId=' + userId,
            show: true
        });
    }
    function initRequestViewModal() {
        $('.ge-request').on('request_canceled', function(event, requestId, adId, resultHtml, resultNum) {
            if ( $('#ad_' + adId).length > 0 ) {
                $('#ad_' + adId).html(resultHtml);
            }
        });
    }
    
    $(function() {
        $('#requestContainer').on('hidden', function() {
            $(this).removeData("modal");
            $('#requestContainer > .modal-body').html('');
        });
    });
</script>

<div id="requestContainer" class="modal hide fade">
    <div class="modal-body"></div>
</div>
