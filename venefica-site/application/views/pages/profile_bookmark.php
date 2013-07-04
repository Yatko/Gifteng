<script language="javascript">
    $(function() {
        $('.ge-bookmark').on('bookmark_removed', function(event, adId) {
            $('#ad_' + adId).addClass('hide');
        });
        $('.ge-request').on('requested', function(event, adId) {
            $('#ad_' + adId).addClass('hide');
        });
    });
</script>

<div class="row">
    <div class="container user-receiving_items">
        <div class="row">
            <div class="ge-tile-view ge-browse">

            <? foreach( $bookmarks as $ad ): ?>
                <?
                $ad_id = $ad->id;
                $user_is_business = $user->businessAccount;
                $ad_is_requested = $ad->requested;
                $ad_is_business = $ad->isBusiness();
                $ad_is_expired = $ad->expired;
                $ad_is_owned = $ad->owner;
                ?>
    
                <div id="ad_<?=$ad_id?>">
                    
                <div class="span3 ge-box">
                    <div class="well ge-well">

                        <div class="row-fluid">
                            <div class="span12">

                                <div class="ge-user">
                                    <? $this->load->view('element/user', array('user' => $ad->creator, 'canEdit' => false, 'small' => true)); ?>
                                </div><!--./ge-user-->

                                <div class="ge-item">
                                    <? $this->load->view('element/ad_item', array('ad' => $ad, 'canBookmark' => false, 'canComment' => false, 'canShare' => true)); ?>
                                    
                                    <div class="row-fluid ge-text ge-description ge-action">
                                        <div class="span4">
                                            <button onclick="remove_bookmark(this, <?=$ad_id?>);" class="ge-bookmark btn btn-small btn-block fui-cross"></button>
                                        </div>
                                        
                                        <div class="span8">
                                        <? if( $ad_is_owned ): ?>
                                            <p class="text-center">MINE</p>
                                        <? elseif( $ad_is_expired ): ?>
                                            <p class="text-center">EXPIRED</p>
                                        <? elseif( $ad_is_requested ): ?>
                                            <p class="text-center">REQUESTED</p>
                                        <? elseif ( !$user_is_business  ): ?>
                                            <button onclick="startRequest(this, '<?= ($ad_is_business ? 'business' : 'member') ?>', <?=$ad_id?>);" class="ge-request btn btn-small btn-block btn-ge">REQUEST GIFT</button>
                                        <? endif; ?>
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
        </div>
    </div>
</div>
