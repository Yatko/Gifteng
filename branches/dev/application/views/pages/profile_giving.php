<?

/**
 * Input params:
 * 
 * user: User_model
 * givings: array of Ad_model
 */

$is_owner = isOwner($user);

?>

<script language="javascript">
    $(function() {
        $('.ge-ad').on('ad_deleted', function(event, adId) {
            $('#ad_' + adId).addClass('hide');
        });
        $('.ge-request').on('request_canceled', function(event, requestId, adId, result) {
            $('#ad_' + adId).html(result);
        });
        
        <? if( $is_owner ): ?>
        if ( $('#postContainer').length > 0 ) {
            $('#postContainer').on('ad_posted', function(event, adId) {
                if ( $('.ge-no-ad').length > 0 ) {
                    $('.ge-no-ad').addClass('hide');
                }

                if ( $('#ad_' + adId).length === 0 ) {
                    $('.ge-browse').prepend('<div id="ad_' + adId + '"></div>');
                }
                
                $.getJSON('<?=base_url()?>ajax/getAdGiving/' + adId + '/<?=$user->id?>', function(response) {
                    if ( !response || response === '' ) {
                        //TODO: empty result
                    } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                        //TODO
                    } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                        $('#ad_' + adId).html(response.<?=AJAX_STATUS_RESULT?>);
                    } else {
                        //TODO: unknown response received
                    }
                });
            });
        }
        <? endif; ?>
        
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

<div class="row">
    <div class="container user-giving_items">
        <div class="row">
            <div class="ge-tile-view ge-browse">

            <? if( isset($givings) && is_array($givings) && count($givings) > 0 ): ?>
                
                <? foreach( $givings as $ad ): ?>
                    <?
                    $ad_id = $ad->id;
                    ?>
                    
                    <div id="ad_<?=$ad_id?>">
                        <? $this->load->view('element/ad_giving', array('ad' => $ad, 'user_id' => $user->id)); ?>
                    </div>
                <? endforeach; ?>
                
            <? else: ?>
                
                <div class="span4 ge-box ge-no-ad">
                    <div class="well ge-well">

                        <div class="ge-item">	
                            <div class="row-fluid ge-item-image">
                                <img src="<?=BASE_PATH?>temp-sample/ge-gift.png" class="img" />

                                <div class="row-fluid">
                                    <div class="ge-action">
                                        <div class="span12">
                                            <button data-target="#postContainer" data-toggle="modal" class="btn btn-small btn-block btn-ge">
                                                <i class="ge-icon-giftbox"></i>
                                                Post Your First Gift Now!
                                            </button>
                                        </div>

                                        <a href="<?=base_url()?>browse">Or click here to see what others are giving away.</a>
                                    </div>
                                </div>
                            </div><!--./ge-item-image-->
                        </div><!--./ge-item-->

                    </div>
                </div><!--/item-->
                
            <? endif; ?>
            
            </div>
        </div>
    </div>
</div>
