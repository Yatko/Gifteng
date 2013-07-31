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
            
            $form.find(':submit').each(function() {
                var $this = $(this);
                $this.attr('disabled', 'disabled');
            });
            
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
    <div class="modal-body"></div>
</div>
