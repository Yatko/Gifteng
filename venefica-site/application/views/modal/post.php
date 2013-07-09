<script langauge="javascript">
    $(function() {
        $('#postContainer').on('hidden', function() {
            //$(this).data('modal', null);
            $(this).removeData("modal");
        });
    });
</script>

<div id="postContainer" class="modal hide fade" data-remote="<?= base_url() ?>post?modal">
    <div class="modal-body"></div>
</div>
