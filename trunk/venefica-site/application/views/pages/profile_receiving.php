<?

/**
 * Input params:
 * 
 * user: User_model
 * receivings: array of Ad_model
 */

?>

<script language="javascript">
    $(function() {
        $('.ge-request').on('request_canceled', function(event, requestId, adId, result) {
            $('#request_' + requestId).addClass('hide');
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

<div class="row">
    <div class="container user-receiving_items">
        <div class="row">
            <div class="ge-tile-view ge-browse">

                <? if( isset($receivings) && is_array($receivings) && count($receivings) > 0 ): ?>
                    
                    <? foreach( $receivings as $ad ): ?>
                        <?
                        $request = $ad->getRequestByUser($user->id);
                        $request_id = $request->id;
                        ?>
                        
                        <div id="request_<?=$request_id?>">
                            <? $this->load->view('element/ad_receiving', array('ad' => $ad, 'request' => $request, 'user_id' => $user->id)); ?>
                        </div>
                    <? endforeach; ?>
                    
                <? else: ?>
                
                    <img src="<?=BASE_PATH?>temp-sample/ge-gift.png" class="img img-rounded">
                
                <? endif; ?>
                
            </div>
        </div>
    </div>
</div>