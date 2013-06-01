<?

$default_action = 'login';
if ( $action == '' ) {
    $action = $default_action;
}

if ( !isset($authentication_forgot_password_requested) ) {
    $authentication_forgot_password_requested = false;
}

?>

<div class="form"><div class="container containerDefaultSize">
    <? if ( $isLogged && $user ): ?>
        <div class="contentBox">
            <? if ( $user->businessAccount ): ?>
                <?=sprintf(lang('login_welcome'), $user->businessName)?>
            <? else: ?>
                <?=sprintf(lang('login_welcome'), $user->name)?>
            <? endif; ?>
        </div>
    <? elseif ( $action == "reset" ): ?>
        <div>
            <?=form_open('/authentication/reset', '', array('reset_password_code' => $reset_password_code))?>
            <?=isset($this->reset_password_form) ? $this->reset_password_form->error_string() : ""?>
            <div class="loginBox"><input name="reset_password_p_1" type="password" class="textbox" title="<?=lang('reset_password_p_1_hint')?>" placeholder="<?=lang('reset_password_p_1_hint')?>"></div>
            <div class="loginBox"><input name="reset_password_p_2" type="password" class="textbox" title="<?=lang('reset_password_p_2_hint')?>" placeholder="<?=lang('reset_password_p_2_hint')?>"></div>
            <div class="loginBox"><input type="submit" value="<?=lang('reset_password_button')?>" class="green"></div>
            <?=form_close()?>
        </div>
    <? else: ?>
        <div id="form_wrapper" class="form_wrapper">
            <?=form_open('/authentication/login', 'id="login" class="login ' . ($action == 'login' ? 'active' : '') . '"')?>
            <?=isset($this->login_form) ? $this->login_form->error_string() : ""?>
            <div class="loginBox"><input name="login_email" value="<?=set_value('login_email')?>" type="text" class="textbox" title="<?=lang('login_email_hint')?>" placeholder="<?=lang('login_email_hint')?>"></div>
            <div class="loginBox"><input name="login_password" type="password" class="textbox" title="<?=lang('login_password_hint')?>" placeholder="<?=lang('login_password_hint')?>"></div>
            <div class="loginBox"><input type="submit" value="<?=lang('login_login_button')?>" class="green"></div>
            <div>
                <div id="forgotPassword" class="fl"><a rel="forgot" class="link linkform"><?=lang('login_forgot_password')?></a></div>
                <div id="rememberMe" class="fr">
                    <input name="login_remember_me" value="1" <?=set_checkbox('login_remember_me', '1')?> id="remember_me" type="checkbox" class="checkbox">
                    <label for="remember_me"><?=lang('login_remember_me')?></label>
                </div>
                <div class="clear"></div>
            </div>
            <?=form_close()?>
            
            <? if ( $authentication_forgot_password_requested ): ?>
                <div class="label"><?=lang('forgot_password_requested_message')?></div>
            <? else: ?>
                <?=form_open('/authentication/forgot', 'id="forgot" class="forgot ' . ($action == 'forgot' ? 'active' : '') . '"')?>
                <?=isset($this->forgot_password_form) ? $this->forgot_password_form->error_string() : ""?>
                <div class="label"><?=lang('forgot_password_message')?></div>
                <div class="loginBox"><input name="forgot_password_email" value="<?=set_value('forgot_password_email')?>" type="text" class="textbox" title="<?=lang('forgot_password_email_hint')?>" placeholder="<?=lang('forgot_password_email_hint')?>"></div>
                <div class="loginBox"><input type="submit" value="<?=lang('forgot_password_button')?>" class="red"></div>
                <?=form_close()?>
            <? endif; ?>
        </div>
    <? endif; ?>
</div></div>
