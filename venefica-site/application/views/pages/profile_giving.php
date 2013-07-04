<div class="row">			
    <div class="container user-giving_items">
        <div class="row">
            <div class="ge-tile-view ge-browse">

            <? foreach( $givings as $ad ): ?>
                
                <div class="span3 ge-box">
                    <div class="well ge-well">
                        <div class="row-fluid">
                            <div class="span12">
                                <div class="ge-item">
                                    
                                    <? $this->load->view('element/ad_item', array('ad' => $ad)); ?>
                                    
                                    <? if( $ad->expired ): ?>

                                        <div class="row-fluid ge-text ge-description">
                                            <div class="span6">
                                                <button class="btn btn-block btn-ge">RELIST</button>
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
                                            $requestor_img = $request->user->getAvatarUrl();
                                            $request_selected = $request->isSelected();
                                            ?>

                                            <div class="span4"><img src="<?=$requestor_img?>" class="img img-rounded <?= $request_selected ? '' : 'inactive' ?>"></div>
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
                                            $requestor_img = $request->user->getAvatarUrl();
                                            ?>

                                            <div class="span4"><img src="<?=$requestor_img?>" class="img img-rounded"></div>
                                        <? endforeach; ?>

                                        </div><!--./ge-action-->

                                    <? endif; ?>
                                    
                                </div><!--./ge-item-->
                            </div>
                        </div>
                    </div>
                </div><!--./ge-box-->

            <? endforeach; ?>
                
            </div>
        </div>
    </div>
</div>
