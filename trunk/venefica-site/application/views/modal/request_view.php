<script langauge="javascript">
    $(function() {
        $('#requestContainer').on('hidden', function() {
            //$(this).data('modal', null);
            $(this).removeData("modal");
        });
    });
</script>

<div id="requestContainer" class="modal hide fade">
    <div class="modal-body"></div>
</div>
