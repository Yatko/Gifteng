<?

/**
 * Input params:
 * 
 * invitation_country: string
 * step: integer
 * invitation_email: string
 * invitation_source: string
 */

$country_condition = 'United States';
$source_condition = 'other';

if ( isset($invitation_country) && $invitation_country == '' ) {
    //default selection
    $invitation_country = $country_condition;
}

?>

<script langauge="javascript">
    $(function() {
        <? /** ?>
        $("#invitation_country").change(function() {
            var selected = $(this).val();
            if ( selected === "<?=$country_condition?>" ) {
                $("#invitationZipcodeUS").show();
            } else {
                $("#invitationZipcodeUS").hide();
            }
        });
        <? /**/ ?>
        $("#invitation_source").change(function() {
            var selected = $(this).val();
            if ( selected === "<?=$source_condition?>" ) {
                $("#invitationSourceOther").show();
            } else {
                $("#invitationSourceOther").hide();
            }
        });
    });
</script>

<div class="container">
    <div class="row">
    
    <? if ( $step == 3 ): ?>
        
        <div class="span6 offset3"><!-- ge-topspace outside of span -->
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
                                <p>Thank you for your request, you should receive an email soon.</p>
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
                                <p class="text-left"><a href="<?=base_url()?>invitation/verify">Do you have an invitation code? Click here &raquo;</a></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
    <? elseif ( $step == 2 ): ?>
        
        <?=form_open('/invitation/request/2', '', array('invitation_email' => $invitation_email, 'invitation_country' => $invitation_country))?>
        
        <div class="span6 offset3"><!-- ge-topspace outside of span -->
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

        <? if( isset($this->request_invitation_form) && $this->request_invitation_form->hasErrors() ): ?>
                        
                        <div class="row-fluid">
                            <div class="span8 offset2">
                                <p class="error">
                                    <?=$this->request_invitation_form->error_string()?>
                                </p>
                            </div>
                        </div>
                        
        <? endif; ?>

                        <? /** ?>
                        <div class="row-fluid">
                            <div class="span10 offset1">
                                <select name="invitation_country" id="invitation_country" class="select-block select-info" data-size="10">
                                    <? foreach ( lang('invitation_country_list') as $country ): ?>
                                    <option value="<?=$country?>" <?=set_select('invitation_country', $country, $invitation_country == $country)?>><?=$country?></option> 
                                    <? endforeach; ?>
                                </select>
                            </div>
                        </div><!--./country-->
                        <? /**/ ?>

                        <div id="invitationZipcodeUS" <?=display($invitation_country, $country_condition)?>>
                            <div class="row-fluid">
                                <div class="span8 offset2">
                                    <div class="control-group large">
                                        <div class="controls">
                                            <input name="invitation_zipcode" type="text" value="<?=set_value('invitation_zipcode')?>" maxlength="5" placeholder="<?=lang('invitation_zipcode_hint')?>" class="span3">
                                        </div>
                                    </div>
                                </div>
                            </div><!--./zip-->
                        </div>

                        <div class="row-fluid">
                            <div class="span8 offset2">
                                <select name="invitation_source" id="invitation_source" class="select-block select-info">
                                    <option value="">How did you hear about us?</option>
                                    
                        <? foreach ( lang('invitation_source_list') as $source ): ?>
                                    <option value="<?=$source?>" <?=set_select('invitation_source', $source)?>><?=$source?></option> 
                        <? endforeach; ?>
                                    
                                    <option value="friend" <?=set_select('invitation_source', 'friend')?>><?=lang('invitation_source_friend')?></option>
                                    <option value="other" <?=set_select('invitation_source', 'other')?>><?=lang('invitation_source_other')?></option>
                                </select>
                            </div>
                        </div><!--./referrer-->

                        <div id="invitationSourceOther" <?=display($invitation_source, $source_condition)?>>
                            <div class="row-fluid">
                                <div class="span8 offset2">
                                    <div class="control-group large">
                                        <div class="controls">
                                            <input name="invitation_source_other" value="<?=set_value('invitation_source_other')?>" type="text" placeholder="<?=lang('invitation_source_other_hint')?>" class="span3">
                                        </div>
                                    </div>
                                </div>
                            </div><!--./other-->
                        </div>

                        <div class="row-fluid">
                            <div id="invitation_usertype" class="span8 offset2">
                                <div class="control-group">
                                    <label class="control-label"><?=lang('invitation_usertype_message')?></label>
                                    <div class="controls">
                                        <label class="radio inline" for="giver_usertype">
                                            <input name="invitation_usertype" id="giver_usertype" value="<?=Invitation_model::USERTYPE_GIVER?>" <?=set_radio('invitation_usertype', Invitation_model::USERTYPE_GIVER)?> type="radio" data-toggle="radio">
                                            <?=lang('invitation_usertype_giver')?>
                                        </label>
                                        <label class="radio inline" for="receiver_usertype">
                                            <input name="invitation_usertype" id="receiver_usertype" value="<?=Invitation_model::USERTYPE_RECEIVER?>" <?=set_radio('invitation_usertype', Invitation_model::USERTYPE_RECEIVER)?> type="radio" data-toggle="radio">
                                            <?=lang('invitation_usertype_receiver')?>
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div><!--./giftenger-->

                        <div class="row-fluid">
                                <div class="span8 offset2">
                                        <div class="control-group">
                                          <div class="controls">
                                            <button type="submit" class="btn btn-large btn-block btn-ge ge-submit"><?=lang('invitation_confirm_button')?> <i class="fui-arrow-right pull-right"></i></button>
                                          </div>
                                        </div>
                                </div>
                        </div><!--./submit-->
                    </div>
                    
                    <div class="ge-footline empty"></div>
                </div>
            </div>		
        </div>
        
        <?=form_close()?>
        
    <? else: ?>
        
        <?=form_open('/invitation/request/1')?>
        
        <div class="span6 offset3"><!-- ge-topspace outside of span -->
            <div class="ge-topspace">
                <div class="well ge-well ge-authbox">
                    <div class="ge-well-content ge-form">
                        <div class="row-fluid">
                            <div class="span8 offset2 text-center">
                                <div class="ge-logo">
                                    <i class="ge-icon-gifteng ge-icon-gifteng-large"></i>
                                </div>
                            </div>
                        </div>
                        <div class="row-fluid">
                            <div class="span8 offset2">
                                <p>Gifteng is a social community where you can give and receive thing you love for free</p>
                            </div>
                        </div>

        <? if( isset($this->request_invitation_form) && $this->request_invitation_form->hasErrors() ): ?>
                                    
                        <div class="row-fluid">
                            <div class="span8 offset2">
                                <p class="error">
                                    <?=$this->request_invitation_form->error_string()?>
                                </p>
                            </div>
                        </div>
                                    
        <? endif; ?>

                        <div class="row-fluid">
                            <div class="span8 offset2">
                                <div class="control-group large">
                                    <div class="controls">
                                        <input name="invitation_email" value="<?=set_value('invitation_email')?>" type="text" placeholder="<?=lang('invitation_email_hint')?>" class="span3" required="">
                                    </div>
                                </div>
                            </div>
                        </div><!--./email-->

                        <div class="row-fluid">
                            <div class="span8 offset2">
                                <div class="control-group">
                                    <div class="controls">
                                        <button type="submit" class="btn btn-large btn-block btn-ge"><?=lang('invitation_request_button')?></button>
                                    </div>
                                </div>
                            </div>
                        </div><!--./submit-->

                        <div class="row-fluid">
                            <div class="span8 offset2">
                                <p><a href="<?=base_url()?>authentication/login">Already a member? Sign in here &raquo;</a></p> 
                            </div>
                        </div>
                    </div>
                    <div class="ge-footline">
                        <div class="row-fluid">
                            <div class="span6 mobile-two">
                                <p class="text-left"><a href="<?=base_url()?>invitation/verify">I have an invitation</a></p>
                            </div>
                            <div class="span6 mobile-two">
                                <p class="text-right"><a href="<?=base_url()?>index">Tell me more</a></p>
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