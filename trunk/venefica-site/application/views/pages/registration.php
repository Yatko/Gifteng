<?=isset($this->registration_form) ? $this->registration_form->error_string() : ""?>
<?=form_open('/registration', '', array('invitation_code' => $invitation_code))?>

<div class="content"><div>
    <div>
        <div class="fl"><input name="registration_firstname" value="<?=set_value('registration_firstname')?>" type="text" class="textbox" title="<?=lang('registration_firstname_hint')?>" placeholder="<?=lang('registration_firstname_hint')?>"></div>
        <div class="fr"><input name="registration_lastname" value="<?=set_value('registration_lastname')?>" type="text" class="textbox" title="<?=lang('registration_lastname_hint')?>" placeholder="<?=lang('registration_lastname_hint')?>"></div>
        <div class="clear"></div>
    </div>
    <div><input name="registration_email" value="<?=set_value('registration_email')?>" type="text" class="textbox" title="<?=lang('registration_email_hint')?>" placeholder="<?=lang('registration_email_hint')?>"></div>
    <div><input name="registration_password" type="password" class="textbox" title="<?=lang('registration_password_hint')?>" placeholder="<?=lang('registration_password_hint')?>"></div>
    <div><input type="submit" value="<?=lang('registration_join_button')?>" class="buttonBlue"></div>
    <div><?=lang('registration_footer')?></div>
</div></div>

<?=form_close()?>