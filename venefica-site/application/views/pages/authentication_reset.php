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
        
        <!-- #login7 -->
			<div id="login7" class="span6"><!-- ge-topspace outside of span -->
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
										Enter your new password:
									</p>
								</div>
							</div>
							
	                    <? if( isset($this->reset_password_form) && $this->reset_password_form->hasErrors() ): ?>
	                        <div class="row-fluid">
	                            <div class="span8 offset1">
	                                <p class="error">
	                                    <?=$this->reset_password_form->error_string()?>
	                                </p>
	                            </div>
	                        </div>
	                    <? endif; ?>
					
							<div class="row-fluid">
								<div class="span8 offset2">
									<div class="control-group large">
										<div class="controls">
											<input name="reset_password_p_1" type="password" placeholder="<?=lang('reset_password_p_1_hint')?>" class="span3" required>
										</div>
									</div>
								</div>
							</div><!--./email-->
					
							<div class="row-fluid">
								<div class="span8 offset2">
									<div class="control-group large">
										<div class="controls">
											<input name="reset_password_p_2" type="password" placeholder="<?=lang('reset_password_p_2_hint')?>" class="span3" required>
										</div>
									</div>
								</div>
							</div><!--./password-->
					
							<div class="row-fluid">
								<div class="span8 offset2">
									<div class="control-group">
									  <div class="controls">
									    <button type="submit" class="btn btn-large btn-block btn-ge"><?=lang('reset_password_button')?></button>
									  </div>
									</div>
								</div>
							</div><!--./submit-->
							
						</div>
						<div class="ge-footline empty">
						</div>
					</div>
				</div>
			</div><!-- #/login7 -->
        
        <?=form_close()?>
        
    </div>
</div>
