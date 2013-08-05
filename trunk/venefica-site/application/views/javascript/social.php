<script language="javascript">
    function connect_to_facebook() {
        var url = '<?=CONNECT_TO_FACEBOOK_URL . '&AuthToken='.urlencode(loadToken())?>';
        var title = 'social';
        open_window_clear(url, title);
    }
    function sign_in_facebook() {
        var url = '<?=SIGN_IN_FACEBOOK_URL . '&AuthToken='.urlencode(loadToken())?>';
        var title = 'social';
        open_window_clear(url, title);
    }
    
    function connect_to_twitter() {
        var url = '<?=CONNECT_TO_TWITTER_URL . '&AuthToken='.urlencode(loadToken())?>';
        var title = 'social';
        open_window_clear(url, title);
    }
    function sign_in_twitter() {
        var url = '<?=SIGN_IN_TWITTER_URL . '&AuthToken='.urlencode(loadToken())?>';
        var title = 'social';
        open_window_clear(url, title);
    }
    
    function shareMessage(text) {
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/share_message',
            dataType: 'json',
            cache: false,
            data: {
                text: text,
            }
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
</script>
