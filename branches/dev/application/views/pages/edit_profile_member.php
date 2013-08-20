<?

/**
 * Input params:
 * 
 * is_modal: boolean
 * currentUser: User_modal
 */

?>

<script langauge="javascript">
    $(function() {
        $('#email').focus(function() {
            $('#email_change_notification').show();
        }).blur(function() {
            $('#email_change_notification').hide();
        });
        $('#password').focus(function() {
            $('#password_change_notification').show();
        }).blur(function() {
            $('#password_change_notification').hide();
        });
        
        
        <? if( $is_modal ): ?>
        $('#edit_profile_form').on('submit', function(e) {
            e.preventDefault();
            
            var $this = $(this);
            
            $.ajax({
                type: "POST",
                url: '<?=base_url()?>edit_profile/ajax/member',
                data: $this.serialize(),
                dataType: 'json'
            }).done(function(response) {
                if ( !response || response === '' ) {
                    //TODO: empty result
                } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                    $('#edit_profile_ajax_error').html(response.<?=AJAX_STATUS_ERROR?>);
                } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                    $('.modal').modal('hide');
                    window.location.reload(true);
                } else {
                    //TODO: unknown response received
                }
            }).fail(function(data) {
                //TODO:
            });
        });
        <? endif; ?>
    });
</script>

<?

$firstName = $currentUser->firstName;
$lastName = $currentUser->lastName;
$about = $currentUser->about;
$zipCode = $currentUser->getZipCode();
$email = $currentUser->email;

?>

<? if( !$is_modal ): ?>


		<div class="row">
			<div class="span8 offset2">
				<div class="well ge-well ge-form">
					<div class="row-fluid">
						<div class="span12">

<? endif; ?>
                        
                    <?
                    $message = isset($this->edit_profile_form) ? $this->edit_profile_form->error_string() : "";
                    if ( $message == '' ) $message = 'Let\'s edit your profile';
                    ?>

                    <?=form_open('/edit_profile/member', array('id' => 'edit_profile_form'))?>

                        <blockquote><!-- removing label tag to fix html validation issues -->
                            <div id="edit_profile_ajax_error"><p><?=$message?></p></div>
                        </blockquote>
                                                    
								<fieldset>
								
									<div class="row-fluid">
										<div class="span6 mobile-two">
											<div class="control-group">
											  <label class="control-label" for="first_name">Name</label>
											  <div class="controls">
											    <input name="firstName" id="first_name" type="text" value="<?=set_value('firstName', $firstName)?>" placeholder="First Name" class="input-block-level" required="">
                                       
											  </div>
											</div>
										</div>
										<div class="span6 mobile-two">
											<div class="control-group">
											  <label class="control-label" for="last_name">&nbsp;</label>
											  <div class="controls">
											    <input id="last_name" name="lastName" type="text" value="<?=set_value('lastName', $lastName)?>" placeholder="Last Name" class="input-block-level" required="">
                                       
											  </div>
											</div>
										</div>
									</div>
									
									<div class="row-fluid">
										<div class="span12">
											<div class="control-group">
												<label class="control-label" for="about">About me</label>
											  	<div class="controls">
											    	<textarea id="about" name="about" rows="2" placeholder="..."><?=set_value('about', $about)?></textarea>
                                        		</div>
											</div>
										</div>
									</div>
									
									<div class="row-fluid">
										<div class="span8">		
											<div class="control-group">
											  <label class="control-label" for="zip">My Zip Code</label>
											  <div class="controls">
											    <input id="zip" name="zipCode" type="text" value="<?=set_value('zipCode', $zipCode)?>" maxlength="5" placeholder="10001" class="input-block-level" required="">
                                        	  </div>
											</div>
										</div>
									</div>
									
									<div class="row-fluid">
										<div class="span12">
											<div class="control-group">
											  <label class="control-label" for="email">My Email <span id="email_change_notification" class="text-danger" style="display: none;">- confirm your change by checking your (old) email address.</span></label>
											  <div class="controls">
											    <input id="email" name="email" type="text" value="<?=set_value('email', $email)?>" placeholder="Email" class="input-block-level" required="" readonly="readonly">
                                        	  </div>
											</div>
										</div>
									</div>	
									
									<div class="row-fluid">
										<div class="span4">
											<div class="control-group">
											  <label class="control-label" for="old_password">Old Password</label>
											  <div class="controls">
											    <input id="old_password" name="password" type="password" placeholder="Current Password" class="input-block-level">
                                        	  </div>
											</div>
										</div>
										<div class="span8">
											<div class="control-group">
											  <label class="control-label" for="new_password">New Password <span id="password_change_notification" class="text-danger" style="display: none;">- please confirm by email.</span></label>
											  <div class="row-fluid">
											  	<div class="span6">
											  		<div class="controls">
											  		  <input id="new_password" name="new_password_1" type="password" placeholder="Password" class="input-block-level">
                                                	</div>
											  	</div>
											  	<div class="span6">
											  		<div class="controls">
											  		  <input id="new_password_1" name="new_password_2" type="password" placeholder="Confirm New Password" class="input-block-level">
                                                	</div>
											  	</div>
											  </div>
											</div>
										</div>									
									</div>
									
                                                                        <? /** ?>
									<div class="row-fluid">
										<div class="edit-profile_facebook">
											<div class="span6">
												<div class="control-group">
												  <label class="control-label" for="facebookButton">Facebook</label>
												  <div class="controls">
												    <button id="facebookButton" class="btn btn-block btn-info"><i class="ge-icon-facebook"></i> Verify now</button>
												  </div>
												</div>
											</div>
											<div class="span6">
												<label class="control-label">&nbsp;</label>
												<div class="controls">
												  <label>We promise not to post anything without
												  your permission.</label>
												</div>
											</div>
										</div>
									</div>
                                                                        <? /**/ ?>
									
									<div class="row-fluid">
                                <? if( $is_modal ): ?>
										<div class="span3 mobile-two">
											<div class="control-group control-form">
											  <div class="controls">
											    <button class="btn btn-large btn-block">Cancel</button>
											  </div>
											</div>
										</div>
                                <? endif; ?>
										<div class="span9 mobile-two">
											<div class="control-group control-form">
											  <div class="controls">
											    <button type="submit" class="btn btn-large btn-block btn-ge">Save Profile</button>
											  </div>
											</div>
										</div>									
									</div>
								
								</fieldset>

                    <?=form_close()?>

<? if( !$is_modal ): ?>

                </div>
            </div>

        </div><!--./ge-well-->
    </div>
</div><!--./row-->

<? endif; ?>
