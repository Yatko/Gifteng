<?

/**
 * Input params:
 * 
 * bookmarks: array of Ad_model
 */

?>

<script language="javascript">
    $(function() {
        $('.ge-bookmark').on('bookmark_removed', function(event, adId) {
            $('#ad_' + adId).addClass('hide');
        });
        $('.ge-request').on('request_created', function(event, adId) {
            $('#ad_' + adId).addClass('hide');
        });
    });
</script>

<div class="row">
    <div class="container user-favorites">
        <div class="row">
        	<div class="ge-tile-view ge-browse">
        	
			<? if( isset($bookmarks) && is_array($bookmarks) && count($bookmarks) > 0 ): ?>
            <? foreach( $bookmarks as $ad ): ?>
                <?
                $ad_id = $ad->id;
                $ad_is_requested = $ad->requested;
                $ad_is_business = $ad->isBusiness();
                $ad_is_expired = $ad->expired;
                $ad_is_owned = $ad->owner;
                $ad_can_request = $ad->canRequest;
                ?>		
				
                <div id="ad_<?=$ad_id?>">
                	<div class="span3">
					<div class="ge-box">
						<div class="well ge-well">
							
							<div class="row-fluid">
								<div class="span12">
									
									<div class="ge-user">
										<? $this->load->view('element/user', array('user' => $ad->creator, 'canEdit' => false, 'small' => true)); ?>
									</div><!--./ge-user-->
										
									<div class="ge-item">	
										<? $this->load->view('element/ad_item', array('ad' => $ad, 'canBookmark' => false, 'canComment' => false, 'canShare' => false)); ?>
                                    
										
										<div class="ge-action">
											<div class="row-fluid">
												<div class="span4 mobile-one">
													<a onclick="remove_bookmark(this, <?=$ad_id?>);" href="" class="btn btn-small btn-block fui-cross"></a>
												</div>
												<div class="span8 mobile-three">
		                                        <? if( $ad_is_owned ): ?>
		                                            <p class="text-center">MINE</p>
		                                        <? elseif( $ad_is_expired ): ?>
		                                            <p class="text-center">EXPIRED</p>
		                                        <? elseif( $ad_is_requested ): ?>
		                                            <p class="text-center">REQUESTED</p>
		                                        <? elseif ( $ad_can_request  ): ?>
													<a onclick="startRequestModal(this, '<?= ($ad_is_business ? 'business' : 'member') ?>', <?=$ad_id?>);" href="" class="btn btn-small btn-block btn-ge">Request Gift</a>
												<? endif; ?>
												</div>
											</div>
										</div><!--./ge-action-->					
									</div><!--./ge-item-->
									
								</div>
							</div>
					
						</div>
					</div><!--./ge-box-->
				</div>
                </div>
				
                
            <? endforeach; ?>
            <? else: ?>
                
                <img src="<?=BASE_PATH?>temp-sample/ge-gift.png" class="img img-rounded">
                
            <? endif; ?>

			</div><!--./ge-tile-view-->
        </div>
    </div>
</div>