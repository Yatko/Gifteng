<script language="javascript">
    function approve_ad(adId) {
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/approve',
            dataType: 'json',
            cache: false,
            data: {
                adId: adId
            }
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                $('#approve_btn_' + adId).attr("disabled", true);
                $('#unapprove_btn_' + adId).attr("disabled", true);
                $('#online_btn_' + adId).removeAttr("disabled");
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
    function unapprove_ad(adId) {
        var reason = $("#reason_" + adId).val();
        
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/unapprove',
            dataType: 'json',
            cache: false,
            data: {
                adId: adId,
                reason: reason
            }
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                $('#approve_btn_' + adId).attr("disabled", true);
                $('#unapprove_btn_' + adId).attr("disabled", true);
                $("#reason_" + adId).val('');
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
    function online_ad(adId) {
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/online',
            dataType: 'json',
            cache: false,
            data: {
                adId: adId
            }
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                if ( $("#ad_" + adId).length > 0 ) {
                    $("#ad_" + adId).hide();
                }
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
</script>
