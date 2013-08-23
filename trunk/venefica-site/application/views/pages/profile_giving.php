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
                
                <!-- <div class="row"> -->
                    <div class="span12">
                        <div class="well ge-well">
                            <div class="row-fluid">
                                <div class="span10 offset1 text-center">
                                	<img src="<?=BASE_PATH?>images/ge-post_your_first_gift.jpg" width="800" height="500" alt="Post Your First Gift Now!" />
                                </div>
                        </div>
                        </div>
                    </div>
                <!-- </div> -->
                
            <? endif; ?>
            
            </div>
        </div>
    </div>
</div>
