<?

/**
 * Input params:
 * 
 * messages: array of Message_model
 * request: Request_model
 * ad: Ad_model
 * request_messages: array of Message_model
 */

?>

<? if( $messages != null && count($messages) > 0 ): ?>

<div class="row">
    <div class="span7 <?= ( $request != null ) ? 'hidden-phone' : ''?>">
        <div class="well ge-well">

            <div class="row-fluid">
                <div class="ge-messages">
                    <div class="span12">

                        <div class="row-fluid">
                            <div class="ge-subject">
                                <a class="ge-title">Messages</a>
                            </div><!--./ge-subject-->
                        </div>

                        <div class="row-fluid">
                            <div class="ge-conversation">
                                <div class="span12">
                                
                                <? foreach ($messages as $message): ?>
                                    
                                    <? $this->load->view('element/message', array('message' => $message, 'showTitle' => true, 'showDelete' => true, 'showProfileLinks' => false)); ?>
                                    
                                <? endforeach; ?>

                                </div>
                            </div><!--./ge-conversation-->
                        </div>

                    </div>
                </div><!--./ge-messages-->
            </div>

        </div><!--./ge-well-->
    </div><!--end messagelist-->

    <? if( $request != null ): ?>
    
        <? 
        $requestor_user = $request->user;
        $ad_title = $ad->getSafeTitle();
        $view_link = $ad->getViewUrl();
        $to_user = $ad->owner ? $requestor_user : $ad->creator;
        ?>
        
        
        <div class="span5"><!--conversation-->
            <div class="well ge-well">

                <a href="<?=base_url()?>profile?message" class="visible-phone close pull-right" style="margin:10px;">X</a>
                
                <div class="row-fluid">
                    <div class="ge-user">

                        <? $this->load->view('element/user', array('user' => $requestor_user, 'canEdit' => false, 'small' => true)); ?>

                    </div><!--./ge-user-->
                </div>

                <div class="row-fluid">
                    <div class="ge-messages">
                        <div class="span12">

                            <div class="row-fluid">
                                <div class="ge-subject">
                                    <a href="<?=$view_link?>" class="ge-title"><?=$ad_title?></a>
                                </div><!--./ge-subject-->
                            </div>

                            <? $this->load->view('element/messages', array('messages' => $request_messages, 'request' => $request, 'to' => $to_user, 'canMessage' => true, 'showProfileLinks' => true)); ?>

                        </div>
                    </div><!--./ge-messages-->
                </div>

            </div><!--./ge-well-->
        </div><!--end conversation-->
    
    <? endif; ?>
    
</div>

<? else: ?>

<div class="row">
    <div class="span12">
        <div class="text-center">
            You have no messages.
        </div>
    </div>
</div>

<? endif; ?>
