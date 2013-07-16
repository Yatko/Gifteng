<script langauge="javascript">
    $(function() {
        $('#requestContainer').on('hidden', function() {
            $(this).removeData("modal");
            $('#requestContainer > .modal-body').html('');
        });
    });
</script>

<div id="requestContainer" class="modal hide fade">
    <div class="modal-body"></div>
</div>
