<?

/**
 * Input params:
 * 
 * follow_users: array of User_model
 * follow_ads: array of Ad_model
 */

$active_menu = Profile::getActiveMenu();
$active_tab = Profile::getActiveTab($active_menu);

?>

<script language="javascript">
    $(function() {
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
        
        <? if( $active_menu == Profile::MENU_FOLLOWING ): ?>
        $(".ge-user-following").on('unfollowed', function(event, userId) {
            $('#user_' + userId).removeClass('masonry-brick');
            $('#user_' + userId).addClass('hide');
        });
        <? endif; ?>
    });
</script>
		
    <div class="container user-follow">
        <div class="row">
            <div class="ge-tile-view ge-browse masonry">
            
            <? if( isset($follow_users) && is_array($follow_users) && count($follow_users) > 0 ): ?>
                
                <? for ( $iii = 4; $iii > 0; $iii-- ): ?>
                <div class="span3">
                
                <? foreach( $follow_users as $index => $follow ): ?>
                    <?
                    if ( ($index + $iii) % 4 != 0 ) {
                        continue;
                    }
                    
                    $user_id = $follow->id;
                    $ads = $follow_ads[$user_id];
                    ?>

                    <div id="user_<?=$user_id?>" class="ge-user-following masonry-brick">
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
                                                $ad_can_request = $ad->canRequest;

                                                //$inactive = ($ad->status == Ad_model::STATUS_FINALIZED || $ad->status == Ad_model::STATUS_EXPIRED);

                                                $inactive = false;
                                                if ( !$ad_can_request ) {
                                                    $inactive = true;
                                                }
                                                ?>

                                                <div class="span4 mobile-one">
                                                    <div class="ge-ad-item-box <?= ($inactive ? 'ge-inactive' : 'ge-active') ?>">
                                                        <div class="ge-item-image">
                                                            <a href="<?=$view_link?>"><img src = "<?=$img?>" class="img img-rounded <?= $inactive ? 'inactive' : '' ?>" /></a>
                                                        </div>
                                                    </div>
                                                </div>
                                            <? endforeach; ?>

                                            </div>
                                        </div>

                    <? else: ?>

                                        <div class="ge-text ge-description ge-user-image ge-action">
                                            <div class="row-fluid">
                                                <div class="span4 mobile-one">
                                                    <img src="<?=BASE_PATH?>temp-sample/ge-gift.png" class="img img-rounded">
                                                </div>
                                            </div>
                                        </div>

                    <? endif; ?>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    </div>

                <? endforeach; ?>
                    
                </div>
                <? endfor; ?>
                    
            <? endif; ?>
            
            </div>
        </div>
    </div>