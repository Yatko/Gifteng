<?

$CI =& get_instance();
$CI->load->library('usermanagement_service');
$user = $CI->usermanagement_service->loadUser();

?>

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
        var adId = $commentAdId.val();

        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/comment',
            dataType: 'json',
            cache: false,
            data: {
                commentAdId: adId,
                commentText: $commentText.val()
            }
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                
                if ( $('#ad_' + adId + '_comments').length > 0 ) {
                    var $name = "<?=$user->getFullName()?>";
                    var $profile_link = "<?=$user->getProfileUrl()?>";
                    var $img = "<?=$user->getAvatarUrl()?>";
                    var template = '<div class="row-fluid ge-message"><div class="ge-user-image"><a href=""><img src="" alt="" class="img img-rounded" /></a></div><div class="ge-text"><a class="ge-name" href=""></a><span class="ge-date"></span><span class="ge-block"></span></div></div>';
                    var $newTemplate = $(template);
                    
                    $('.ge-user-image a', $newTemplate).attr('href', $profile_link);
                    $('.ge-user-image img', $newTemplate).attr('src', $img);
                    $('.ge-name', $newTemplate).attr('href', $profile_link);
                    $('.ge-name', $newTemplate).html($name);
                    $('.ge-date', $newTemplate).html('Right now');
                    $('.ge-block', $newTemplate).html($commentText.val());

                    $('#ad_' + adId + '_comments .ge-messagelist .span12').prepend($newTemplate);
                }
                
                //$commentAdId.val('');
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
