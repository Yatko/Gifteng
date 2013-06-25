<script langauge="javascript">
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
        
        $('#bookmarkCheckbox').change(function() {
            $this = $(this);
            
            /**
            $.ajax({
                type: 'POST',
                url: '<?=base_url()?>view/ajax/bookmark?adId=<?=$ad->id?>',
                dataType: 'json',
                cache: false
            }).done(function(response) {
                if ( !response || response == '' ) {
                    //TODO: empty result
                } else if ( response.<?=View::AJAX_STATUS_ERROR?> ) {
                    //TODO
                } else if ( response.<?=View::AJAX_STATUS_RESULT?> ) {
                    //TODO
                } else {
                    //TODO: unknown response received
                }
            }).fail(function(data) {
                //TODO
            });
            /**/
        });
    });
</script>


<?

$is_owner = $ad->owner;
$user_is_business = $user->businessAccount;
$ad_is_business = $ad->isBusiness();
$ad_is_online = $ad->isOnline();
$ad_is_bookmarked = $ad->inBookmars;

if ( $ad->address != null ) {
    $ad_longitude = $ad->address->longitude;
    $ad_latitude = $ad->address->latitude;
} else {
    $ad_longitude = 0;
    $ad_latitude = 0;
}

$creator_img = $ad->getCreatorAvatarUrl();
$creator_name = $ad->getCreatorFullName();
$creator_joined = $ad->getCreatorJoinDate();
$creator_location = $ad->getCreatorLocation();
$creator_points = '';

$ad_img = $ad->getImageUrl();
$ad_title = $ad->title;
$ad_subtitle = $ad->subtitle;
$ad_description = $ad->description;
$ad_category = $ad->category;
$ad_price = $ad->price;
$ad_pickUp = $ad->pickUp;
$ad_freeShipping = $ad->freeShipping;

$creator_name = safe_content($creator_name);

if ( trim($ad_subtitle) != '' ) $ad_subtitle = $ad_subtitle.' % off';

?>


<div class="container ge-topspace">
    <div class="row ge-detail-view">

        <div class="span6 ge-item">
            <div class="well ge-well">

                <div class="row-fluid ge-user">
                    <div class="span12">
                        <div class="ge-user-image">
                            <img src="<?=$creator_img?>" class="img img-rounded">
                        </div>
                        <div class="ge-detail">
                            <div class="ge-name"><?=$creator_name?></div>
                            <? if( $creator_joined != '' ): ?>
                                <div class="ge-age">Giftenger since <?=$creator_joined?></div>
                            <? endif; ?>
                            <? if( $creator_location != '' ): ?>
                                <div class="ge-location"><?=$creator_location?></div>
                            <? endif; ?>
                            <? if( $creator_points != '' ): ?>
                                <div class="ge-points"><?=$creator_points?></div>
                            <? endif; ?>
                        </div>
                    </div>
                </div><!--./ge-user-->

                <div class="row-fluid ge-item-image">
                    <img src="<?=$ad_img?>" class="img">
                </div><!--./ge-item-image-->

                <div class="row-fluid ge-share">					
                    <a href="#fakelink" class="btn btn-small btn-social-facebook">
                        <i class="fui-facebook"></i>
                        Share
                        <span class="btn-tip">0</span>
                    </a>
                    <a href="#fakelink" class="btn btn-small btn-social-twitter">
                        <i class="fui-twitter"></i>
                        Tweet
                        <span class="btn-tip">0</span>
                    </a>
                    <a href="#fakelink" class="btn btn-small btn-social-pinterest">
                        <i class="fui-pinterest"></i>
                        Pin
                        <span class="btn-tip">0</span>
                    </a>
                    
                    <!--
                    <label class="checkbox">
                        <input id="bookmarkCheckbox" <?=$ad_is_bookmarked ? 'checked="checked"' : ''?> type="checkbox" data-toggle="checkbox"/>
                        Bookmark
                    </label>
                    -->
                    
                </div><!--./ge-item-share-->

            </div>
        </div><!--./ge-item main-->

        <div class="span6 ge-item">
            <div class="well ge-well">

                <div class="row-fluid ge-action">
                    <div class="span12">
                        <div class="control-group">
                            <div class="controls">
                                <button class="btn btn-large btn-ge btn-block" type="button">REQUEST GIFT</button>
                            </div>
                        </div>
                    </div>
                </div><!--./ge-action-->

                <div id="item_description" class="row-fluid ge-text">
                    <p class="ge-title" style="margin-top: 0px;padding-top: 0px;line-height: 16px;">
                        <?=$ad_title?>
                        <? if( $ad_subtitle != '' ): ?>
                            <em>&</em>
                            <span class="ge-subtitle"><?=$ad_subtitle?></span>
                        <? endif; ?>
                    </p>

                    <p class="ge-description">
                        <em>Description:</em>
                        <?=$ad_description?>
                    </p>

                    <p class="ge-details">
                        <ul>
                            <li><em>Category: </em><?=$ad_category?></li>
                            <li><em>Current value: $</em><?=$ad_price?></li>
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
                </div><!--./ge-text-->
                
                <? if( $ad_is_business && $ad_is_online ): ?>
                    <div class="row-fluid ge-map ge-item-image">
                        <img src="<?=BASE_PATH?>temp-sample/ge-map-online.png" class="img">
                    </div><!--./ge-map-->
                <? else: ?>
                    <input id="marker_longitude" type="hidden" value="<?=$ad_longitude?>">
                    <input id="marker_latitude" type="hidden" value="<?=$ad_latitude?>">
                    
                    <div class="row-fluid ge-map ge-item-image">
                        <div id="map"></div>
                    </div><!--./ge-map-->
                <? endif; ?>

            </div>
        </div><!--./ge-item details-->

        <div class="span6 ge-item">
            <div class="row-fluid ge-comments">
                <div class="span12">
                    <div class="well ge-well">

                        <div class="row-fluid ge-input">
                            <div class="span12 add_new_comment">
                                <div class="row-fluid"><!--TODO fix this container and problems with textarea -->

                                    <div class="span2">
                                        <div class="ge-user-image">
                                            <img src="temp-sample/ge-user.jpg" class="img img-rounded">
                                        </div>
                                    </div>
                                    <div class="span8">
                                        <textarea rows="2" style="max-width: 265px ;" placeholder="your comment ..."></textarea>
                                    </div>
                                    <div class="span2">
                                        <button id="" name="" class="btn btn-huge btn-ge" style="height:62px ;"><i class="fui-arrow-right pull-right"></i></button>
                                    </div>	

                                </div><!--./add message-->
                            </div>
                        </div>

                        <div class="row-fluid ge-messagelist">
                            <div class="row-fluid ge-message">
                                <div class="ge-user-image">
                                    <img src="temp-sample/ge-user.jpg" class="img img-rounded">
                                </div>
                                <div class="ge-text">
                                    <span class="ge-name">Jean Claude</span><span class="ge-date">- 1 day ago</span>
                                    <span class="ge-block">This tutorial examines the different layout properties available in CSS: position:static, position:relative.</span>
                                </div>
                            </div><!--./ge-message-->
                            <div class="row-fluid ge-message">
                                <div class="ge-user-image">
                                    <img src="temp-sample/ge-user.jpg" class="img img-rounded">
                                </div>
                                <div class="ge-text">
                                    <span class="ge-name">Jean Claude</span><span class="ge-date">- 1 day ago</span>
                                    <span class="ge-block">This tutorial examines the different layout properties available in CSS: position:static, position:relative.</span>
                                </div>
                            </div><!--./ge-message-->
                            <div class="row-fluid ge-message">
                                <div class="ge-user-image">
                                    <img src="temp-sample/ge-user.jpg" class="img img-rounded">
                                </div>
                                <div class="ge-text">
                                    <span class="ge-name">Jean Claude</span><span class="ge-date">- 1 day ago</span>
                                    <span class="ge-block">This tutorial examines the different layout properties available in CSS: position:static, position:relative.</span>
                                </div>
                            </div><!--./ge-message-->
                            <div class="row-fluid ge-message">
                                <div class="ge-user-image">
                                    <img src="temp-sample/ge-user.jpg" class="img img-rounded">
                                </div>
                                <div class="ge-text">
                                    <span class="ge-name">Jean Claude</span><span class="ge-date">- 1 day ago</span>
                                    <span class="ge-block">This tutorial examines the different layout properties available in CSS: position:static, position:relative.</span>
                                </div>
                            </div><!--./ge-message-->
                            <div class="row-fluid ge-message">
                                <div class="ge-user-image">
                                    <img src="temp-sample/ge-user.jpg" class="img img-rounded">
                                </div>
                                <div class="ge-text">
                                    <span class="ge-name">Jean Claude</span><span class="ge-date">- 1 day ago</span>
                                    <span class="ge-block">This tutorial examines the different layout properties available in CSS: position:static, position:relative.</span>
                                </div>
                            </div><!--./ge-message-->
                            <div class="row-fluid ge-message">
                                <div class="ge-user-image">
                                    <img src="temp-sample/ge-user.jpg" class="img img-rounded">
                                </div>
                                <div class="ge-text">
                                    <span class="ge-name">Jean Claude</span><span class="ge-date">- 1 day ago</span>
                                    <span class="ge-block">This tutorial examines the different layout properties available in CSS: position:static, position:relative.</span>
                                </div>
                            </div><!--./ge-message-->
                        </div>

                        <div class="row-fluid ge-expand"></div>

                    </div><!--./ge-well-->
                </div>
            </div><!--./ge-comments-->
        </div><!--./ge-item-->

        <div class="span6 ge-item">
            <div class="well ge-well">

                <div class="row-fluid">
                    <div class="span12">
                        <div class="row-fluid ge-item-thumbnail">

                            <img src="temp-sample/ge-item.jpg" class="img">
                            <img src="temp-sample/ge-item.jpg" class="img">
                            <img src="temp-sample/ge-item.jpg" class="img">
                            <img src="temp-sample/ge-item.jpg" class="img">

                        </div>
                    </div>
                </div>

            </div>
        </div><!--./ge-item more gifts-->

    </div>
</div><!--./ge-container-->