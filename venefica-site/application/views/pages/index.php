<div class="container"><h2>
    <?=lang('index_welcome')?>
</h2></div>

<div class="form">
    <div class="container">
        <div id="wistia_4b9homs4ka" class="wistia_embed" style="width:400px;height:225px;" data-video-width="400" data-video-height="225">&nbsp;</div>
        <script charset="ISO-8859-1" src="http://fast.wistia.com/static/concat/E-v1.js"></script>
        <script>
            wistiaEmbed = Wistia.embed("4b9homs4ka", {
                version: "v1",
                videoWidth: 400,
                videoHeight: 225,
                volumeControl: true
            });
        </script>
    </div>
</div>

<div class="form"><div class="container containerDefaultSize">
    <div id="form_wrapper" class="form_wrapper">
        <?=form_open('/invitation/request/1', 'id="requestInvitation" class="request active"')?>
        <div>
            <div class="loginBox"><input name="invitation_email" type="text" class="textbox" title="<?=lang('index_invitation_email_hint')?>" placeholder="<?=lang('index_invitation_email_hint')?>"></div>
            <div class="loginBox"><input type="submit" value="<?=lang('index_invitation_request_button')?>" class="green"></div>
        </div>
        <div class="contentBox">
            <div><a rel="verify" class="red linkform"><?=lang('invitation_invitation_invitation')?></a></div>
        </div>
        <?=form_close()?>

        <?=form_open('/invitation/verify/1', 'id="verifyInvitation" class="verify"')?>
        <div>
            <div class="loginBox"><input name="invitation_code" value="" type="text" class="textbox" title="<?=lang('index_invitation_code_hint')?>" placeholder="<?=lang('index_invitation_code_hint')?>"></div>
            <div class="loginBox"><input type="submit" value="<?=lang('index_invitation_verify_button')?>" class="red"></div>
        </div>
        <div class="contentBox">
            <div><a rel="request" class="red linkform"><?=lang('invitation_invitation_request')?></a></div>
        </div>
        <?=form_close()?>
    </div>
</div></div>

<div class="container">
    <h1 class="green"><?=lang('index_slogan')?></h1>
</div>

<div class="container">
    <a href="http://facebook.com/gifteng" target="_blank" title="Gifteng on facebook"><div class="share facebook"></div></a>
    <a href="http://twitter.com/gifteng" target="_blank" title="Gifteng on twitter"><div class="share twitter"></div></a>
    <!-- <a href="#" target="_blank" title=""><div class="share email"></div></a> -->
    <a href="http://linkedin.com/in/gifteng" target="_blank" title="Gifteng on LinkedIn"><div class="share linkedin"></div></a>
</div>