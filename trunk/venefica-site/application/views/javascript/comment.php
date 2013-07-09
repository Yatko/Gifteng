<script langauge="javascript">
    function show_comment_rest(callerElement, commentId) {
        $('.comment_' + commentId + '_separator').addClass('hide');
        $('.comment_' + commentId + '_rest').removeClass('hide').css('display', 'inline');
        $(callerElement).addClass('hide');
    }
    
    function add_comment() {
        if ( $("#comment_post_form").length === 0 ) {
            return;
        }

        var $commentAdId = $("#comment_post_form input[name=commentAdId]");
        var $commentText = $("#comment_post_form textarea[name=commentText]");

        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/comment',
            dataType: 'json',
            cache: false,
            data: {
                commentAdId: $commentAdId.val(),
                commentText: $commentText.val()
            }
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                var adId = $commentAdId.val();

                $commentAdId.val('');
                $commentText.val('');

                if ( $('.ad_comment_' + adId).length > 0 ) {
                    var num_comments = response.<?=AJAX_STATUS_RESULT?>;
                    $('.ad_comment_' + adId).text(num_comments);
                }

                if ( $('#commentContainer').length > 0 ) {
                    $('#commentContainer').modal('hide');
                }
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
</script>
