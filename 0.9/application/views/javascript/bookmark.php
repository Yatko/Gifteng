<script langauge="javascript">
    function bookmark(callerElement, adId) {
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/bookmark?adId=' + adId,
            dataType: 'json',
            cache: false
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                if ( callerElement !== null ) {
                    var $element = $(callerElement);
                    $element.addClass('disabled');
                    $element.trigger('bookmarked', [adId]);
                }
                
                if ( $('.ad_bookmark_' + adId).length > 0 ) {
                    var num_bookmarks = response.<?=AJAX_STATUS_RESULT?>.<?=AD_BOOKMARKS_NUM?>;
                    $('.ad_bookmark_' + adId).text(num_bookmarks);
                }
                if ( $('.user_bookmark').length > 0 ) {
                    var num_bookmarks = response.<?=AJAX_STATUS_RESULT?>.<?=USER_BOOKMARKS_NUM?>;
                    $('.user_bookmark').text(num_bookmarks);
                }
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
    
    function remove_bookmark(callerElement, adId) {
        if ( callerElement !== null ) {
            var $element = $(callerElement);
            $element.addClass('disabled');
        }
        
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/remove_bookmark?adId=' + adId,
            dataType: 'json',
            cache: false
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                if ( $('#ad_' + adId) ) {
                    $('#ad_' + adId).removeClass('masonry-brick');
                    $('#ad_' + adId).addClass('hide');
                }
                
                if ( $('.ad_bookmark_' + adId).length > 0 ) {
                    var num_bookmarks = response.<?=AJAX_STATUS_RESULT?>.<?=AD_BOOKMARKS_NUM?>;
                    $('.ad_bookmark_' + adId).text(num_bookmarks);
                }
                if ( $('.user_bookmark').length > 0 ) {
                    var num_bookmarks = response.<?=AJAX_STATUS_RESULT?>.<?=USER_BOOKMARKS_NUM?>;
                    $('.user_bookmark').text(num_bookmarks);
                }
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
</script>
