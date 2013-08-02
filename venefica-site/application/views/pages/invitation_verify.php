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
            
        <!-- #login4 -->
			<div id="login4" class="span6"><!-- ge-topspace outside of span -->
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
                                		<?=lang('invitation_verified')?>
									</p>
								</div>
							</div>
					
							<div class="row-fluid">
								<div class="span8 offset2">
									<div class="control-group">
									  <div class="controls">
									    <button type="submit" class="btn btn-large btn-block btn-ge ge-submit"><?=lang('invitation_join_button')?></button>
									  </div>
									</div>
								</div>
							</div><!--./submit-->
						</div>
						<div class="ge-footline empty">
						</div>
					</div>
				</div>	
			</div><!-- #/login4 -->
        
        <?=form_close()?>
        
    <? else: ?>
        
        <?=form_open('/invitation/verify/1')?>
        
        <!-- #login6 -->
			<div id="login6" class="span6"><!-- ge-topspace outside of span -->
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
					

	                    <? if( isset($this->verify_invitation_form) && $this->verify_invitation_form->hasErrors() ): ?>
	                        <div class="row-fluid">
	                            <div class="span8 offset2">
	                                <p class="error">
	                                    <?=$this->verify_invitation_form->error_string()?>
	                                </p>
	                            </div>
	                        </div>
	                    <? endif; ?>
							
							<div class="row-fluid">
								<div class="span8 offset2">
									<div class="control-group large">
										<div class="controls">
											<input type="text" value="<?=set_value('invitation_code')?>" placeholder="<?=lang('invitation_code_hint')?>" class="span3" required>
										</div>
									</div>
								</div>
							</div><!--./invitation code-->
					
							<div class="row-fluid">
								<div class="span8 offset2">
									<div class="control-group">
									  <div class="controls">
									    <button type="submit" class="btn btn-large btn-block btn-ge"><?=lang('invitation_verify_button')?></button>
									  </div>
									</div>
								</div>
							</div><!--./submit-->		
						</div>
						<div class="ge-footline">
							<div class="row-fluid">
								<div class="span12">
					 				<p class="text-left"><a href="<?=base_url()?>invitation/request">Request an invitation now &raquo;</a></p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div><!-- #/login6 -->
        
        <?=form_close()?>
        
    <? endif; ?>
    
    </div>
</div>