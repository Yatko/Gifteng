<script langauge="javascript">
    function follow_unfollow(userId) {
        if ( $('.user_' + userId).length === 0 ) {
            return;
        }
        
        var elem = $('.user_' + userId)[0];
        var $elem = $(elem);
        if ( $elem.hasClass('ge-user-follow') ) {
            unfollow(userId);
        } else {
            follow(userId);
        }
    }
    function follow(userId) {
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/follow?userId=' + userId,
            dataType: 'json',
            cache: false
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                if ( $('.user_' + userId).length > 0 ) {
                    $('.user_' + userId).each(function() {
                        var $this = $(this);
                        $this.removeClass('ge-user-unfollow');
                        $this.addClass('ge-user-follow');

                        $this.html('Unfollow');
                        $this.text('Unfollow');
                    });
                }
                
                if ( $('.user_following').length > 0 ) {
                    var num_following = response.<?=AJAX_STATUS_RESULT?>.<?=USER_FOLLOWINGS_NUM?>;
                    $('.user_following').text(num_following);
                }
                if ( $('.user_follower').length > 0 ) {
                    var num_follower = response.<?=AJAX_STATUS_RESULT?>.<?=USER_FOLLOWERS_NUM?>;
                    $('.user_follower').text(num_follower);
                }
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
    
    function unfollow(userId) {
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/unfollow?userId=' + userId,
            dataType: 'json',
            cache: false
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                if ( $('#user_' + userId).length > 0 ) {
                    $('#user_' + userId).trigger('unfollowed', [userId]);
                }
                
                if ( $('.user_' + userId).length > 0 ) {
                    $('.user_' + userId).each(function() {
                        var $this = $(this);
                        $this.removeClass('ge-user-follow');
                        $this.addClass('ge-user-unfollow');

                        $this.html('Follow');
                        $this.text('Follow');
                    });
                }
                
                if ( $('.user_following').length > 0 ) {
                    var num_following = response.<?=AJAX_STATUS_RESULT?>.<?=USER_FOLLOWINGS_NUM?>;
                    $('.user_following').text(num_following);
                }
                if ( $('.user_follower').length > 0 ) {
                    var num_follower = response.<?=AJAX_STATUS_RESULT?>.<?=USER_FOLLOWERS_NUM?>;
                    $('.user_follower').text(num_follower);
                }
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
</script>
