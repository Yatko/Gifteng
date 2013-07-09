<script language="javascript">
    function request_view(requestId) {
        if ( $('#requestContainer').length > 0 ) {
            $('#requestContainer').modal({
                remote: '<?=base_url()?>request/' + requestId + '?modal',
                show: true
            });
        }
    }
</script>



<div class="row">			
    <div class="container user-giving_items">
        <div class="row">
            <div class="ge-tile-view ge-browse">

            <? if( isset($givings) && is_array($givings) && count($givings) > 0 ): ?>
            <? foreach( $givings as $ad ): ?>
                
                <div class="span3 ge-box">
                    <div class="well ge-well">
                        <div class="row-fluid">
                            <div class="span12">
                                <div class="ge-item">
                                    
                                    <? $this->load->view('element/ad_item', array('ad' => $ad)); ?>
                                    
                                    <? if( isOwner($user) ): ?>
                                    <? if( $ad->expired ): ?>

                                        <div class="row-fluid ge-text ge-description">
                                            <div class="span6">
                                                <button onclick="ad_relist(<?= $ad->id ?>)" class="btn btn-block btn-ge">RELIST</button>
                                            </div>
                                            <div class="span6">
                                                <button class="btn btn-block btn-ge">HIDE</button>
                                            </div>
                                        </div>
                                        
                                        <div class="row-fluid ge-text ge-description ge-user-image ge-action">
                                    
                                        <? if( $ad->requests != null ): ?>
                                        <? foreach( $ad->requests as $request ): ?>
                                            <?
                                            $requestor_img = $request->user->getAvatarUrl();
                                            ?>

                                            <div class="span4"><img src="<?=$requestor_img?>" class="img img-rounded inactive"></div>
                                        <? endforeach; ?>
                                        <? endif; ?>
                                        
                                        </div>
                                    
                                    <? elseif( !$ad->hasRequest() ): ?>

                                        <div class="row-fluid ge-text ge-description">
                                            <div class="span12">
                                                <p class="text-center"><span class="fui-triangle-down"></span> Share to receive requests <span class="fui-triangle-down"></span></p>
                                            </div>
                                        </div>

                                        <div class="row-fluid ge-text ge-description ge-action">
                                            <div class="span4">					
                                                <a class="btn btn-mini btn-block btn-social-facebook link ge-icon-facebook"></a>
                                            </div>
                                            <div class="span4">
                                                <a class="btn btn-mini btn-block btn-social-twitter link ge-icon-twitter"></a>
                                            </div>
                                            <div class="span4">
                                                <a class="btn btn-mini btn-block btn-social-pinterest link ge-icon-pinterest"></a>
                                            </div>
                                        </div><!--./ge-action-->

                                    <? elseif( $ad->hasSelection() ): ?>

                                        <div class="row-fluid ge-text ge-description">
                                            <div class="span12">
                                                <p class="text-center">Recipient selected</p>
                                            </div>
                                        </div><!--./ge-text ge-description-->

                                        <div class="row-fluid ge-text ge-description ge-user-image ge-action">
                                        
                                        <? if( $ad->requests != null ): ?>
                                        <? foreach( $ad->requests as $request ): ?>
                                            <?
                                            $request_id = $request->id;
                                            $requestor_img = $request->user->getAvatarUrl();
                                            $request_selected = $request->isSelected();
                                            ?>

                                            <? if( $request_selected ): ?>
                                                <div class="span4"><img onclick="request_view(<?=$request_id?>);" src="<?=$requestor_img?>" class="img img-rounded link"></div>
                                            <? else: ?>
                                                <div class="span4"><img src="<?=$requestor_img?>" class="img img-rounded inactive"></div>
                                            <? endif; ?>
                                        <? endforeach; ?>
                                        <? endif; ?>

                                        </div>

                                    <? else: ?>

                                        <div class="row-fluid ge-text ge-description">
                                            <div class="span12">
                                                <p class="text-center"><span class="fui-triangle-down"></span> Select recipient <span class="fui-triangle-down"></span></p>
                                            </div>
                                        </div><!--./ge-text ge-description-->

                                        <div class="row-fluid ge-text ge-description ge-user-image ge-action">

                                        <? foreach( $ad->requests as $request ): ?>
                                            <?
                                            $request_id = $request->id;
                                            $requestor_img = $request->user->getAvatarUrl();
                                            ?>

                                            <div class="span4"><img onclick="request_view(<?=$request_id?>);" src="<?=$requestor_img?>" class="img img-rounded link"></div>
                                        <? endforeach; ?>

                                        </div><!--./ge-action-->

                                    <? endif; ?>
                                    <? endif; ?>
                                    
                                </div><!--./ge-item-->
                            </div>
                        </div>
                    </div>
                </div><!--./ge-box-->

            <? endforeach; ?>
            <? else: ?>
                
                <img src="<?=BASE_PATH?>temp-sample/ge-no-gift.png" class="img img-rounded">
                
            <? endif; ?>
            
            </div>
        </div>
    </div>
</div>
