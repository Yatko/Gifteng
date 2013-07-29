<?

/**
 * Input params:
 * 
 * user: User_model
 * givings: array of Ad_model
 */

?>

<script language="javascript">
    function request_view(requestId) {
        if ( $('#requestContainer').length > 0 ) {
            $('#requestContainer').removeData('modal').modal({
                remote: '<?=base_url()?>request/' + requestId + '?modal&giving&userId=<?=$user->id?>',
                show: true
            });
        }
    }
    
    $(function() {
        $('.ge-request').on('request_canceled', function(event, requestId, adId, result) {
            $('#ad_' + adId).html(result);
        });
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
                
                <div class="span4 ge-box">
                    <div class="well ge-well">

                        <div class="row-fluid">
                            <div class="span12">

                                <div class="ge-item">	
                                    <div class="row-fluid ge-item-image">

                                        <img src="<?=BASE_PATH?>temp-sample/ge-gift.png" class="img" />

                                        <div class="row-fluid">
                                            <div class="span12 ge-action">
                                                <div class="row-fluid">
                                                    <div class="span12">
                                                        <button data-target="#postContainer" data-toggle="modal" class="btn btn-small btn-block btn-ge">
                                                            <i class="ge-icon-giftbox"></i>
                                                            Post Your First Gift Now!
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div><!--./ge-item-image-->

                                    <div class="row-fluid ge-text ge-description">
                                        <div class="row-fluid">
                                            <p class="ge-title"> 
                                                <span class="ge-subtitle">You don't have anything to give at this time. Start by posting your first Gift now :)</span>
                                            </p>
                                        </div><!--/text-->
                                    </div>
                                </div><!--./ge-item-->

                            </div>
                        </div>

                    </div>
                </div><!--/item-->
                
            <? endif; ?>
            
            </div>
        </div>
    </div>
</div>
