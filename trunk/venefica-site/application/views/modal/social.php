<script language="javascript">
    var socialTitle;
    var socialItemUrl;
    var socialImgUrl;
    
    function startSocialModal(title, itemUrl, imgUrl) {
        socialTitle = title;
        socialItemUrl = itemUrl;
        socialImgUrl = imgUrl;
        
        $('#socialContainer').removeData("modal").modal('show');
    }
    
    function share_on_facebook_modal() {
        shareOnFacebook(socialTitle, socialItemUrl, socialImgUrl);
        $('#socialContainer').modal('hide');
    }
    function share_on_twitter_modal() {
        shareOnTwitter(socialTitle, socialItemUrl, socialImgUrl);
        $('#socialContainer').modal('hide');
    }
    function share_on_pinterest_modal() {
        shareOnPinterest(socialTitle, socialItemUrl, socialImgUrl);
        $('#socialContainer').modal('hide');
    }
</script>

<div id="socialContainer" class="modal hide fade">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <div class="modal-header-content">
        	<div class="ge-modal_header">
        		<label class="control-label">
        			<h3>Sharing is caring... Select your favorite social network!</h3>
        		</label>
        	</div>
        </div>
	</div>
    <div class="modal-body">
    	<div class="well ge-well ge-form">
	        <div class="row-fluid">
	            <div class="span4">
	                <button onclick="share_on_facebook_modal();" class="btn btn-mini btn-block btn-social-facebook fui-facebook"></button>
	            </div>
	            <div class="span4">
	                <button onclick="share_on_twitter_modal();" class="btn btn-mini btn-block btn-social-twitter fui-twitter"></button>
	            </div>
	            <div class="span4">
	                <button onclick="share_on_pinterest_modal();" class="btn btn-mini btn-block btn-social-pinterest fui-pinterest"></button>
	            </div>
	        </div>
        </div>
    </div>
</div>
