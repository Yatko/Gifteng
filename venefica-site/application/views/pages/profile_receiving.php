<script language="javascript">
    $(function() {
        $('.ge-request').on('request_cancelled', function(event, requestId) {
            $('#request_' + requestId).addClass('hide');
        });
    });
</script>

<div class="row">
    <div class="container user-receiving_items">
        <div class="row">
            <div class="ge-tile-view ge-browse">

                <? if( isset($receivings) && is_array($receivings) && count($receivings) > 0 ): ?>
                <? foreach( $receivings as $ad ): ?>
                    <?
                    $request = $ad->getRequestByUser($user->id);
                    $ad_id = $ad->id;
                    $to_id = $ad->creator->id;
                    $request_id = $request->id;
                    ?>
                    
                    <div id="request_<?=$request_id?>">
                    
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
                                                <button onclick="request_hide(<?= $request_id ?>);" class="btn btn-small btn-block fui-trash"></button>
                                            </div>
                                            <div class="span8">
                                                <p class="text-center">GIFT EXPIRED</p>
                                            </div>
                                        <? elseif( $request->isExpired() ): ?>
                                            <div class="span4">
                                                <button onclick="request_hide(<?= $request_id ?>);" class="btn btn-small btn-block fui-trash"></button>
                                            </div>
                                            <div class="span8">
                                                <p class="text-center">EXPIRED</p>
                                            </div>
                                        <? elseif( $request->isPending() ): ?>
                                            <div class="span4">
                                                <button class="btn btn-small btn-block disabled fui-mail"></button>
                                            </div>
                                            <div class="span4">
                                                <button onclick="request_cancel(this, <?=$request_id?>);" class="ge-request btn btn-small btn-block fui-cross"></button>
                                            </div>
                                            <div class="span4">
                                                <p class="text-center">PENDING</p>
                                            </div>
                                        <? elseif( $request->isSelected() ): ?>
                                            
                                            <? if( $ad->sent ): ?>
                                                <div class="span4">
                                                    <button onclick="startMessage(this, <?= $ad_id ?>, <?= $to_id ?>);" class="btn btn-small btn-block btn-ge fui-mail"></button>
                                                </div>
                                                <div class="span8">
                                                    <button class="btn btn-small btn-block btn-ge">RECEIVED</button>
                                                </div>
                                            <? else: ?>
                                                <div class="span4">
                                                    <button onclick="startMessage(this, <?= $ad_id ?>, <?= $to_id ?>);" class="btn btn-small btn-block btn-ge fui-mail"></button>
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
                <? else: ?>
                
                    <img src="<?=BASE_PATH?>temp-sample/ge-no-gift.png" class="img img-rounded">
                
                <? endif; ?>
                
            </div>
        </div>
    </div>
</div>