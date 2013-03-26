<script langauge="javascript">
    $(function() {
        var selectedTab = 1;
        $('#authenticationSlider').find('.tabTitle').each(function(index) {
            if ( $(this).attr('title') && $(this).attr('title') === "<?=$selected_tab?>" ) {
                selectedTab = index + 1;
            }
        });
        
        $('#authenticationSlider').codaSlider({
            continuous: false,
            autoHeight: false,
            dynamicArrows: false,
            hashLinking: false,
            firstPanelToLoad: selectedTab,
            slideEaseDuration: 350,
            slideEaseFunction: "backinout",
            panelTitleSelector: ".tabTitle"
        });
        
        $("#requestInvitation").click(function() {
            $("#requestInvitationForm").slideToggle(350, "backinout");
        });
    });
</script>

<div id="authenticationSlider" class="coda-slider">
    <div>
        <div class="tabTitle" title="invitation"><?=lang('authentication_tab_invitation')?></div>
        <div>
            <?=isset($this->request_invitation_form) ? $this->request_invitation_form->error_string() : ""?>
            <?=form_open('/authentication/invitation/request')?>
            
                <div><input name="invitation_email" value="<?=set_value('invitation_email')?>" type="text" class="textbox" title="<?=lang('authentication_invitation_email_hint')?>" placeholder="<?=lang('authentication_invitation_email_hint')?>"></div>
                <div><input type="submit" value="<?=lang('authentication_invitation_request_button')?>" class="buttonBlue"></div>
                <div><a id="requestInvitation" class="linkOrange"><?=lang('authentication_invitation_invitation')?></a></div>
                
            <?=form_close()?>
            
            
            <div id="requestInvitationForm" class="tabContent" style="display: <?=isset($action) && $action == "verify" ? "block" : "none"?>;">
                <?=isset($this->verify_invitation_form) ? $this->verify_invitation_form->error_string() : ""?>
                <?=form_open('/authentication/invitation/verify')?>
                
                    <div><input name="invitation_code" value="<?=set_value('invitation_code')?>" type="text" class="textbox" title="<?=lang('authentication_invitation_code_hint')?>" placeholder="<?=lang('authentication_invitation_code_hint')?>"></div>
                    <div><input type="submit" value="<?=lang('authentication_invitation_verify_button')?>" class="buttonOrange"></div>
                
                <?=form_close()?>
            </div>
        </div>
    </div>

    <div>
        <div class="tabTitle" title="login"><?=lang('authentication_tab_login')?></div>
        <div>
            <? if ( $is_logged && $user ): ?>
                <?=sprintf(lang('authentication_login_welcome'), $user->name)?>
            <? else: ?>
                <?=isset($this->login_form) ? $this->login_form->error_string() : ""?>
                <?=form_open('/authentication/login')?>
            
                <div><input name="login_email" value="<?=set_value('login_email')?>" type="text" class="textbox" title="<?=lang('authentication_login_email_hint')?>" placeholder="<?=lang('authentication_login_email_hint')?>"></div>
                <div><input name="login_password" type="password" class="textbox" title="<?=lang('authentication_login_password_hint')?>" placeholder="<?=lang('authentication_login_password_hint')?>"></div>
                <div><input type="submit" value="<?=lang('authentication_login_login_button')?>" class="buttonBlue"></div>
                <div class="tabContent">
                    <div class="fl"><a href="#"><?=lang('authentication_login_forgot_password')?></a></div>
                    <div class="fr">
                        <input name="login_remember_me" value="1" <?=set_checkbox('login_remember_me', '1')?> id="remember_me" type="checkbox" class="checkbox">
                        <label for="remember_me"><?=lang('authentication_login_remember_me')?></label>
                    </div>
                </div>
                <div class="clear"></div>
                
                <?=form_close()?>
            <? endif; ?>
        </div>
    </div>
</div>
