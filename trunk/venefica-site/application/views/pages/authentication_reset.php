<?

/**
 * Input params:
 * 
 * reset_password_code: string
 */

?>

<div class="container">
    <div class="row">
        
        <?=form_open('/authentication/reset', '', array('reset_password_code' => $reset_password_code))?>
        
        <div id="login_7" class="span4 ge-form ge-topspace">
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
                                Set up your new password.
                            </p>
                        </div>
                    </div>
                    
                    <? if( isset($this->reset_password_form) && $this->reset_password_form->hasErrors() ): ?>
                        <div class="row-fluid">
                            <div class="span10 offset1">
                                <p class="error">
                                    <?=$this->reset_password_form->error_string()?>
                                </p>
                            </div>
                        </div>
                    <? endif; ?>

                    <div class="row-fluid">
                        <div class="span10 offset1">
                            <div class="control-group large">
                                <div class="controls">
                                    <input name="reset_password_p_1" type="password" placeholder="<?=lang('reset_password_p_1_hint')?>" class="span3" required="">
                                </div>
                            </div>
                        </div>
                    </div><!--./passowrd-->

                    <div class="row-fluid">
                        <div class="span10 offset1">
                            <div class="control-group large">
                                <div class="controls">
                                    <input name="reset_password_p_2" type="password" placeholder="<?=lang('reset_password_p_2_hint')?>" class="span3" required="">
                                </div>
                            </div>
                        </div>
                    </div><!--./password-->

                    <div class="row-fluid ge-submit">
                        <div class="span10 offset1">
                            <div class="control-group">
                                <div class="controls">
                                    <button type="submit" class="btn btn-large btn-block btn-ge"><?=lang('reset_password_button')?></button>
                                </div>
                            </div>
                        </div>
                    </div><!--./submit-->

                </div>	
                <div class="row-fluid ge-footline" style="background-color: transparent;"></div>
            </div><!--./ge well-->
        </div><!--./login step7-->
        
        <?=form_close()?>
        
    </div>
</div>
