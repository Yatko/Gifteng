<?

$request_id = $request->id;
$requestor_user = $request->user;
$ad_title = trim($ad->title);

?>

<div class="span6">
    <div class="well ge-well">

        <div class="row-fluid">
            <div class="ge-user">
                <? $this->load->view('element/user', array('user' => $requestor_user, 'canEdit' => false, 'small' => true)); ?>
            </div>
        </div>
        
        
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
                <div class="ge-action">
                    <div class="span6">
                        <button onclick="request_cancel(this, <?=$request_id?>);" type="button" class="btn btn-small btn-block">Decline Request</button>
                    </div>
                    <div class="span6">
                        <button onclick="request_send(<?=$request_id?>);" type="button" class="btn btn-small btn-block btn-ge">Shipped/Handed Over</button>
                    </div>
                </div><!--./ge-action-->
            </div>
        
        <? else: ?>
        
            <div class="row-fluid">
                <div class="ge-action">
                    <div class="span6">
                        <button onclick="request_cancel(this, <?=$request_id?>);" type="button" class="btn btn-small btn-block">Decline Request</button>
                    </div>
                    <div class="span6">
                        <button onclick="request_select(this, <?=$request_id?>);" type="button" class="btn btn-small btn-block btn-ge">Accept Request</button>
                    </div>
                </div><!--./ge-action-->
            </div>
        
        <? endif; ?>

        
        
        <div class="ge-messages">
            <div class="row-fluid">
                <div class="span12">

                    <div class="row-fluid">
                        <div class="ge-subject">
                            <a class="ge-title"><?=$ad_title?></a>
                        </div>
                    </div><!--./ge-subject-->
                    
                    <? $this->load->view('element/messages', array('messages' => $messages, 'ad' => $ad, 'to' => $requestor_user, 'canMessage' => true)); ?>
                    
                </div>
            </div>
        </div><!--./ge-messages-->

    </div><!--./ge-well-->
</div>
