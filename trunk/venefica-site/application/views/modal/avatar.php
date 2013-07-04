<script langauge="javascript">
    $(function() {
        $('#avatar').on('file_selected', function() {
            var $this = $(this);
            var formData = new FormData($("#avatar_post_form").get(0));

            $.ajax({
                type: 'POST',
                url: '<?= base_url() ?>profile/ajax/change_avatar',
                dataType: 'json',
                cache: false,
                data: formData,
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

                $this.text($this.attr('original_text'));
                $("#avatarContainer").modal('hide');
            }).fail(function(data) {
                //TODO
            });
        });
    });
</script>

<div id="avatarContainer" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-header">
        <label class="control-label" for="fieldset">
            <blockquote>
                <p>
                    Choose your new avatar image
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                </p>
            </blockquote>
        </label>
    </div>
    <div class="modal-body">

    <?= form_open_multipart('/profile/ajax/change_avatar', array('id' => 'avatar_post_form')) ?>

        <button id="avatar" for="image" type="button" class="btn btn-huge btn-block file">
            Upload a great photo
            <i class="fui-photo pull-right"></i>
        </button>
        <input type="file" name="image" id="image" />

    <?= form_close() ?>

    </div>
</div>
