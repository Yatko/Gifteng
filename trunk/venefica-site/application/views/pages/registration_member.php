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
        
        <div class="span6 offset3"><!-- ge-topspace outside of span -->
            <div class="ge-topspace">
                <div class="well ge-well ge-authbox">
                    <div class="ge-well-content ge-form">
                        <div class="row-fluid">
                            <div class="span8 offset2">
                                <div class="ge-logo">
                                    <i class="ge-icon-gifteng ge-icon-gifteng-large"></i>
                                </div>
                            </div>
                        </div>
                        
                        <div class="row-fluid">
                            <div class="span8 offset2">
                                <p>Let's set up your account:</p>
                            </div>
                        </div>
        
        <? if( isset($this->registration_form) && $this->registration_form->hasErrors() ): ?>
        
                        <div class="row-fluid">
                            <div class="span8 offset2">
                                <p class="error">
                                    <?=$this->registration_form->error_string()?>
                                </p>
                            </div>
                        </div>
        
        <? endif; ?>

                        <div class="row-fluid">
                            <div class="span4 offset2 mobile-two">
                                <div class="control-group large">
                                    <div class="controls">
                                        <input name="registration_firstname" value="<?=set_value('registration_firstname')?>" type="text" placeholder="<?=lang('registration_firstname_hint')?>" class="span3" required="">
                                    </div>
                                </div>
                            </div>
                            <div class="span4 mobile-two">
                                <div class="control-group large">
                                    <div class="controls">
                                        <input name="registration_lastname" value="<?=set_value('registration_lastname')?>" type="text" placeholder="<?=lang('registration_lastname_hint')?>" class="span3" required="">
                                    </div>
                                </div>
                            </div>
                        </div><!--./name-->

                        <div class="row-fluid">
                            <div class="span8 offset2">
                                <div class="control-group large">
                                    <div class="controls">
                                        <input name="registration_email" value="<?=set_value('registration_email')?>" type="email" placeholder="<?=lang('registration_email_hint')?>" class="span3" required="">
                                    </div>
                                </div>
                            </div>
                        </div><!--./email-->

                        <div class="row-fluid">
                            <div class="span8 offset2">
                                <div class="control-group large">
                                    <div class="controls">
                                        <input name="registration_password" type="password" placeholder="<?=lang('registration_password_hint')?>" class="span3" required="">
                                    </div>
                                </div>
                            </div>
                        </div><!--./passowrd-->

                        <div class="row-fluid">
                            <div class="span8 offset2">
                                <div class="control-group">
                                    <div class="controls">
                                        <button type="submit" class="btn btn-large btn-block btn-ge"><?=lang('registration_join_button')?></button>
                                    </div>
                                </div>
                            </div>
                        </div><!--./submit-->
                    </div>
                    
                    <div class="ge-footline">
                        <div class="row-fluid">
                            <div class="span12">
                                <p class="text-left"><?=lang('registration_footer')?></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>	
        </div>
        
        <?=form_close()?>
        
    </div>
</div>
