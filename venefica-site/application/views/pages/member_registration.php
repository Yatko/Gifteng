<div class="container ge-topspace">
    <div class="row user-register">
        
        <div class="span4 offset4">
            <div class="well ge-well">
                
                <div class="row-fluid">
                    <div class="span12">

                        <?=form_open('/registration/user', '', array('invitation_code' => $invitation_code))?>
                        <?=isset($this->registration_form) ? $this->registration_form->error_string() : ""?>

                            <label class="control-label" for="fieldset">
                                <blockquote><p>Let's set up your account</p></blockquote>
                            </label>

                            <fieldset>

                                <div class="row-fluid">
                                    <div class="span6">
                                        <div class="control-group">
                                            <div class="controls">
                                                <input name="registration_firstname" value="<?=set_value('registration_firstname')?>" type="text" placeholder="<?=lang('registration_firstname_hint')?>" class="input-block-level" required="">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="span6">
                                        <div class="control-group">
                                            <div class="controls">
                                                <input name="registration_lastname" value="<?=set_value('registration_lastname')?>" type="text" placeholder="<?=lang('registration_lastname_hint')?>" class="input-block-level" required="">
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="control-group">
                                    <div class="controls">
                                        <input name="registration_email" value="<?=set_value('registration_email')?>" type="email" placeholder="<?=lang('registration_email_hint')?>" class="input-block-level" required="">
                                    </div>
                                </div>

                                <div class="control-group">
                                    <div class="controls">
                                        <input name="registration_password" type="password" placeholder="<?=lang('registration_password_hint')?>" class="input-block-level" required="">
                                    </div>
                                </div>

                                <div class="control-group">
                                    <div class="controls">
                                        <button type=""submit class="btn btn-large btn-ge btn-block ge-submit"><?=lang('registration_join_button')?></button>
                                    </div>
                                </div>

                                <div><?=lang('registration_footer')?></div>

                            </fieldset>

                        <?=form_close()?>

                    </div>
                </div>
                
            </div><!--./ge-well-->
        </div>
        
    </div><!--./row-->
</div><!--./container-->


<? /** ?>
<?=isset($this->registration_form) ? $this->registration_form->error_string() : ""?>
<?=form_open('/registration/user', '', array('invitation_code' => $invitation_code))?>

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
<? /**/ ?>