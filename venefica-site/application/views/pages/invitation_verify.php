<?

/**
 * Input params:
 * 
 * step: integer
 * invitation_code: string
 */

?>

<div class="container">
    <div class="row">
    
    <? if ( $step == 2): ?>
        
        <?=form_open('/invitation/verify/2', '', array('invitation_code' => $invitation_code))?>
            
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
                                <?=lang('invitation_verified')?>
                            </p>
                        </div>
                    </div>

                    <div class="row-fluid ge-submit">
                        <div class="span10 offset1">
                            <div class="control-group">
                                <div class="controls">
                                    <button type="submit" class="btn btn-large btn-block btn-ge"><?=lang('invitation_join_button')?></button>
                                </div>
                            </div>
                        </div>
                    </div><!--./submit-->
                </div>
                
            </div><!--./ge well-->
        </div><!--./login step4-->
        
        <?=form_close()?>
        
    <? else: ?>
        
        <?=form_open('/invitation/verify/1')?>
        
        <div id="login_6" class="span4 ge-form ge-topspace">
            <div class="well ge-well">
                <div class="row-fluid ge-text">
                    <div class="row-fluid ge-topspace">
                        <div class="span10 offset1 text-center ge-logo">
                            <img src="<?=BASE_PATH?>temp-sample/gifteng.png" alt="Gifteng" />
                        </div>
                    </div><!--./gifteng-->

                    <? if( isset($this->verify_invitation_form) && $this->verify_invitation_form->hasErrors() ): ?>
                        <div class="row-fluid">
                            <div class="span10 offset1">
                                <p class="error">
                                    <?=$this->verify_invitation_form->error_string()?>
                                </p>
                            </div>
                        </div>
                    <? endif; ?>
                    
                    <div class="row-fluid">
                        <div class="span10 offset1">
                            <div class="control-group large">
                                <div class="controls">
                                    <input name="invitation_code" value="<?=set_value('invitation_code')?>" type="text" placeholder="<?=lang('invitation_code_hint')?>" class="span3" required="">
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="row-fluid ge-submit">
                        <div class="span10 offset1">
                            <div class="control-group">
                                <div class="controls">
                                    <button type="submit" class="btn btn-large btn-block btn-ge"><?=lang('invitation_verify_button')?></button>
                                </div>
                            </div>
                        </div>
                    </div><!--./submit-->

                </div>
                <div class="row-fluid ge-footline">
                    <div class="span12">
                        <p style="text-align:left;"><a href="<?=base_url()?>invitation/request">Request an invitation now &raquo;</a></p>
                    </div>
                </div>
            </div><!--./ge well-->
        </div><!--./login step6-->
        
        <?=form_close()?>
        
    <? endif; ?>
    
    </div>
</div>