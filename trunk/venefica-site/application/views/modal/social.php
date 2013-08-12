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
    <div class="modal-body">
        <div class="row-fluid">
            <div class="span4">
                <button onclick="share_on_facebook_modal();" class="btn btn-mini btn-block btn-social-facebook ge-icon-facebook"></button>
            </div>
            <div class="span4">
                <button onclick="share_on_twitter_modal();" class="btn btn-mini btn-block btn-social-twitter ge-icon-twitter"></button>
            </div>
            <div class="span4">
                <button onclick="share_on_pinterest_modal();" class="btn btn-mini btn-block btn-social-pinterest ge-icon-pinterest"></button>
            </div>
        </div>
    </div>
</div>
