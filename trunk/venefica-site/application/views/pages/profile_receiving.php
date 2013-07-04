<script language="javascript">
    function hideRequest(adId, requestId) {
        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/hide_request?requestId=' + requestId,
            dataType: 'json',
            cache: false
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                $('#ad_' + adId).addClass('hide');
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
</script>

<div class="row">
    <div class="container user-receiving_items">
        <div class="row">
            <div class="ge-tile-view ge-browse">

                
                <? foreach( $receivings as $ad ): ?>
                    <?
                    $request = $ad->getRequestByUser($user->id);
                    $ad_id = $ad->id;
                    $request_id = $request->id;
                    ?>
                    
                    <div id="ad_<?=$ad_id?>">
                    
                    <div class="span3 ge-box">
                        <div class="well ge-well">
                            <div class="row-fluid">
                                <div class="span12">
                                    
                                    <div class="ge-user">
                                        <? $this->load->view('element/user', array('user' => $ad->creator, 'canEdit' => false, 'small' => true)); ?>
                                    </div>

                                    <div class="ge-item">	
                                        <? $this->load->view('element/ad_item', array('ad' => $ad)); ?>
                                        
                                        <div class="row-fluid ge-action">
                                        
                                        <? if( $ad->expired ): ?>
                                            <div class="span4">
                                                <button onclick="hideRequest(<?= $ad_id ?>, <?= $request_id ?>)" class="btn btn-small btn-block fui-trash"></button>
                                            </div>
                                            <div class="span8">
                                                <p class="text-center">GIFT EXPIRED</p>
                                            </div>
                                        <? elseif( $request->isExpired() ): ?>
                                            <div class="span4">
                                                <button onclick="hideRequest(<?= $ad_id ?>, <?= $request_id ?>)" class="btn btn-small btn-block fui-trash"></button>
                                            </div>
                                            <div class="span8">
                                                <p class="text-center">EXPIRED</p>
                                            </div>
                                        <? elseif( $request->isPending() ): ?>
                                            <div class="span4">
                                                <button class="btn btn-small btn-block disabled fui-mail"></button>
                                            </div>
                                            <div class="span8">
                                                <p class="text-center">PENDING</p>
                                            </div>
                                        <? elseif( $request->isSelected() ): ?>
                                            
                                            <? if( $ad->sent ): ?>
                                                <div class="span4">
                                                    <button onclick="startMessage(this, <?= $ad_id ?>)" class="btn btn-small btn-block btn-ge fui-mail"></button>
                                                </div>
                                                <div class="span8">
                                                    <button class="btn btn-small btn-block btn-ge">RECEIVED</button>
                                                </div>
                                            <? else: ?>
                                                <div class="span4">
                                                    <button onclick="startMessage(this, <?= $ad_id ?>)" class="btn btn-small btn-block btn-ge fui-mail"></button>
                                                </div>
                                                <div class="span8">
                                                    <p class="text-center">ACCEPTED</p>
                                                </div>
                                            <? endif; ?>
                                            
                                        <? endif; ?>
                                            
                                        </div><!--./ge-action-->
                                    </div><!--./ge-item-->
                                </div>
                            </div>
                        </div>
                    </div><!--./ge-box-->
                    
                    </div>
                <? endforeach; ?>
                
                
            </div>
        </div>
    </div>
</div>