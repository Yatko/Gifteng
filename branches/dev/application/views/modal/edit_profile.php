<script langauge="javascript">
    $(function() {
        $('#editProfileContainer').on('hidden', function() {
            //$(this).data('modal', null);
            $(this).removeData("modal");
            $('#editProfileContainer > .modal-body').html('');
        });
    });
</script>

<div id="editProfileContainer" class="modal hide fade" data-remote="<?= base_url() ?>edit_profile?modal">
    <div class="modal-body"></div>
</div>
