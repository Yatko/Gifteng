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
    
    function shareOnFacebookModal() {
        shareOnFacebook(socialTitle, socialItemUrl, socialImgUrl);
        $('#socialContainer').modal('hide');
    }
    function shareOnTwitterModal() {
        shareOnTwitter(socialTitle, socialItemUrl, socialImgUrl);
        $('#socialContainer').modal('hide');
    }
    function shareOnPinterestModal() {
        shareOnPinterest(socialTitle, socialItemUrl, socialImgUrl);
        $('#socialContainer').modal('hide');
    }
</script>

<div id="socialContainer" class="modal hide fade">
    <div class="modal-body">
        <div class="row-fluid">
            <div class="span4">
                <button onclick="shareOnFacebookModal();" class="btn btn-mini btn-block btn-social-facebook fui-facebook"></button>
            </div>
            <div class="span4">
                <button onclick="shareOnTwitterModal();" class="btn btn-mini btn-block btn-social-twitter fui-twitter"></button>
            </div>
            <div class="span4">
                <button onclick="shareOnPinterestModal();" class="btn btn-mini btn-block btn-social-pinterest fui-pinterest"></button>
            </div>
        </div>
    </div>
</div>
