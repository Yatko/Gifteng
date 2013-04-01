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
        $("#forgotPassword").click(function() {
            $("#forgotPasswordForm").slideToggle(350, "backinout");
        });
    });
</script>

<div id="authenticationSlider" class="coda-slider">
    
    <!-- invitation tab -->
    
    <div>
        <div class="tabTitle" title="invitation"><?=lang('authentication_tab_invitation')?></div>
        <div>
            <? if ( !isset($step) || !$step || $step == 1 ): ?>
                <?=isset($this->request_invitation_form) ? $this->request_invitation_form->error_string() : ""?>
                <?=form_open('/authentication/invitation/request/1')?>

                <div><input name="invitation_email" value="<?=set_value('invitation_email')?>" type="text" class="textbox" title="<?=lang('authentication_invitation_email_hint')?>" placeholder="<?=lang('authentication_invitation_email_hint')?>"></div>
                <div><input type="submit" value="<?=lang('authentication_invitation_request_button')?>" class="buttonBlue"></div>

                <?=form_close()?>
            <? elseif ( $step == 2 ): ?>
                <?=isset($this->request_invitation_form) ? $this->request_invitation_form->error_string() : ""?>
                <?=form_open('/authentication/invitation/request/2', '', array('invitation_email' => $invitation_email))?>
                
                <div><input name="invitation_zipcode" value="<?=set_value('invitation_zipcode')?>" type="text" class="textbox" title="<?=lang('authentication_invitation_zipcode_hint')?>" placeholder="<?=lang('authentication_invitation_zipcode_hint')?>"></div>
                <div class="tabContent">
                    <select data-placeholder="<?=lang('authentication_invitation_source_hint')?>" name="invitation_source" class="chzn-select">
                        <option value=""></option>
                        <option value="Google" <?=set_select('invitation_source', 'Google')?>><?=lang('authentication_invitation_source_google')?></option>
                        <option value="Facebook" <?=set_select('invitation_source', 'Facebook')?>><?=lang('authentication_invitation_source_facebook')?></option>
                        <option value="Twitter" <?=set_select('invitation_source', 'Twitter')?>><?=lang('authentication_invitation_source_twitter')?></option>
                        <option value="friend" <?=set_select('invitation_source', 'friend')?>><?=lang('authentication_invitation_source_friend')?></option>
                        <option value="other" <?=set_select('invitation_source', 'other')?>><?=lang('authentication_invitation_source_other')?></option>
                    </select>
                </div>
                <div class="tabContent"><?=lang('authentication_invitation_usertype_message')?></div>
                <div class="tabContent">
                    <input type="radio" name="invitation_usertype" id="giver_usertype" value="GIVER" <?=set_radio('invitation_usertype', 'GIVER', TRUE)?> class="radionInline" />
                    <label for="giver_usertype"><?=lang('authentication_invitation_usertype_giver')?></label>
                    <input type="radio" name="invitation_usertype" id="receiver_usertype" value="RECEIVER" <?=set_radio('invitation_usertype', 'RECEIVER')?> class="radionInline" />
                    <label for="receiver_usertype"><?=lang('authentication_invitation_usertype_receiver')?></label>
                </div>
                <div class="clear"></div>
                <div><input type="submit" value="<?=lang('authentication_invitation_confirm_button')?>" class="buttonBlue"></div>

                <?=form_close()?>
            <? elseif ( $step == 3 ): ?>
                <div class="tabContent">
                    <div class="textOrange"><?=lang('authentication_invitation_confirmed')?></div>
                </div>
            <? endif; ?>
            
                
            <div><a id="requestInvitation" class="textOrange"><?=lang('authentication_invitation_invitation')?></a></div>
            
            
            <div id="requestInvitationForm" <?=display($action, "verify")?>>
                <?=isset($this->verify_invitation_form) ? $this->verify_invitation_form->error_string() : ""?>
                <?=form_open('/authentication/invitation/verify')?>
                
                <div><input name="invitation_code" value="<?=set_value('invitation_code')?>" type="text" class="textbox" title="<?=lang('authentication_invitation_code_hint')?>" placeholder="<?=lang('authentication_invitation_code_hint')?>"></div>
                <div><input type="submit" value="<?=lang('authentication_invitation_verify_button')?>" class="buttonOrange"></div>
                
                <?=form_close()?>
            </div>
        </div>
    </div>

    <!-- login tab -->
    
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
                    <div class="fl"><a id="forgotPassword" class="link"><?=lang('authentication_login_forgot_password')?></a></div>
                    <div class="fr">
                        <input name="login_remember_me" value="1" <?=set_checkbox('login_remember_me', '1')?> id="remember_me" type="checkbox" class="checkbox">
                        <label for="remember_me"><?=lang('authentication_login_remember_me')?></label>
                    </div>
                    <div class="clear"></div>
                </div>
                
                <?=form_close()?>
                
                
                <div id="forgotPasswordForm" <?=display($action, "forgot")?>>
                    <?=isset($this->forgot_password_form) ? $this->forgot_password_form->error_string() : ""?>
                    <?=form_open('/authentication/login/forgot')?>
                    
                    <div class="tabContent"><?=lang('authentication_forgot_password_message')?></div>
                    <div><input name="forgot_password_email" value="<?=set_value('forgot_password_email')?>" type="text" class="textbox" title="<?=lang('authentication_forgot_password_email_hint')?>" placeholder="<?=lang('authentication_forgot_password_email_hint')?>"></div>
                    <div><input type="submit" value="<?=lang('authentication_forgot_password_button')?>" class="buttonBlue"></div>
                    
                    <?=form_close()?>
                </div>
            <? endif; ?>
        </div>
    </div>
</div>
