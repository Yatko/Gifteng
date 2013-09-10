<?

/**
 * Input params:
 * 
 * user: User_model
 * givings: array of Ad_model
 */

//A very good example: http://bragthemes.com/demo/pinstrap/

$is_owner = isOwner($user);

?>

<script language="javascript">
    var numberOfCreatedAds = 0;
    
    $(function() {
        $('.ge-ad').on('ad_deleted', function(event, adId) {
            $('#ad_' + adId).removeClass('masonry-brick');
            $('#ad_' + adId).addClass('hide');
        });
        $('.ge-request').on('request_canceled', function(event, requestId, adId, resultHtml, resultNum) {
            $('#ad_' + adId).html(resultHtml);
        });
        
        <? if( $is_owner ): ?>
        if ( $('#postContainer').length > 0 ) {
            $('#postContainer').on('ad_posted', function(event, adId) {
                numberOfCreatedAds++;
                
                if ( $('.ge-no-ad').length > 0 ) {
                    $('.ge-no-ad').addClass('hide');
                }

                if ( $('#ad_' + adId).length === 0 ) {
                    $('.ge-browse .span3:nth-child(' + (((numberOfCreatedAds - 1) % 4) + 1) + ')').prepend('<div id="ad_' + adId + '" class="masonry-brick"></div>');
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

    <div class="container user-giving_items <?= empty($_GET) ? 'hidden-phone' : '' ?>">
        <div class="row">
            <div class="ge-tile-view ge-browse masonry">

            <? if( isset($givings) && is_array($givings) && count($givings) > 0 ): ?>
                
                <? for ( $iii = 4; $iii > 0; $iii-- ): ?>
                <div class="span3">
                
                <? foreach( $givings as $index => $ad ): ?>
                    <?
                    if ( ($index + $iii) % 4 != 0 ) {
                        continue;
                    }
                    
                    $ad_id = $ad->id;
                    ?>
                    
                    <div id="ad_<?=$ad_id?>" class="masonry-brick">
                        <? $this->load->view('element/ad_giving', array('ad' => $ad, 'user_id' => $user->id)); ?>
                    </div>
                <? endforeach; ?>
                
                </div>
                <? endfor; ?>
                
            <? elseif ( $is_owner ): ?>
                
                <div class="span12 ge-no-ad">
                    <div class="well ge-well">
                        <div class="row-fluid">
                            <div class="span10 offset1 text-center">
                                    <img src="<?=BASE_PATH?>images/ge-post_your_first_gift.jpg" width="800" height="500" alt="Post Your First Gift Now!" />
                            </div>
                        </div>
                        <div class="row-fluid">
                                <div class="ge-action">
                                        <div class="span4 offset4 text-center">
                                                <button data-target="#postContainer" data-toggle="modal" class="btn btn-small btn-block btn-ge"><i class="ge-icon-giftbox"></i>Post Your First Gift Now!</button>
                                        </div>
                                </div>
                        </div>
                        <div class="row-fluid">
                            <div class="ge-action">
                                <div class="span4 offset4 text-center">
                                        <a href="<?=base_url()?>browse"><span style="font-size: 90%;color: #6f6f6f;margin-bottom: 19px;">Or click here to see what others are giving away.</span></a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
            <? else: ?>
                
                <div class="span12">
                    <div class="well ge-well">
                        <div class="row-fluid">
                            <div class="span10 offset1 text-center">
                                    <img src="<?=BASE_PATH?>images/ge-nothing_here.jpg" width="800" height="500" alt="Nothing here ... :(" />
                            </div>
                        </div>
                    </div>
                </div>
                
            <? endif; ?>
            
            </div>
        </div>
    </div>
