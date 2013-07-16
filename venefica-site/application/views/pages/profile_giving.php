<script language="javascript">
    function request_view(requestId) {
        if ( $('#requestContainer').length > 0 ) {
            $('#requestContainer').modal({
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
                
                <img src="<?=BASE_PATH?>temp-sample/ge-no-gift.png" class="img img-rounded">
                
            <? endif; ?>
            
            </div>
        </div>
    </div>
</div>
