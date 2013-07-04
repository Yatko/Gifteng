<script langauge="javascript">
    function show_description_rest(callerElement) {
        $('.description_separator').addClass('hide');
        $('.description_rest').removeClass('hide').css('display', 'inline');
        $(callerElement).addClass('hide');
    }
    
    $(function() {
        if ( $('#map').length > 0 ) {
            var locationIcon = L.icon({
                iconUrl: '<?=BASE_PATH?>temp-sample/ge-location-pin-teal.png',
                iconSize: [64, 64]
            });
            
            var marker_longitude = $("#marker_longitude").val();
            var marker_latitude = $("#marker_latitude").val();
            
            var locationMarker = L.marker([marker_latitude, marker_longitude], {
                icon: locationIcon,
                draggable: false
            });
            
            var tileLayer = L.tileLayer.provider('Esri.WorldStreetMap');
            var map = L.map('map').setView([marker_latitude, marker_longitude], 16);
            tileLayer.addTo(map);
            locationMarker.addTo(map);
        }
    });
</script>


<?

$is_owner = $ad->owner;
$user_is_business = $user->businessAccount;
$ad_is_business = $ad->isBusiness();
$ad_is_online = $ad->isOnline();
$ad_is_requested = $ad->requested;
$ad_has_request = $ad->hasRequest();

if ( $ad->address != null ) {
    $ad_longitude = $ad->address->longitude;
    $ad_latitude = $ad->address->latitude;
} else {
    $ad_longitude = 0;
    $ad_latitude = 0;
}

$ad_id = $ad->id;
$ad_title = trim($ad->title);
$ad_subtitle = trim($ad->subtitle);
$ad_category = trim($ad->category);
$ad_price = trim($ad->price);
$ad_pickUp = $ad->pickUp;
$ad_freeShipping = $ad->freeShipping;
$ad_description = trim($ad->description);


if ( $ad_subtitle != '' ) $ad_subtitle = $ad_subtitle . ' % off the bill';
if ( $ad_price != '' ) $ad_price = '$' . $ad_price;

if ( strlen($ad_description) > DESCRIPTION_MAX_LENGTH ) {
    $ad_description_rest = substr($ad_description, DESCRIPTION_MAX_LENGTH);
    $ad_description = substr($ad_description, 0, DESCRIPTION_MAX_LENGTH);
} else {
    $ad_description_rest = '';
}

?>


<div class="row">
    <div class="ge-detail-view">
        
        <div class="span6">
            <div class="well ge-well">
                <div class="row-fluid">
                    <div class="span12">

                        <div class="ge-user">
                            <? $this->load->view('element/user', array('user' => $ad->creator, 'canEdit' => false, 'small' => false)); ?>
                        </div>

                        <div class="ge-item">
                            <? $this->load->view('element/ad_item', array('ad' => $ad, 'canBookmark' => true, 'canComment' => false, 'canShare' => true)); ?>
                        </div>

                    </div>
                </div>
            </div>
        </div><!--./ge-item main-->

        <div class="span6 ge-item">
            <div class="well ge-well">

                <? if( $is_owner || (!$user_is_business && !$ad_is_requested) ): ?>

                <div id="ad_control" class="row-fluid ge-action">
                    <div class="span12">
                        <div class="control-group">
                            <div class="controls">
                                <? if( $is_owner && !$ad_has_request ): ?>
                                    <div class="span6">
                                        <button class="btn btn-large btn-ge btn-block" type="button">EDIT GIFT</button>
                                    </div>
                                    <div class="span6">
                                        <button class="btn btn-large btn-ge btn-block" type="button">DELETE GIFT</button>
                                    </div>
                                <? elseif ( !$user_is_business && !$ad_is_requested ): ?>
                                    <button onclick="startRequest(this, '<?= ($ad_is_business ? 'business' : 'member') ?>', <?=$ad_id?>);" class="ge-request btn btn-large btn-ge btn-block" type="button">REQUEST GIFT</button>
                                <? endif; ?>
                            </div>
                        </div>
                    </div>
                </div><!--./ge-action-->

                <? endif; ?>

                <div class="row-fluid">
                    <div class="ge-text ge-description">
                        <p class="ge-title">
                            <?=$ad_title?>
                            <? if( $ad_subtitle != '' ): ?>
                                <span class="ge-subtitle">
                                    <em>&amp;</em>
                                    <?=$ad_subtitle?>
                                </span>
                            <? endif; ?>
                        </p>

                        <p class="ge-description">
                            <em>Description:</em>
                            <?=$ad_description?>

                            <? if( $ad_description_rest != '' ): ?>
                                <span class="description_separator">...</span>
                                <span class="description_rest hide"><?=$ad_description_rest?></span>

                                <a class="text-note link" onclick="show_description_rest(this);">read more</a>
                            <? endif; ?>
                        </p>

                        <p class="ge-details">
                        <ul>
                            <li><em>Category: </em><?=$ad_category?></li>
                            <li><em>Current value: </em><?=$ad_price?></li>
                        </ul>
                        <div class="row-fluid">
                            <div class="span6">
                                <label class="checkbox">
                                    <input <?=(isset($ad_pickUp) && $ad_pickUp) ? 'checked="checked"' : ''?> type="checkbox" disabled="disabled" data-toggle="checkbox"/>
                                    Pick up
                                </label>
                            </div>
                            <div class="span6">
                                <label class="checkbox">
                                    <input <?=(isset($ad_freeShipping) && $ad_freeShipping) ? 'checked="checked"' : ''?> type="checkbox" disabled="disabled" data-toggle="checkbox"/>
                                    Free shipping
                                </label>
                            </div>	
                        </div>
                        </p>
                    </div>
                </div><!--./ge-text-->


                <? if( $ad_is_business && $ad_is_online ): ?>
                    <div class="row-fluid ge-map">
                        <img src="<?=BASE_PATH?>temp-sample/ge-map-online.png" class="img">
                    </div><!--./ge-map-->
                <? else: ?>
                    <input id="marker_longitude" type="hidden" value="<?=$ad_longitude?>">
                    <input id="marker_latitude" type="hidden" value="<?=$ad_latitude?>">

                    <div class="row-fluid ge-map">
                        <div id="map"></div>
                    </div><!--./ge-map-->
                <? endif; ?>

            </div>
        </div><!--./ge-item details-->

        <div class="span6 ge-item">
            <div class="well ge-well">
                <? $this->load->view('element/comments', array('comments' => $comments, 'ad' => $ad, 'canComment' => true)); ?>
            </div>
        </div>
    </div>

</div><!-- ./row -->
