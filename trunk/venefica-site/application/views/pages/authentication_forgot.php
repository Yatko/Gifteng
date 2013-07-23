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
        
            <div id="login_4" class="span4 ge-form ge-topspace">
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
                                    Success!<br />
                                    You should receive an email shortly.
                                </p>
                            </div>
                        </div>

                        <div class="row-fluid text-center">
                            <div class="span10 offset1">
                                <i class="fui-facebook lead"></i>
                                <i class="fui-twitter lead"></i>
                                <i class="fui-pinterest lead"></i>
                            </div>
                        </div><!--./email-->

                    </div>	
                    <div class="row-fluid ge-footline">
                        <div class="span12">
                            <p style="text-align:left;"><a href="<?=base_url()?>authorization/login">Sign in here &raquo;</a></p>
                        </div>
                    </div>
                </div><!--./ge well-->
            </div><!--./login step4-->
        
        <? else: ?>
            
            <?=form_open('/authentication/forgot')?>
            
            <div id="login_3" class="span4 ge-form ge-topspace">
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
                                    Please enter your email address so we can look up your account:
                                </p>
                            </div>
                        </div>
                        
                        <? if( isset($this->forgot_password_form) && $this->forgot_password_form->hasErrors() ): ?>
                            <div class="row-fluid">
                                <div class="span10 offset1">
                                    <p class="error">
                                        <?=$this->forgot_password_form->error_string()?>
                                    </p>
                                </div>
                            </div>
                        <? endif; ?>
                        
                        <div class="row-fluid">
                            <div class="span10 offset1">
                                <div class="control-group large">
                                    <div class="controls">
                                        <input name="forgot_password_email" value="<?=set_value('forgot_password_email')?>" type="text" placeholder="<?=lang('forgot_password_email_hint')?>" class="span3" required="">
                                    </div>
                                </div>
                            </div>
                        </div><!--./email-->

                        <div class="row-fluid ge-submit">
                            <div class="span10 offset1">
                                <div class="control-group">
                                    <div class="controls">
                                        <button type="submit" class="btn btn-large btn-block btn-ge"><?=lang('forgot_password_button')?></button>
                                    </div>
                                </div>
                            </div>
                        </div><!--./submit-->

                    </div>	
                    <div class="row-fluid ge-footline">
                        <div class="span12">
                            <p style="text-align:left;"><a href="<?=base_url()?>invitation/request">New to Gifteng? Request an invitation now &raquo;</a></p>
                        </div>
                    </div>
                </div><!--./ge well-->
            </div><!--./login step3-->
            
            <?=form_close()?>
            
        <? endif; ?>
        
    </div>
</div>
