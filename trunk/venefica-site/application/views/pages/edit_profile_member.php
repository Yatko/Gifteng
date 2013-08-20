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
        /**
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
        /**/
        
        disable_form_buttons_on_submit('edit_profile_form', null);
    });
</script>

<?

$firstName = $currentUser->firstName;
$lastName = $currentUser->lastName;
$about = $currentUser->about;
$zipCode = $currentUser->getZipCode();
$email = $currentUser->email;

if ( $is_modal ) {
    $form_action = '';
} else {
    $form_action = 'edit_profile/member';
}

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
                                                    
                    <div class="ge-modal_header">
                        <div id="edit_profile_ajax_error"><p><?=$message?></p></div>
                    </div>

                    <form <?=($form_action != '' ? 'action="' . base_url() . $form_action . '"' : '')?> method="post" id="edit_profile_form">

                    <fieldset>
                        <div class="row-fluid">
                            <div class="span6 mobile-two">
                                <div class="control-group">
                                    <label class="control-label" for="firstName">Name</label>
                                    <div class="controls">
                                        <input id="firstName" name="firstName" type="text" value="<?=set_value('firstName', $firstName)?>" placeholder="First Name" class="input-block-level" required="">
                                    </div>
                                </div>
                            </div>
                            <div class="span6 mobile-two">
                                <div class="control-group">
                                    <label class="control-label" for="lastName">&nbsp;</label>
                                    <div class="controls">
                                        <input id="lastName" name="lastName" type="text" value="<?=set_value('lastName', $lastName)?>" placeholder="Last Name" class="input-block-level" required="">
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
                                    <label class="control-label" for="zipCode">My Zip Code</label>
                                    <div class="controls">
                                        <input id="zipCode" name="zipCode" type="text" value="<?=set_value('zipCode', $zipCode)?>" maxlength="5" placeholder="10001" class="input-block-level" required="">
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
                                      <label class="control-label" for="password">Old Password</label>
                                      <div class="controls">
                                        <input id="password" name="password" type="password" placeholder="Current Password" class="input-block-level">
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
                                                <input name="new_password_2" type="password" placeholder="Confirm New Password" class="input-block-level">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="ge-modal_footer">
                            <div class="row-fluid">
                                <div class="span12 mobile-two">
                                    <div class="control-group control-form">
                                        <div class="controls">
                                            
                                            <? if( $is_modal ): ?>
                                                <button type="button" data-dismiss="modal" class="span3 btn btn-large">Cancel</button>
                                            <? endif; ?>
                                            <button onclick="submit_form('edit_profile_form');" class="span9 pull-right btn btn-large btn-ge">Save Profile</button>
                                            
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </fieldset>

                    </form>

<? if( !$is_modal ): ?>

                </div>
            </div>
        </div><!--./ge-well-->
    </div>
</div><!--./row-->

<? endif; ?>


<? if( $is_modal ): ?>
<script language="javascript">
    initEditProfileModal();
</script>
<? endif; ?>
