<script langauge="javascript">
    function show_message_rest(callerElement, messageId) {
        $('.message_' + messageId + '_separator').addClass('hide');
        $('.message_' + messageId + '_rest').removeClass('hide').css('display', 'inline');
        $(callerElement).addClass('hide');
    }
    
    function add_message() {
        if ( $("#message_post_form").length === 0 ) {
            return;
        }

		var $name = "<?=$user->getFullName()?>";
		var $profile_link = "<?=$user->getProfileUrl()?>";
		var $img = "<?=$user->getAvatarUrl()?>";
        var $messageRequestId = $("#message_post_form input[name=messageRequestId]");
        var $messageToId = $("#message_post_form input[name=messageToId]");
        var $messageText = $("#message_post_form textarea[name=messageText]");
		var template = '<div class="row-fluid ge-message"><div class="ge-user-image"><a href=""><img src="" alt="" class="img img-rounded" /></a></div><div class="ge-text"><a class="ge-name" href=""></a><span class="ge-date"></span><span class="ge-block"></span></div></div>', $newTemplate = $(template);
		
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/message',
            dataType: 'json',
            cache: false,
            data: {
                messageRequestId: $messageRequestId.val(),
                messageToId: $messageToId.val(),
                messageText: $messageText.val()
            }
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                
                $(".ge-user-image a", $newTemplate).attr('href', $profile_link);
                $(".ge-user-image img", $newTemplate).attr('src', $img);
                $(".ge-name", $newTemplate).attr('href', $profile_link);
                $(".ge-name", $newTemplate).html($name);
                $(".ge-date", $newTemplate).html('Just now');
                $('.ge-block', $newTemplate).html($messageText.val());
                
                /*
                $messageRequestId.val('');
                $messageToId.val('');
                */
                $messageText.val('');
                
                $('#messages .ge-conversation .span12').append($newTemplate);

                if ( $('#messageContainer').length > 0 ) {
                    $('#messageContainer').modal('hide');
                }
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
</script>
