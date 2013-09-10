<?

/**
 * Input params:
 * 
 * ad: Ad_model
 * request: Request_model
 * messages: array of Message_model
 * currentUser: User_model
 * is_modal: boolean
 * userId: long
 */

//$ad_id = $ad->id;
//$request_id = $request->id;
$ad_creator = $ad->creator;
$ad_title = $ad->getSafeTitle();
$view_link = $ad->getViewUrl();

?>


<? if ( $is_modal ): ?>
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <div class="modal-header-content">
            <div class="ge-modal_header">
<? else: ?>
    <div class="span6">
        <div class="well ge-well">
<? endif; ?>


        <div class="row-fluid">
            <div class="ge-user">
                <? $this->load->view('element/user', array('user' => $ad_creator, 'canEdit' => false, 'small' => true)); ?>
            </div><!--./ge-user-->
        </div>
		
<? if( $ad->sold ): ?>
            
    <? /* nothing here */ ?>
            
<? elseif( $request->accepted ): ?>
        
        <? /** ?>
        <div class="row-fluid">
            <div class="ge-action">
                <div class="span6">
                    <button onclick="request_receive(this, <?=$request_id?>, <?=$ad_id?>, <?=$userId?>, null);" type="button" class="btn btn-small btn-block btn-ge">Gift Received</button>
                </div>
            </div>
        </div>
        <? /**/ ?>

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
                        </div><!--./ge-subject-->
                    </div>

                    <? $this->load->view('element/messages', array('messages' => $messages, 'request' => $request, 'to' => $ad_creator, 'canMessage' => true)); ?>
                </div>
            </div><!--./ge-messages-->
        </div>
            
    </div>
</div>

            
<? if( $is_modal ): ?>
<script language="javascript">
    initRequestViewModal();
</script>
<? endif; ?>
