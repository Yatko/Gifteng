<script langauge="javascript">
    $(function() {
        $("#loginSlider").codaSlider();
    });
</script>

<div class="stripViewer">
    <div id="loginSliderWrap"><div id="loginSlider">
        <div class="innerWrap"><div class="panelContainer">
            <div class="panel" title="<?=lang('authentication_tab_invitation')?>">
                <div class="wrapper">
                    <form>
                        <div><input type="text" class="textbox" title="<?=lang('authentication_invitation_email_hint')?>" placeholder="<?=lang('authentication_invitation_email_hint')?>"></div>
                        <div><a href="#" class="buttonBlue"><?=lang('authentication_invitation_request_button')?></a></div>
                    </form>
                    <div><a href="#" class="linkOrange"><?=lang('authentication_invitation_invitation')?></a></div>
                </div>
            </div>

            <div class="panel" title="<?=lang('authentication_tab_login')?>">
                <div class="wrapper">
                    <form>
                        <div><input type="text" class="textbox" title="<?=lang('authentication_login_email_hint')?>" placeholder="<?=lang('authentication_login_email_hint')?>"></div>
                        <div><input type="password" class="textbox" title="<?=lang('authentication_login_password_hint')?>" placeholder="<?=lang('authentication_login_password_hint')?>"></div>
                        <div><a href="#" class="buttonBlue"><?=lang('authentication_login_login_button')?></a></div>
                        <div class="tabContent">
                            <div class="fl"><a href="#"><?=lang('authentication_login_forgot_password')?></a></div>
                            <div class="fr">
                                <input id="remember_me" type="checkbox" class="checkbox">
                                <label for="remember_me"><?=lang('authentication_login_remember_me')?></label>
                            </div>
                        </div>
                        <div class="clear"></div>
                    </form>
                </div>
            </div>
        </div></div>
    </div></div>
</div>
