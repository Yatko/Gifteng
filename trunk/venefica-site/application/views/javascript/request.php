<script langauge="javascript">
    $(function() {
        $('#requestBtn').click(function() {
            if ( $("#request_post_form").length === 0 ) {
                return;
            }
            
            var $requestAdId = $("#request_post_form input[name=requestAdId]");
            var $requestText = $("#request_post_form textarea[name=requestText]");
            
            $.ajax({
                type: 'POST',
                url: '<?=base_url()?>ajax/request',
                dataType: 'json',
                cache: false,
                data: {
                    requestAdId: $requestAdId.val(),
                    requestText: $requestText.val()
                }
            }).done(function(response) {
                if ( !response || response === '' ) {
                    //TODO: empty result
                } else if ( response.<?=AJAX_STATUS_ERROR?> ) {
                    //TODO
                } else if ( response.<?=AJAX_STATUS_RESULT?> ) {
                    $requestAdId.val('');
                    $requestText.val('');
                    
                    $('#requestBtn').trigger('requested');
                    
                    if ( $('#requestContainer').length > 0 ) {
                        $('#requestContainer').modal('hide');
                    }
                } else {
                    //TODO: unknown response received
                }
            }).fail(function(data) {
                //TODO
            });
        });
    });
</script>
