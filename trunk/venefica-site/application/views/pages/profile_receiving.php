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
            $('#request_' + requestId).removeClass('masonry-brick');
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

    <div class="container user-receiving_items">
        <div class="row">
            <div class="ge-tile-view ge-browse masonry">

                <? if( isset($receivings) && is_array($receivings) && count($receivings) > 0 ): ?>
                    
                    <? for ( $iii = 4; $iii > 0; $iii-- ): ?>
                    <div class="span3">
                    
                    <? foreach( $receivings as $index => $ad ): ?>
                        <?
                        if ( ($index + $iii) % 4 != 0 ) {
                            continue;
                        }
                        
                        $request = $ad->getRequestByUser($user->id);
                        $request_id = $request->id;
                        ?>
                        
                        <div id="request_<?=$request_id?>" class="masonry-brick">
                            <? $this->load->view('element/ad_receiving', array('ad' => $ad, 'request' => $request, 'user_id' => $user->id)); ?>
                        </div>
                    <? endforeach; ?>
                        
                    </div>
                    <? endfor; ?>
                    
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