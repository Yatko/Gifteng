<script langauge="javascript">
    $(function() {
        $('#avatar').on('file_selected', function() {
            var $this = $(this);
            var $error = $(".avatar_ajax_error");
            
            if ( get_file_size($('#avatar_image').get(0)) > <?=UPLOAD_FILE_MAX_SIZE?> ) {
                if ( !$error.attr('original_text') ) {
                    $error.attr('original_text', $error.html());
                }
                $error.html("<div class='error'>Please limit photo size to 2MB !</div>");
                
                $this.html($this.attr('original_text'));
                return;
            }
            
            $this.html("Please wait...");
            $this.attr('disabled', 'disabled');
            
            $.ajax({
                type: 'POST',
                url: '<?= base_url() ?>profile/ajax/change_avatar',
                dataType: 'json',
                cache: false,
                data: new FormData($("#avatar_post_form").get(0)),
                processData: false,
                contentType: false
            }).done(function(response) {
                if (!response || response === '') {
                    //TODO: empty result
                } else if (response.hasOwnProperty('<?= AJAX_STATUS_ERROR ?>')) {
                    //TODO
                } else if (response.hasOwnProperty('<?= AJAX_STATUS_RESULT ?>')) {
                    $('#avatarImage').attr('src', response.<?= AJAX_STATUS_RESULT ?>);
                } else {
                    //TODO: unknown response received
                }

                $error.html($error.attr('original_text'));
                
                $this.html($this.attr('original_text'));
                $this.removeClass('btn-ge');
                $this.removeAttr('disabled');
                
                $("#avatarContainer").modal('hide');
            }).fail(function(data) {
                //TODO
            });
        });
        $('#avatarContainer').on('hidden', function() {
            var $error = $(".avatar_ajax_error");
            if ( $error.attr('original_text') ) {
                $error.html($error.attr('original_text'));
            }
        });
    });
</script>

<div id="avatarContainer" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-header">
        <label class="control-label" for="fieldset">
            <blockquote>
                <div class="avatar_ajax_error">Choose your profile picture</div>
            </blockquote>
        </label>
    </div>
    <div class="modal-body">

    <?= form_open_multipart('/profile/ajax/change_avatar', array('id' => 'avatar_post_form')) ?>

        <button id="avatar" for="avatar_image" type="button" class="btn btn-huge btn-block file">
            Upload photo
            <i class="fui-photo pull-right"></i>
        </button>
        <input type="file" name="avatar_image" id="avatar_image" />

    <?= form_close() ?>

    </div>
</div>
