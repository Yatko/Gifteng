<script language="javascript">
    function startEditPostModal(adId) {
        var $adId = $("#edit_post_form input[name=adId]");
        $adId.val(adId);
        
        if ( $('#editPostContainer').length > 0 ) {
            $('#editPostContainer').removeData("modal").modal('show');
        }
    }
    
    function edit_post_modal() {
        if ( $("#edit_post_form").length === 0 ) {
            return;
        }
        
        var $adId = $("#edit_post_form input[name=adId]");
        window.location.href = "<?=base_url()?>post/edit/" + $adId.val();
    }
</script>

<div id="editPostContainer" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <div class="modal-header-content">
        	<div class="ge-modal_header">
        		<label class="control-label">
        			<h3>Sorry. We need to check your gift again before it goes back to the site. Are you sure you need to edit it?</h3>
        		</label>
        	</div>
        </div>
    </div>
    <div class="modal-footer">
        <div class="ge-modal_footer">
        <?=form_open('/ajax/post', array('id' => 'edit_post_form'))?>
            <input type="hidden" name="adId"/>
            <fieldset>
                <div class="row-fluid">
                    <div class="span12">
                        <div class="control-group control-form">
                            <div class="controls">
                                <button type="button" onclick="edit_post_modal();" class="span4 btn">Edit</button>
                                <button type="button" data-dismiss="modal" class="span8 btn btn-ge">Don't Edit</button>
                            </div>
                        </div>
                    </div>
                </div><!--./submit-->
            </fieldset>
            
        <?=form_close()?>
        </div>
    </div>
</div>
