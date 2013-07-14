<script language="javascript">
    function request_view(requestId) {
        if ( $('#requestContainer').length > 0 ) {
            $('#requestContainer').modal({
                remote: '<?=base_url()?>request/' + requestId + '?modal&receiving',
                show: true
            });
        }
    }
    
    $(function() {
        $('.ge-request').on('request_canceled', function(event, requestId) {
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
                                        
                    <? if( $ad->sold ): ?>
                                            
                                            <div class="row-fluid ge-text ge-description">
                                                <div class="span12">
                                                    <p class="text-center">
                                                        Inactive (sold out)
                                                    </p>
                                                </div>
                                            </div>
                                            
                    <? elseif( $ad->expired || $request->isExpired() ): ?>
                                            
                                            <div class="row-fluid ge-text ge-description">
                                                <div class="span12">
                                                    <p class="text-center">
                                                        <span class="fui-triangle-down"></span>
                                                        Expired
                                                        <span class="fui-triangle-down"></span>
                                                    </p>
                                                </div>
                                            </div>
                                            
                                            <div class="span12">
                                                <button onclick="request_hide(<?= $request_id ?>);" class="btn btn-small btn-block">
                                                    Delete Gift
                                                    <i class=" fui-trash pull-left"></i>
                                                </button>
                                            </div>
                                            
                    <? elseif( $request->isPending() ): ?>
                                            
                                            <div class="row-fluid ge-text ge-description">
                                                <div class="span12">
                                                    <p class="text-center">
                                                        <span class="fui-triangle-down"></span>
                                                        Pending
                                                        <span class="fui-triangle-down"></span>
                                                    </p>
                                                </div>
                                            </div>
                                            
                                            <div class="span12">
                                                <button onclick="request_cancel(this, <?=$request_id?>);" class="ge-request btn btn-small btn-block">
                                                    Cancel Request
                                                    <i class="fui-cross pull-left"></i>
                                                </button>
                                            </div>
                                            
                    <? elseif( $request->accepted ): ?>
                                            
                                            <div class="row-fluid ge-text ge-description">
                                                <div class="span12">
                                                    <p class="text-center">
                                                        <span class="fui-triangle-down"></span>
                                                        Accepted
                                                        <span class="fui-triangle-down"></span>
                                                    </p>
                                                </div>
                                            </div>
                                            
                                            <div class="row-fluid">
                                                <div class="span4">
                                                    <button onclick="request_view(<?=$request_id?>);" class="btn btn-small btn-block btn-ge">
                                                        <i class="fui-mail"></i>
                                                    </button>
                                                </div>
                                                <div class="span8">
                                                    <button onclick="request_receive(<?=$request_id?>);" class="btn btn-small btn-block btn-ge">
                                                        Mark Received
                                                    </button>
                                                </div>
                                            </div>
                                            
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