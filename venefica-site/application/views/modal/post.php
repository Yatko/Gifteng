<script langauge="javascript">
    function startPostModal() {
        $('#postContainer').removeData("modal").modal('show');
    }
    function initPostModal() {
        init_select();
        init_checkbox();
        init_files();
        
        $('#member_post_form').on('submit', function(e) {
            e.preventDefault();
            
            var $form = $("#member_post_form");
            var formData = new FormData($form.get(0));

            $.ajax({
                type: "POST",
                url: '<?=base_url()?>post/member?modal',
                dataType: 'html',
                cache: false,
                data: formData,
                processData: false,
                contentType: false
            }).done(function(response) {
                $('#postContainer > .modal-body').html(response);
            }).fail(function(data) {
                //TODO
            });
        });
        
        $('#postContainer .modal-header-content').empty();
        if ( $('#postContainer > .modal-body .ge-modal_header').length > 0 ) {
            var $header = $('#postContainer > .modal-body .ge-modal_header').clone();
            $('#postContainer > .modal-body .ge-modal_header').remove();
            $('#postContainer .modal-header-content').append($header);
        }
        
        $('#postContainer > .modal-footer').empty();
        if ( $('#postContainer > .modal-body .ge-modal_footer').length > 0 ) {
            var $footer = $('#postContainer > .modal-body .ge-modal_footer').clone();
            $('#postContainer > .modal-body .ge-modal_footer').remove();
            $('#postContainer > .modal-footer').append($footer);
        }
    }
    
    $(function() {
        $('#postContainer').on('hide', function() {
            var $unique_id = $("#member_post_form input[name=unique_id]");
            
            $.ajax({
                type: "POST",
                url: '<?=base_url()?>post/member/ajax/remove',
                dataType: 'json',
                cache: false,
                data: {
                    unique_id: $unique_id.val()
                }
            }).done(function(response) {
                //
            }).fail(function(data) {
                //TODO
            });
        });
    });
</script>

<div id="postContainer" class="modal hide fade" data-remote="<?=base_url()?>post?modal" data-backdrop="static" data-keyboard="false">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <div class="modal-header-content"></div>
    </div>
    <div class="modal-body"></div>
    <div class="modal-footer"></div>
</div>
