<script langauge="javascript">
    function bookmark(callerElement, adId) {
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/bookmark?adId=' + adId,
            dataType: 'json',
            cache: false
        }).done(function(response) {
            if ( !response || response == '' ) {
                //TODO: empty result
            } else if ( response.<?=AJAX_STATUS_ERROR?> ) {
                //TODO
            } else if ( response.<?=AJAX_STATUS_RESULT?> ) {
                if ( callerElement !== null ) {
                    var $element = $(callerElement);
                    $element.addClass('disabled');
                }
                
                if ( $('.ad_bookmark_' + adId).length > 0 ) {
                    var num_bookmarks = response.<?=AJAX_STATUS_RESULT?>;
                    $('.ad_bookmark_' + adId).text(num_bookmarks);
                }
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
</script>
