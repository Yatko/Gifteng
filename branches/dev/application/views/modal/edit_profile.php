<script langauge="javascript">
    function startEditProfileModal() {
        $('#editProfileContainer').removeData("modal").modal('show');
    }
    function initEditProfileModal() {
        $('#edit_profile_form').on('submit', function(e) {
            e.preventDefault();
            
            var $this = $(this);
            
            $.ajax({
                type: "POST",
                url: '<?=base_url()?>edit_profile/ajax/member',
                data: $this.serialize(),
                dataType: 'json'
            }).done(function(response) {
                if ( !response || response === '' ) {
                    //TODO: empty result
                } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                    $('#edit_profile_ajax_error').html(response.<?=AJAX_STATUS_ERROR?>);
                    enable_buttons('#editProfileContainer > .modal-footer button');
                } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                    if ( $('#editProfileContainer').length > 0 ) {
                        $('#editProfileContainer').modal('hide');
                    }
                    window.location.reload(true);
                } else {
                    //TODO: unknown response received
                }
            }).fail(function(data) {
                //TODO:
            });
        });
        
        $('#editProfileContainer .modal-header-content').empty();
        if ( $('#editProfileContainer > .modal-body .ge-modal_header').length > 0 ) {
            var $header = $('#editProfileContainer > .modal-body .ge-modal_header').clone();
            $('#editProfileContainer > .modal-body .ge-modal_header').remove();
            $('#editProfileContainer .modal-header-content').append($header);
        }
        
        $('#editProfileContainer > .modal-footer').empty();
        if ( $('#editProfileContainer > .modal-body .ge-modal_footer').length > 0 ) {
            var $footer = $('#editProfileContainer > .modal-body .ge-modal_footer').clone();
            $('#editProfileContainer > .modal-body .ge-modal_footer').remove();
            $('#editProfileContainer > .modal-footer').append($footer);
        }
    }
    
    function resend_verification(callerElement) {
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/resend_verification',
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
                    $element.addClass('hide');
                }
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
</script>

<div id="editProfileContainer" class="modal hide fade" data-remote="<?= base_url() ?>edit_profile?modal">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <div class="modal-header-content"></div>
    </div>
    <div class="modal-body"></div>
    <div class="modal-footer"></div>
</div>
