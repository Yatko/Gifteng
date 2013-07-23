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
    <div class="span7">
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
                                    
                                    <? $this->load->view('element/message', array('message' => $message, 'showTitle' => true, 'showDelete' => true)); ?>
                                    
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
        $ad_title = trim($ad->title);
        $view_link = $ad->getViewUrl();
        ?>
    
        <div class="span5"><!--conversation-->
            <div class="well ge-well">

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

                            <? $this->load->view('element/messages', array('messages' => $request_messages, 'request' => $request, 'to' => $requestor_user, 'canMessage' => true)); ?>

                        </div>
                    </div><!--./ge-messages-->
                </div>

            </div><!--./ge-well-->
        </div><!--end conversation-->
    
    <? endif; ?>
    
</div>

<? endif; ?>