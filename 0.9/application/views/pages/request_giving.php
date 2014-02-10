<?

/**
 * Input params:
 * 
 * ad: Ad_model
 * request: Request_model
 * userId: long
 * messages: array of Message_model
 * currentUser: User_model
 * is_modal: boolean
 */

$ad_id = $ad->id;
$request_id = $request->id;
$user_id = $userId;
$requestor_user = $request->user;
$ad_title = $ad->getSafeTitle();
$view_link = $ad->getViewUrl();

?>

<? if ( !$is_modal ): ?>
<div class="span6">
    <div class="well ge-well">
<? else: ?>
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <div class="modal-header-content">
            <div class="ge-modal_header">
<? endif; ?>

        <div class="ge-user">
            <? $this->load->view('element/user', array('user' => $requestor_user, 'canEdit' => false, 'small' => true, 'size' => LIST_USER_IMAGE_SIZE)); ?>
        </div><!--./ge-user-->

<? if( $request->sent ): ?>

        <? /** ?>
        <div class="row-fluid">
            <div class="ge-action">
                <div class="span6">
                    <button type="button" class="btn btn-small btn-block">Remove Gift</button>
                </div>
            </div><!--./ge-action-->
        </div>
        <? /**/ ?>
    
<? elseif( $request->accepted ): ?>
        
        <div class="row-fluid">
            <div class=" ge-action">
                <div class="span6 mobile-two">
                    <button onclick="request_cancel(this, 'giving', <?=$request_id?>, <?=$ad_id?>, <?=$user_id?>, null);" type="button" class="ge-request btn btn-small btn-block">Decline Request</button>
                </div>
                <div class="span6 mobile-two">
                    <button onclick="request_send(this, <?=$request_id?>, <?=$ad_id?>, <?=$user_id?>);" type="button" class="btn btn-small btn-block btn-ge">Shipped / Handed Over</button>
                </div>
            </div><!--./ge-action-->
        </div>

<? else: ?>
        
        <div class="row-fluid">
            <div class=" ge-action">
                <div class="span6 mobile-two">
                    <button onclick="request_cancel(this, 'giving', <?=$request_id?>, <?=$ad_id?>, <?=$user_id?>, null);" type="button" class="ge-request btn btn-small btn-block">Decline Request</button>
                </div>
                <div class="span6 mobile-two">
                    <button onclick="request_select(this, <?=$request_id?>, <?=$ad_id?>, <?=$user_id?>);" type="button" class="btn btn-small btn-block btn-ge">Accept Request</button>
                </div>
            </div><!--./ge-action-->
        </div>
        
<? endif; ?>

<? if ( $is_modal ): ?>
            </div>
        </div>
    </div>
    
    <div class="modal-body">
    	<div class="well ge-well">
<? endif; ?>

        <div class="ge-messages">
            <div class="row-fluid">
                <div class="span12">
                    <div class="row-fluid">
                        <div class="ge-subject">
                            <a href="<?=$view_link?>" class="ge-title"><?=$ad_title?></a>
                        </div>
                    </div><!--./ge-subject-->

                    <? $this->load->view('element/messages', array('messages' => $messages, 'requestId' => $request_id, 'to' => $requestor_user, 'canMessage' => true, 'showProfileLinks' => true, 'currentUser' => $currentUser)); ?>
                </div>
            </div>
        </div><!--./ge-messages-->

<? if ( $is_modal ): ?>
        </div>
    </div>
<? else: ?>
    </div>
</div>
<? endif; ?>
        
    

<? if( $is_modal ): ?>
<script language="javascript">
    initRequestViewModal();
</script>
<? endif; ?>
