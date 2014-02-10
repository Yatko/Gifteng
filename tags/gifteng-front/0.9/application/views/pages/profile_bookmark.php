<?

/**
 * Input params:
 * 
 * user: User_model
 * bookmarks: array of Ad_model
 */

$user_can_request = $user->canRequest();

?>

<script language="javascript">
    $(function() {
        $('.ge-request').on('request_created', function(event, adId) {
            $('#ad_' + adId).removeClass('masonry-brick');
            $('#ad_' + adId).addClass('hide');
        });
        /**
        $(".ge-browse").vgrid({
            easing: "easeOutQuint",
            time: 500,
            delay: 20,
            fadeIn: {
                time: 300,
                delay: 50
            }
        });
        /**/
    });
</script>

    <div class="container user-favorites">
        <div class="row">
            <div class="ge-tile-view ge-browse masonry">
            
            <? if( isset($bookmarks) && is_array($bookmarks) && count($bookmarks) > 0 ): ?>
                
                <? for ( $iii = 4; $iii > 0; $iii-- ): ?>
                <div class="span3">
                
                <? foreach( $bookmarks as $index => $ad ): ?>
                    <?
                    if ( ($index + $iii) % 4 != 0 ) {
                        continue;
                    }
                    
                    $ad_id = $ad->id;
                    $ad_is_requested = $ad->requested;
                    $ad_is_business = $ad->isBusiness();
                    $ad_is_expired = $ad->expired;
                    $ad_is_owned = $ad->owner;
                    $ad_can_request = $ad->canRequest;

                    $inactive = !$ad_can_request;
                    ?>		

                    <div id="ad_<?=$ad_id?>" class="masonry-brick ge-ad-item-box <?=($inactive ? 'ge-inactive' : 'ge-active')?>">
                        <div class="ge-box">
                            <div class="well ge-well">

                                <div class="row-fluid">
                                    <div class="span12">

                                        <div class="ge-user">
                                            <? $this->load->view('element/user', array('user' => $ad->creator, 'canEdit' => false, 'small' => true, 'size' => LIST_USER_IMAGE_SIZE)); ?>
                                        </div><!--./ge-user-->

                                        <div class="ge-item">	
                                            <? $this->load->view('element/ad_item', array('ad' => $ad, 'size' => LIST_AD_IMAGE_SIZE)); ?>

                                            <div class="ge-action">
                                                <div class="row-fluid">
                                                    <div class="span4 mobile-one">
                                                        <button onclick="remove_bookmark(this, <?=$ad_id?>);" class="ge-bookmark btn btn-small btn-block fui-cross"></button>
                                                    </div>
                                                    <div class="span8 mobile-three">

                                                    <? if( $ad_is_owned ): ?>
                                                        <p class="text-center">MINE</p>
                                                    <? elseif( $ad_is_expired ): ?>
                                                        <p class="text-center">ENDED</p>
                                                    <? elseif( $ad_is_requested ): ?>
                                                        <p class="text-center">REQUESTED</p>
                                                    <? elseif ( $ad_can_request  ): ?>
                                                        <? if( $user_can_request ): ?>
                                                            <button onclick="startRequestModal(this, '<?= ($ad_is_business ? 'business' : 'member') ?>', <?=$ad_id?>);" class="ge-request btn btn-small btn-block btn-ge">Request Gift</button>
                                                        <? else: ?>
                                                            <button onclick="startCannotRequestModal();" class="ge-request btn btn-small btn-block btn-ge">Request Gift</button>
                                                        <? endif; ?>
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

                <? endforeach; ?>
                
                </div>
                <? endfor; ?>
            
            <? else: ?>
                
                <div class="span12">
                    <div class="well ge-well">
                        <div class="row-fluid">
                            <div class="span10 offset1 text-center">
                                    <img src="<?=BASE_PATH?>images/ge-nothing_here_favorites.jpg" width="800" height="500" alt="Nothing here ... :(" />
                            </div>
                        </div>
                    </div>
                </div>
                
            <? endif; ?>

            </div><!--./ge-tile-view-->
        </div>
    </div>