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
        $("#invitation_country").chosen().change(function() {
            var selected = $(this).val();
            if ( selected === "<?=$country_condition?>" ) {
                $("#invitationZipcodeUS").show();
            } else {
                $("#invitationZipcodeUS").hide();
            }
        });
        $("#invitation_source").chosen().change(function() {
            var selected = $(this).val();
            if ( selected === "<?=$source_condition?>" ) {
                $("#invitationSourceOther").show();
            } else {
                $("#invitationSourceOther").hide();
            }
        });
    });
</script>

<div class="form"><div class="container containerDefaultSize">
    
    <? if ( $action == "request" ): ?>
        <? if ( $step == 3 ): ?>
            <div class="contentBox">
                <div class="textOrange">
                    <a href="<?=base_url()?>index" class="red"><?=lang('invitation_confirmed')?></a>
                </div>
            </div>
        <? elseif ( $step == 2 ): ?>
            <div>
                <?=form_open('/invitation/request/2', '', array('invitation_email' => $invitation_email))?>
                <?=isset($this->request_invitation_form) ? $this->request_invitation_form->error_string() : ""?>
                <div class="loginBox">
                    <select data-placeholder="<?=lang('invitation_country_hint')?>" name="invitation_country" id="invitation_country" class="chzn-select">
                        <option value=""></option>
                        <? foreach ( lang('invitation_country_list') as $country ): ?>
                        <option value="<?=$country?>" <?=set_select('invitation_country', $country, $invitation_country == $country)?>><?=$country?></option> 
                        <? endforeach; ?>
                    </select>
                </div>

                <div id="invitationZipcodeUS" class="loginBox" <?=display($invitation_country, $country_condition)?>>
                    <input name="invitation_zipcode" value="<?=set_value('invitation_zipcode')?>" type="text" class="textbox" title="<?=lang('invitation_zipcode_hint')?>" placeholder="<?=lang('invitation_zipcode_hint')?>">
                </div>

                <div class="loginBox">
                    <select data-placeholder="<?=lang('invitation_source_hint')?>" name="invitation_source" id="invitation_source" class="chzn-select">
                        <option value=""></option>
                        <option value="Google" <?=set_select('invitation_source', 'Google')?>><?=lang('invitation_source_google')?></option>
                        <option value="Facebook" <?=set_select('invitation_source', 'Facebook')?>><?=lang('invitation_source_facebook')?></option>
                        <option value="Twitter" <?=set_select('invitation_source', 'Twitter')?>><?=lang('invitation_source_twitter')?></option>
                        <option value="Indiegogo" <?=set_select('invitation_source', 'Indiegogo')?>><?=lang('invitation_source_indiegogo')?></option>
                        <option value="friend" <?=set_select('invitation_source', 'friend')?>><?=lang('invitation_source_friend')?></option>
                        <option value="other" <?=set_select('invitation_source', 'other')?>><?=lang('invitation_source_other')?></option>
                    </select>
                </div>
                <div id="invitationSourceOther" class="loginBox" <?=display($invitation_source, $source_condition)?>>
                    <input name="invitation_source_other" value="<?=set_value('invitation_source_other')?>" type="text" class="textbox" title="<?=lang('invitation_source_other_hint')?>" placeholder="<?=lang('invitation_source_other_hint')?>">
                </div>
                <div class="label" style="text-align:left;margin:6px;font-weight:200;"><?=lang('invitation_usertype_message')?></div>
                <div class="label" style="text-align:center;margin:6px 6px 32px 6px;">
                    <input type="radio" name="invitation_usertype" id="giver_usertype" value="GIVER" <?=set_radio('invitation_usertype', 'GIVER')?> class="radionInline" />
                    <label for="giver_usertype"><?=lang('invitation_usertype_giver')?></label>
                    <input type="radio" name="invitation_usertype" id="receiver_usertype" value="RECEIVER" <?=set_radio('invitation_usertype', 'RECEIVER')?> class="radionInline" />
                    <label for="receiver_usertype"><?=lang('invitation_usertype_receiver')?></label>
                </div>
                <div class="clear"></div>
                <div class="loginBox"><input type="submit" value="<?=lang('invitation_confirm_button')?>" class="green"></div>
                <?=form_close()?>
            </div>
        <? else: ?>
            <div>
                <?=form_open('/invitation/request/1')?>
                <?=isset($this->request_invitation_form) ? $this->request_invitation_form->error_string() : ""?>
                <div><input name="invitation_email" value="<?=set_value('invitation_email')?>" type="text" class="textbox" title="<?=lang('invitation_email_hint')?>" placeholder="<?=lang('invitation_email_hint')?>"></div>
                <div><input type="submit" value="<?=lang('invitation_request_button')?>" class="green"></div>
                <?=form_close()?>
            </div>
        <? endif; ?>
    <? endif; ?>
    
    
    <? if ( $action == "verify" ): ?>
        <? if ( $step == 2): ?>
            <div>
                <?=form_open('/invitation/verify/2', '', array('invitation_code' => $invitation_code))?>
                <div class="red"><?=lang('invitation_verified')?></div>
                <div class="loginBox"><input type="submit" value="<?=lang('invitation_join_button')?>" class="green"></div>
                <?=form_close()?>
            </div>
        <? else: ?>
            <div>
                <?=form_open('/invitation/verify/1')?>
                <?=isset($this->verify_invitation_form) ? $this->verify_invitation_form->error_string() : ""?>
                <div class="loginBox"><input name="invitation_code" value="<?=set_value('invitation_code')?>" type="text" class="textbox" title="<?=lang('invitation_code_hint')?>" placeholder="<?=lang('invitation_code_hint')?>"></div>
                <div class="loginBox"><input type="submit" value="<?=lang('invitation_verify_button')?>" class="red"></div>
                <?=form_close()?>
            </div>
        <? endif; ?>
    <? endif; ?>
    
    
    <? /** ?>
    <? if ( $action == "verify" && $step == 2): ?>
        <!-- -->
    <? else: ?>
        <div class="contentBox">
            <div><a id="requestInvitation" class="red"><?=lang('invitation_invitation')?></a></div>
        </div>
    <? endif; ?>

    <div id="requestInvitationForm" <?=display($action, "verify")?>>

        <? if ( $action == "verify" && $step == 2): ?>
            <?=form_open('/invitation/verify/2', '', array('invitation_code' => $invitation_code))?>
            
            <div class="formBox">
                <div class="red"><?=lang('invitation_verified')?></div>
                <div><input type="submit" value="<?=lang('invitation_join_button')?>" class="green"></div>
            </div>

            <?=form_close()?>
        <? else: ?>
            <?=form_open('/invitation/verify/1')?>
            
            <div class="formBox">
                <?=isset($this->verify_invitation_form) ? $this->verify_invitation_form->error_string() : ""?>
                <div><input name="invitation_code" value="<?=set_value('invitation_code')?>" type="text" class="textbox" title="<?=lang('invitation_code_hint')?>" placeholder="<?=lang('invitation_code_hint')?>"></div>
                <div><input type="submit" value="<?=lang('invitation_verify_button')?>" class="red"></div>
            </div>

            <?=form_close()?>
        <? endif; ?>

    </div>
    <? /**/ ?>
    
</div></div>
