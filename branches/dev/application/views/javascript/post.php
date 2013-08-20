<script langauge="javascript">
    function edit_post() {
        $('input[name=next_step]').val('start');
        $('#member_post_form').submit();
    }
    function another_post() {
        /**
        $('input[name=next_step]').val('start');
        $('#member_post_form').submit();
        /**/
        
        $('#postContainer').modal('hide');
        $('#postContainer').removeData("modal").modal('show');
    }
</script>
