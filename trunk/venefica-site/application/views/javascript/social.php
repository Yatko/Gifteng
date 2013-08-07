<? /** ?>
<script language="javascript">
    function connect_to_facebook() {
        var url = '<?=CONNECT_TO_FACEBOOK_URL . '&AuthToken='.urlencode(loadToken())?>';
        var name = 'social';
        open_window_clear(url, name, null, null);
    }
    function sign_in_facebook() {
        var url = '<?=SIGN_IN_FACEBOOK_URL . '&AuthToken='.urlencode(loadToken())?>';
        var name = 'social';
        open_window_clear(url, name, null, null);
    }
    
    function connect_to_twitter() {
        var url = '<?=CONNECT_TO_TWITTER_URL . '&AuthToken='.urlencode(loadToken())?>';
        var name = 'social';
        open_window_clear(url, name, null, null);
    }
    function sign_in_twitter() {
        var url = '<?=SIGN_IN_TWITTER_URL . '&AuthToken='.urlencode(loadToken())?>';
        var name = 'social';
        open_window_clear(url, name, null, null);
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
<? /**/ ?>

<?
$user = $this->usermanagement_service->loadUser();
$fullName = $user->getFullName();
?>

<script language="javascript">
    function shareOnFacebook(title, itemUrl, imgUrl) {
        FB.ui({
            link: itemUrl,
            picture: imgUrl,
            method: 'feed',
            display: 'popup',
            name: title + ' - FREE on Gifteng',
            caption: 'www.gifteng.com',
            description: 'Gifteng is a social community where you can give and receive things you love for free.'
        }, function(response){});
    }
    function shareOnTwitter(title, itemUrl, imgUrl) {
        var width = 575;
        var height = 450;
        var safeTitle = encodeURIComponent(title.replace(/&amp;/g, "&"));
        safeTitle = safeTitle.substring(0, 30);
        var safeItemUrl = encodeURIComponent(itemUrl.replace(/&amp;/g, "&"));
        var safeImgUrl = encodeURIComponent(imgUrl.replace(/&amp;/g, "&"));
        open_window_clear('https://twitter.com/share?text=' + safeTitle + '&url=' + safeItemUrl + '&via=gifteng&button_hashtag=free', '', width, height);
    }
    function shareOnPinterest(title, itemUrl, imgUrl) {
        var width = 575;
        var height = 450;
        var safeItemUrl = encodeURIComponent(itemUrl.replace(/&amp;/g, "&"));
        var safeImgUrl = encodeURIComponent(imgUrl.replace(/&amp;/g, "&"));
        var description = 'Check out <?=$fullName?>\'s "' + title + '" free gift @Gifteng';
        var safeDescription = encodeURIComponent(description.replace(/&amp;/g, "&"));
        open_window_clear('//pinterest.com/pin/create/button/?url=' + safeItemUrl + '&media=' + safeImgUrl + '&description=' + safeDescription, '', width, height);
    }
</script>
