<?

$country_condition = 'United States';
$source_condition = 'other';

if ( isset($invitation_country) && $invitation_country == '' ) {
    //default selection
    $invitation_country = $country_condition;
}

?>

<script langauge="javascript">
    $(function() {
        $("#requestInvitation").click(function() {
            $("#requestInvitationForm").slideToggle(350, "backinout");
        });
        $("#invitation_country").change(function() {
            var selected = $(this).val();
            if ( selected === "<?=$country_condition?>" ) {
                $("#invitationZipcodeUS").show();
            } else {
                $("#invitationZipcodeUS").hide();
            }
        });
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

<div class="container ge-topspace">
    <div class="row">
        
        <div id="invitation_2" class="span4 offset4">
            <div class="well ge-well">
                
    <? if ( $action == "request" ): ?>
        <? if ( $step == 3 ): ?>
            <div class="row-fluid">
                <div class="span12">
                    <a href="<?=base_url()?>index" class="red"><?=lang('invitation_confirmed')?></a>
                </div>
            </div>
            
            <div class="row-fluid">
                <div class="span12">
                    <div style="text-align:center; color: #00bebe !important; padding: 16px;">
                        <?=lang('invitation_confirmed_share')?>
                    </div>
                    <ul class="bottom-icons">
                        <li><a href="http://pinterest.com/gifteng" target="_blank" title="Gifteng on pinterest" class="fui-pinterest"></a></li>
                        <li><a href="http://facebook.com/gifteng" target="_blank" title="Gifteng on facebook" class="fui-facebook"></a></li>
                        <li><a href="http://twitter.com/gifteng" target="_blank" title="Gifteng on twitter" class="fui-twitter"></a></li>
                    </ul>
                </div>
            </div>
        <? elseif ( $step == 2 ): ?>
            
            <?=form_open('/invitation/request/2', '', array('invitation_email' => $invitation_email))?>
            <?=isset($this->request_invitation_form) ? $this->request_invitation_form->error_string() : ""?>
            <input type="hidden" name="invitation_country" value="<?=$invitation_country?>">
            
            <?/**?>
            <div class="row-fluid">
                <div class="span12">
                    <select name="invitation_country" id="invitation_country" class="select-block mbl select-info" data-size="10">
                        <? foreach ( lang('invitation_country_list') as $country ): ?>
                        <option value="<?=$country?>" <?=set_select('invitation_country', $country, $invitation_country == $country)?>><?=$country?></option> 
                        <? endforeach; ?>
                    </select>
                </div>
            </div><!--./country-->
            <?/**/?>

            <div id="invitationZipcodeUS" <?=display($invitation_country, $country_condition)?>>
                <div class="row-fluid">
                    <div class="span12">
                        <div class="control-group large">
                            <div class="controls">
                                <input name="invitation_zipcode" type="text" value="<?=set_value('invitation_zipcode')?>" maxlength="5" placeholder="<?=lang('invitation_zipcode_hint')?>" class="span3">
                            </div>
                        </div>
                    </div>
                </div><!--./zip-->
            </div>

            <div class="row-fluid">
                <div class="span12">
                    <select name="invitation_source" id="invitation_source" class="select-block mbl select-info">
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
                    <div class="span12">
                        <div class="control-group large">
                            <div class="controls">
                                <input name="invitation_source_other" value="<?=set_value('invitation_source_other')?>" type="text" placeholder="<?=lang('invitation_source_other_hint')?>" class="span3">
                            </div>
                        </div>
                    </div>
                </div><!--./source-->
            </div>

            <div class="row-fluid">
                <div id="invitation_usertype" class="span12">
                    <div class="control-group">
                        <label class="control-label" for="checkboxes"><?=lang('invitation_usertype_message')?></label>
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

            <div class="row-fluid ge-submit">
                <div class="span12">
                    <div class="control-group">
                        <div class="controls">
                            <button type="submit" class="btn btn-huge btn-block btn-ge"><?=lang('invitation_confirm_button')?></button>
                        </div>
                    </div>
                </div>
            </div><!--./submit-->
            
            <?=form_close()?>
            
        <? else: ?>
            
            <?=form_open('/invitation/request/1')?>
            <?=isset($this->request_invitation_form) ? $this->request_invitation_form->error_string() : ""?>
            
            <div class="row-fluid">
                <div class="span12">
                    <div class="control-group large">
                        <div class="controls">
                            <input name="invitation_email" value="<?=set_value('invitation_email')?>" type="text" placeholder="<?=lang('invitation_email_hint')?>" class="span3" required="">
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="row-fluid ge-submit">
                <div class="span12">
                    <div class="control-group">
                        <div class="controls">
                            <button type="submit" class="btn btn-huge btn-block btn-ge"><?=lang('invitation_request_button')?></button>
                        </div>
                    </div>
                </div>
            </div><!--./submit-->
            
            <?=form_close()?>
            
        <? endif; ?>
    <? endif; ?>
    
    
    <? if ( $action == "verify" ): ?>
        <? if ( $step == 2): ?>
            
            <?=form_open('/invitation/verify/2', '', array('invitation_code' => $invitation_code))?>
            
            <div class="row-fluid">
                <div class="span12">
                    <?=lang('invitation_verified')?>
                </div>
            </div>
            
            <div class="row-fluid ge-submit">
                <div class="span12">
                    <div class="control-group">
                        <div class="controls">
                            <button type="submit" class="btn btn-huge btn-block btn-ge"><?=lang('invitation_join_button')?></button>
                        </div>
                    </div>
                </div>
            </div><!--./submit-->
            
            <?=form_close()?>
            
        <? else: ?>
            
            <?=form_open('/invitation/verify/1')?>
            <?=isset($this->verify_invitation_form) ? $this->verify_invitation_form->error_string() : ""?>
            
            <div class="row-fluid">
                <div class="span12">
                    <div class="control-group large">
                        <div class="controls">
                            <input name="invitation_code" value="<?=set_value('invitation_code')?>" type="text" placeholder="<?=lang('invitation_code_hint')?>" class="span3" required="">
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="row-fluid ge-submit">
                <div class="span12">
                    <div class="control-group">
                        <div class="controls">
                            <button type="submit" class="btn btn-huge btn-block btn-ge"><?=lang('invitation_verify_button')?></button>
                        </div>
                    </div>
                </div>
            </div><!--./submit-->
            
            <?=form_close()?>
            
        <? endif; ?>
    <? endif; ?>
                
            </div><!--./ge well-->
        </div><!--./ge-invite step2-->

    </div>
</div>