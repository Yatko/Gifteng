<?

/**
 * Input params:
 * 
 * user: User_model
 * receivings: array of Ad_model
 */

?>

<script language="javascript">
    function request_view(requestId) {
        if ( $('#requestContainer').length > 0 ) {
            $('#requestContainer').modal({
                remote: '<?=base_url()?>request/' + requestId + '?modal&receiving&userId=<?=$user->id?>',
                show: true
            });
        }
    }
    
    $(function() {
        $('.ge-request').on('request_canceled', function(event, requestId, adId, result) {
            $('#request_' + requestId).addClass('hide');
        });
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
                            <? $this->load->view('element/request_receiving', array('ad' => $ad, 'request' => $request)); ?>
                        </div>
                    <? endforeach; ?>
                    
                <? else: ?>
                
                    <img src="<?=BASE_PATH?>temp-sample/ge-gift.png" class="img img-rounded">
                
                <? endif; ?>
                
            </div>
        </div>
    </div>
</div>