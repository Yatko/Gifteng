<?

/**
 * Input params:
 * 
 * 
 */

?>

<div class="container">
    <div class="row">
        
        <?=form_open('/authentication/login')?>
        
        <!-- #login2 -->
			<div id="login2" class="span6"><!-- ge-topspace outside of span -->
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
							<? if( isset($this->login_form) && $this->login_form->hasErrors() ): ?>
	                        <div class="row-fluid">
	                            <div class="span8 offset2">
	                                <p class="error">
	                                    <?=$this->login_form->error_string()?>
	                                </p>
	                            </div>
	                        </div>
		                    <? else : ?>
							<div class="row-fluid">
								<div class="span8 offset2">
									<p>
										&nbsp;
									</p>
								</div>
							</div>
		                    <? endif; ?>
					
							<div class="row-fluid">
								<div class="span8 offset2">
									<div class="control-group large">
										<div class="controls">
											<input name="login_email" type="email" value="<?=set_value('login_email')?>" placeholder="<?=lang('login_email_hint')?>" class="span3" required>
										</div>
									</div>
								</div>
							</div><!--./email-->
					
							<div class="row-fluid">
								<div class="span8 offset2">
									<div class="control-group large">
										<div class="controls">
											<input name="login_password" type="password" placeholder="<?=lang('login_password_hint')?>" class="span3" required>
										</div>
									</div>
								</div>
							</div><!--./password-->
					
							<div class="row-fluid">
								<div class="span8 offset2">
									<div class="control-group">
									  <div class="controls">
									    <button type="submit" class="btn btn-large btn-block btn-ge"><?=lang('login_login_button')?></button>
									  </div>
									</div>
								</div>
							</div><!--./submit-->
							
							<div class="row-fluid">
								<div class="span4 offset2 mobile-two">
									<label class="checkbox inline" for="checkbox_remember">
									  <input name="login_remember_me" type="checkbox" value="1" <?=set_checkbox('login_remember_me', '1')?> id="checkbox_remember" data-toggle="checkbox">
									  <?=lang('login_remember_me')?>
									</label>
								</div>
								<div class="span4 mobile-two">
									 <a href="<?=base_url()?>authentication/forgot" class="optional pull-right">Forgot password</a>
								</div>
							</div>
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
			</div><!-- #/login2 -->
        
        <?=form_close()?>
        
    </div>
</div> 
