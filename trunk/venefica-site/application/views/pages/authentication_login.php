<div class="container">
    <div class="row">
        
        <?=form_open('/authentication/login')?>
        
        <div id="login_2" class="span4 ge-form ge-topspace">
            <div class="well ge-well">
                <div class="row-fluid ge-text">
                    <div class="row-fluid ge-topspace">
                        <div class="span10 offset1 text-center ge-logo">
                            <img src="<?=BASE_PATH?>temp-sample/gifteng.png" alt="Gifteng" />
                        </div>
                    </div><!--./gifteng-->

                    <? if( isset($this->login_form) && $this->login_form->hasErrors() ): ?>
                        <div class="row-fluid">
                            <div class="span10 offset1">
                                <p class="error">
                                    <?=$this->login_form->error_string()?>
                                </p>
                            </div>
                        </div>
                    <? endif; ?>
                    
                    <div class="row-fluid">
                        <div class="span10 offset1">
                            <div class="control-group large">
                                <div class="controls">
                                    <input name="login_email" value="<?=set_value('login_email')?>" type="text" placeholder="<?=lang('login_email_hint')?>" class="span3" required="">
                                </div>
                            </div>
                        </div>
                    </div><!--./email-->

                    <div class="row-fluid">
                        <div class="span10 offset1">
                            <div class="control-group large">
                                <div class="controls">
                                    <input name="login_password" type="password" placeholder="<?=lang('login_password_hint')?>" class="span3" required="">
                                </div>
                            </div>
                        </div>
                    </div><!--./passowrd-->

                    <div class="row-fluid ge-submit">
                        <div class="span10 offset1">
                            <div class="control-group">
                                <div class="controls">
                                    <button type="submit" class="btn btn-large btn-block btn-ge"><?=lang('login_login_button')?></button>
                                </div>
                            </div>
                        </div>
                    </div><!--./submit-->

                    <div class="row-fluid">
                        <div class="span5 offset1">
                            <label class="checkbox inline" for="remember_me">
                                <input name="login_remember_me" value="1" <?=set_checkbox('login_remember_me', '1')?> id="remember_me" type="checkbox" data-toggle="checkbox">
                                <?=lang('login_remember_me')?>
                            </label>
                        </div>
                        <div class="span5">
                            <a href="<?=base_url()?>authentication/forgot" class="optional pull-right">Forgot password</a>
                        </div>
                    </div>
                </div>	
                <div class="row-fluid ge-footline">
                    <div class="span12">
                        <p style="text-align:left;"><a href="<?=base_url()?>invitation/request">New to Gifteng? Request an invitation now &raquo;</a></p>
                    </div>
                </div>
            </div><!--./ge well-->
        </div><!--./login step2-->
        
        <?=form_close()?>
        
    </div>
</div> 
