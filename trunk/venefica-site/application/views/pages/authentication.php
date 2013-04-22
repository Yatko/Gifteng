<?

$default_action = 'login';
if ( $action == '' ) {
    $action = $default_action;
}

?>

<div class="form"><div class="container containerDefaultSize">
    <? if ( $is_logged && $user ): ?>
        <div class="contentBox">
            <?=sprintf(lang('login_welcome'), $user->name)?>
        </div>
    <? else: ?>
        <div id="form_wrapper" class="form_wrapper">
            <?=form_open('/authentication/login', 'id="login" class="login ' . ($action == 'login' ? 'active' : '') . '"')?>
            <?=isset($this->login_form) ? $this->login_form->error_string() : ""?>
            <div class="loginBox"><input name="login_email" value="<?=set_value('login_email')?>" type="text" class="textbox" title="<?=lang('login_email_hint')?>" placeholder="<?=lang('login_email_hint')?>"></div>
            <div class="loginBox"><input name="login_password" type="password" class="textbox" title="<?=lang('login_password_hint')?>" placeholder="<?=lang('login_password_hint')?>"></div>
            <div class="loginBox"><input type="submit" value="<?=lang('login_login_button')?>" class="green"></div>
            <div>
                <div id="forgtPassword" class="fl"><a rel="forgot" class="link linkform"><?=lang('login_forgot_password')?></a></div>
                <div id="rememberMe" class="fr">
                    <input name="login_remember_me" value="1" <?=set_checkbox('login_remember_me', '1')?> id="remember_me" type="checkbox" class="checkbox">
                    <label for="remember_me"><?=lang('login_remember_me')?></label>
                </div>
                <div class="clear"></div>
            </div>
            <?=form_close()?>
            
            <?=form_open('/authentication/forgot', 'id="forgot" class="forgot ' . ($action == 'forgot' ? 'active' : '') . '"')?>
            <?=isset($this->forgot_password_form) ? $this->forgot_password_form->error_string() : ""?>
            <div class="label"><?=lang('forgot_password_message')?></div>
            <div class="loginBox"><input name="forgot_password_email" value="<?=set_value('forgot_password_email')?>" type="text" class="textbox" title="<?=lang('forgot_password_email_hint')?>" placeholder="<?=lang('forgot_password_email_hint')?>"></div>
            <div class="loginBox"><input type="submit" value="<?=lang('forgot_password_button')?>" class="red"></div>
            <?=form_close()?>
        </div>
    <? endif; ?>
</div></div>
