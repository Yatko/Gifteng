<?

/**
 * Input params:
 * 
 * invitation_code: string
 */

?>

<div class="container">
    <div class="row">
        
        <?=form_open('/registration/user', '', array('invitation_code' => $invitation_code))?>
        
        <div id="login_10" class="span4 ge-form ge-topspace">
            <div class="well ge-well">
                <div class="row-fluid ge-text">
                    <div class="row-fluid ge-topspace">
                        <div class="span10 offset1 text-center ge-logo">
                            <img src="<?=BASE_PATH?>temp-sample/gifteng.png" alt="Gifteng" />
                        </div>
                    </div><!--./gifteng-->

                    <div class="row-fluid">
                        <div class="span10 offset1">
                            <p>
                                Let's set up your account.
                            </p>
                        </div>
                    </div>
                    
                    <? if( isset($this->registration_form) && $this->registration_form->hasErrors() ): ?>
                        <div class="row-fluid">
                            <div class="span10 offset1">
                                <p class="error">
                                    <?=$this->registration_form->error_string()?>
                                </p>
                            </div>
                        </div>
                    <? endif; ?>

                    <div class="row-fluid">
                        <div class="span5 offset1">
                            <div class="control-group large">
                                <div class="controls">
                                    <input name="registration_firstname" value="<?=set_value('registration_firstname')?>" type="text" placeholder="<?=lang('registration_firstname_hint')?>" class="span3" required="">
                                </div>
                            </div>
                        </div>
                        <div class="span5">
                            <div class="control-group large">
                                <div class="controls">
                                    <input name="registration_lastname" value="<?=set_value('registration_lastname')?>" type="text" placeholder="<?=lang('registration_lastname_hint')?>" class="span3" required="">
                                </div>
                            </div>
                        </div>
                    </div><!--./name-->

                    <div class="row-fluid">
                        <div class="span10 offset1">
                            <div class="control-group large">
                                <div class="controls">
                                    <input name="registration_email" value="<?=set_value('registration_email')?>" type="email" placeholder="<?=lang('registration_email_hint')?>" class="span3" required="">
                                </div>
                            </div>
                        </div>
                    </div><!--./email-->

                    <div class="row-fluid">
                        <div class="span10 offset1">
                            <div class="control-group large">
                                <div class="controls">
                                    <input name="registration_password" type="password" placeholder="<?=lang('registration_password_hint')?>" class="span3" required="">
                                </div>
                            </div>
                        </div>
                    </div><!--./passowrd-->

                    <div class="row-fluid ge-submit">
                        <div class="span10 offset1">
                            <div class="control-group">
                                <div class="controls">
                                    <button type="submit" class="btn btn-large btn-block btn-ge"><?=lang('registration_join_button')?></button>
                                </div>
                            </div>
                        </div>
                    </div><!--./submit-->

                </div>
                <div class="row-fluid ge-footline">
                    <div class="span12">
                        <p style="text-align:left;"><?=lang('registration_footer')?></p>
                    </div>
                </div>
            </div><!--./ge well-->
        </div><!--./login step10-->
        
        <?=form_close()?>
        
    </div>
</div>