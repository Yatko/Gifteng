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
                        <div></div>
                    </form>
                </div>
            </div>

            <div class="panel" title="<?=lang('authentication_tab_login')?>">
                <div class="wrapper">
                    <form>
                        <div><input type="text" title="<?=lang('authentication_login_email_hint')?>" placeholder="<?=lang('authentication_login_email_hint')?>"></div>
                        <div><input type="password" title="<?=lang('authentication_login_password_hint')?>" placeholder="<?=lang('authentication_login_password_hint')?>"></div>
                        <div><a href="#" class="buttonBlue"><?=lang('authentication_login_submit_button')?></a></div>
                    </form>
                </div>
            </div>
        </div></div>
    </div></div>
</div>
