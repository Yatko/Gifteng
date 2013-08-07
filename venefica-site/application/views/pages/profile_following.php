<?

/**
 * Input params:
 * 
 * follow_users: array of User_model
 * follow_ads: array of Ad_model
 */

?>

<script language="javascript">
    $(function() {
        $(".ge-browse").vgrid({
            easing: "easeOutQuint",
            time: 500,
            delay: 20,
            fadeIn: {
                time: 300,
                delay: 50
            }
        });
    });
</script>

<div class="row">			
    <div class="container user-follow">
        <div class="row">
            <div class="ge-tile-view ge-browse">
            
            <? if( isset($follow_users) && is_array($follow_users) && count($follow_users) > 0 ): ?>
            <? foreach( $follow_users as $follow ): ?>
                
                <?
                $user_id = $follow->id;
                $ads = $follow_ads[$user_id];
                ?>
				
				<div class="span3">
					<div class="ge-box">
						<div class="well ge-well">
							
							<div class="row-fluid">
								<div class="span12">
									
									<div class="ge-user">
										<? $this->load->view('element/user', array('user' => $follow, 'canEdit' => false, 'small' => true)); ?>
									</div><!--./ge-user-->
									
                                    <? if( count($ads) > 0 ): ?>
                                    
									<div class="ge-text ge-description ge-user-image ge-action">
										<div class="row-fluid">
										<? foreach ($ads as $ad): ?>
                                            
                                            <?
                                            $img = $ad->getImageUrl();
                                            $view_link = $ad->getViewUrl();
                                            $inactive = ($ad->status == Ad_model::STATUS_FINALIZED || $ad->status == Ad_model::STATUS_EXPIRED)
                                            ?>
                                            
                                            <div class="span4">
                                                <a href="<?=$view_link?>"><img src = "<?=$img?>" class="img img-rounded <?= $inactive ? 'inactive' : '' ?>" /></a>
                                            </div>
                                            
                                        <? endforeach; ?>
                                        
                                        </div>
                                    </div><!--./ge-action-->
                                    
                                    <? else: ?>
                                    
									<div class="ge-text ge-description ge-user-image ge-action">
										<div class="row-fluid">
                                            <div class="span4">
                                                <img src="<?=BASE_PATH?>temp-sample/ge-gift.png" class="img img-rounded">
                                            </div>
                                        </div>
                                    </div><!--./ge-action-->
                                    
                                    <? endif; ?>
                                    
								</div>
							</div>
					
						</div>
					</div><!--./ge-box-->
				</div>

            <? endforeach; ?>
            <? endif; ?>
            
            </div><!--./ge-tile-view-->
        </div><!--./row-->
    </div><!--./container user-follow-->					
</div>