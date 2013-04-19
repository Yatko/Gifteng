<script langauge="javascript">
    $(function() {
        $("#forgotPassword").click(function() {
            $("#forgotPasswordForm").slideToggle(350, "backinout");
        });
    });
</script>

<div class="form"><div class="container">
    <? if ( $is_logged && $user ): ?>
        <div class="contentBox">
            <?=sprintf(lang('login_welcome'), $user->name)?>
        </div>
    <? else: ?>
        <?=form_open('/authentication/login')?>
        
        <div class="formBox">
            <?=isset($this->login_form) ? $this->login_form->error_string() : ""?>
            <div><input name="login_email" value="<?=set_value('login_email')?>" type="text" class="textbox" title="<?=lang('login_email_hint')?>" placeholder="<?=lang('login_email_hint')?>"></div>
            <div><input name="login_password" type="password" class="textbox" title="<?=lang('login_password_hint')?>" placeholder="<?=lang('login_password_hint')?>"></div>
            <div><input type="submit" value="<?=lang('login_login_button')?>" class="green"></div>
            <div>
                <div class="fl"><a id="forgotPassword" class="link"><?=lang('login_forgot_password')?></a></div>
                <div class="fr">
                    <input name="login_remember_me" value="1" <?=set_checkbox('login_remember_me', '1')?> id="remember_me" type="checkbox" class="checkbox">
                    <label for="remember_me"><?=lang('login_remember_me')?></label>
                </div>
                <div class="clear"></div>
            </div>
        </div>

        <?=form_close()?>


        <div id="forgotPasswordForm" <?=display($action, "forgot")?>>
            <?=form_open('/authentication/forgot')?>

            <div class="formBox">
                <?=isset($this->forgot_password_form) ? $this->forgot_password_form->error_string() : ""?>
                <div><?=lang('forgot_password_message')?></div>
                <div><input name="forgot_password_email" value="<?=set_value('forgot_password_email')?>" type="text" class="textbox" title="<?=lang('forgot_password_email_hint')?>" placeholder="<?=lang('forgot_password_email_hint')?>"></div>
                <div><input type="submit" value="<?=lang('forgot_password_button')?>" class="red"></div>
            </div>

            <?=form_close()?>
        </div>
    <? endif; ?>
</div></div>
