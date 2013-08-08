<?

/**
 * Input params:
 * 
 * authentication_forgot_password_requested: boolean
 */

?>

<div class="container">
    <div class="row">
        
        <? if ( isset($authentication_forgot_password_requested) && $authentication_forgot_password_requested ): ?>
        
            <div class="span6"><!-- ge-topspace outside of span -->
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
                                                            <p>
                                                                Please check your email to reset your password.
                                                            </p>
                                                    </div>
                                            </div>
                                            
                                            <? /** ?>
                                            <div class="row-fluid text-center">
                                                    <div class="span8 offset2">
                                                            <i class="ge-icon-facebook lead"></i>
                                                            <i class="ge-icon-twitter lead"></i>
                                                            <i class="ge-icon-pinterest lead"></i>
                                                    </div>
                                            </div><!--./social icons-->
                                            <? /**/ ?>

                                    </div>
                                    <div class="ge-footline">
                                            <div class="row-fluid">
                                                    <div class="span12">
                                                            <p class="text-left"><a href="<?=base_url()?>authorization/login">Sign in here &raquo;</a></p>
                                                    </div>
                                            </div>
                                    </div>
                            </div>
                    </div>	
            </div>
        
        <? else: ?>
            
            <?=form_open('/authentication/forgot')?>
            
            <div class="span6"><!-- ge-topspace outside of span -->
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
                                                            <p>
                                                                    Please enter your email address so we can look up your account:
                                                            </p>
                                                    </div>
                                            </div>

            <? if( isset($this->forgot_password_form) && $this->forgot_password_form->hasErrors() ): ?>
                <div class="row-fluid">
                    <div class="span8 offset2">
                        <p class="error">
                            <?=$this->forgot_password_form->error_string()?>
                        </p>
                    </div>
                </div>
            <? endif; ?>

                                            <div class="row-fluid">
                                                    <div class="span8 offset2">
                                                            <div class="control-group large">
                                                                    <div class="controls">
                                                                            <input name="forgot_password_email" type="text" value="<?=set_value('forgot_password_email')?>" placeholder="<?=lang('forgot_password_email_hint')?>" class="span3" required="">
                                                                    </div>
                                                            </div>
                                                    </div>
                                            </div><!--./email-->


                                            <div class="row-fluid">
                                                    <div class="span8 offset2">
                                                            <div class="control-group">
                                                              <div class="controls">
                                                                <button type="submit" class="btn btn-large btn-block btn-ge"><?=lang('forgot_password_button')?></button>
                                                              </div>
                                                            </div>
                                                    </div>
                                            </div><!--./submit-->

                                    </div>
                                    <div class="ge-footline">
                                            <div class="row-fluid">
                                                    <div class="span12">
                                                            <p class="text-left"><a href="<?=base_url()?>invitation/request">New to Gifteng? Request an invitation now &raquo;</a></p>
                                                    </div>
                                            </div>
                                    </div>
                            </div>
                    </div>	
            </div>
            
            <?=form_close()?>
            
        <? endif; ?>
        
    </div>
</div>
