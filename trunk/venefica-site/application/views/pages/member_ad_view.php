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
        
        $('#bookmarkCheckbox').change(function() {
            $this = $(this);
            
            if ( $this.attr('checked') ) {
                $.ajax({
                    type: 'POST',
                    url: '<?=base_url()?>ajax/bookmark?adId=<?=$ad->id?>',
                    dataType: 'json',
                    cache: false
                }).done(function(response) {
                    if ( !response || response == '' ) {
                        //TODO: empty result
                    } else if ( response.<?=AJAX_STATUS_ERROR?> ) {
                        //TODO
                    } else if ( response.<?=AJAX_STATUS_RESULT?> ) {
                        //
                    } else {
                        //TODO: unknown response received
                    }
                }).fail(function(data) {
                    //TODO
                });
            } else {
                $.ajax({
                    type: 'POST',
                    url: '<?=base_url()?>ajax/remove_bookmark?adId=<?=$ad->id?>',
                    dataType: 'json',
                    cache: false
                }).done(function(response) {
                    if ( !response || response == '' ) {
                        //TODO: empty result
                    } else if ( response.<?=AJAX_STATUS_ERROR?> ) {
                        //TODO
                    } else if ( response.<?=AJAX_STATUS_RESULT?> ) {
                        //
                    } else {
                        //TODO: unknown response received
                    }
                }).fail(function(data) {
                    //TODO
                });
            }
        });
    });
</script>


<?

$is_owner = $ad->owner;
//$is_in_followers = $ad->creator->inFollowers;
$is_in_followings = $ad->creator->inFollowings;
$user_is_business = $user->businessAccount;
$ad_is_business = $ad->isBusiness();
$ad_is_online = $ad->isOnline();
$ad_is_bookmarked = $ad->inBookmarks;

if ( $ad->address != null ) {
    $ad_longitude = $ad->address->longitude;
    $ad_latitude = $ad->address->latitude;
} else {
    $ad_longitude = 0;
    $ad_latitude = 0;
}

if ( $ad->statistics != null ) {
    $ad_num_bookmarks = $ad->statistics->numBookmarks;
    $ad_num_comments = $ad->statistics->numComments;
    $ad_num_shares = $ad->statistics->numShares;
} else {
    $ad_num_bookmarks = 0;
    $ad_num_comments = 0;
    $ad_num_shares = 0;
}

$creator_id = $ad->creator->id;
$creator_username = $ad->creator->name;
$creator_img = $ad->getCreatorAvatarUrl();
$creator_name = $ad->getCreatorFullName();
$creator_joined = $ad->getCreatorJoinDate();
$creator_location = $ad->getCreatorLocation();
$creator_points = $ad->getCreatorPoints();

$ad_id = $ad->id;
$ad_img = $ad->getImageUrl();
$ad_title = trim($ad->title);
$ad_subtitle = trim($ad->subtitle);
$ad_category = trim($ad->category);
$ad_price = trim($ad->price);
$ad_pickUp = $ad->pickUp;
$ad_freeShipping = $ad->freeShipping;
$ad_description = trim($ad->description);

$creator_name = safe_content($creator_name);

if ( $ad_subtitle != '' ) $ad_subtitle = $ad_subtitle . ' % off the bill';
if ( $ad_price != '' ) $ad_price = '$' . $ad_price;

if ( strlen($ad_description) > DESCRIPTION_MAX_LENGTH ) {
    $ad_description_rest = substr($ad_description, DESCRIPTION_MAX_LENGTH);
    $ad_description = substr($ad_description, 0, DESCRIPTION_MAX_LENGTH);
} else {
    $ad_description_rest = '';
}

?>


<div class="container ge-topspace">
    
    <div class="row">

        <div class="ge-detail-view">
            <div class="span6 ge-item">
                <div class="well ge-well ge-item">

                    <div class="row-fluid ge-user">
                        <div class="span12">
                            <div class="ge-user-image">
                                <a href="<?=base_url()?>profile/<?=$creator_username?>"><img src="<?=$creator_img?>" class="img img-rounded"></a>
                            </div>
                            <div class="ge-detail">
                                <div class="ge-name"><a href="<?=base_url()?>profile/<?=$creator_username?>"><?=$creator_name?></a></div>
                                <? if( $creator_joined != '' ): ?>
                                    <div class="ge-age">Giftenger since <?=$creator_joined?></div>
                                <? endif; ?>
                                <? if( $creator_location != '' ): ?>
                                    <div class="ge-location"><?=$creator_location?></div>
                                <? endif; ?>
                                
                                <div class="ge-points">
                                    <? if( $creator_points != '' ): ?>
                                        <span class="label"><?=$creator_points?></span>
                                        <!--<span class="label label-ge active">Following</span>-->
                                    <? endif; ?>
                                    
                                    <? if( $is_in_followings ): ?>
                                        <span onclick="follow_unfollow(<?=$creator_id?>)" class="user_<?=$creator_id?> ge-user-follow label label-small label-ge link">Unfollow</span>
                                    <? else: ?>
                                        <span onclick="follow_unfollow(<?=$creator_id?>)" class="user_<?=$creator_id?> ge-user-unfollow label label-small label-ge link">Follow</span>
                                    <? endif; ?>
                                </div>
                            </div>
                        </div>
                    </div><!--./ge-user-->

                    <div class="row-fluid ge-item-image" style="background-image: url('<?=$ad_img?>');">
                        <div class="row-fluid">
                            <div class="ge-ribbon"></div>
                        </div>
                        <div class="row-fluid">
                            <div class="span12 ge-action">
                                <div class="row-fluid">
                                    <div class="span4">
                                        <? if( $ad_is_bookmarked ): ?>
                                            <button class="btn btn-small btn-block btn-ge disabled"><i class="fui-star-2"></i> <?=$ad_num_bookmarks?></button>
                                        <? else: ?>
                                            <button onclick="bookmark(this, <?=$ad_id?>)" class="btn btn-small btn-block btn-ge"><i class="fui-star-2"></i> <span class="ad_bookmark_<?=$ad_id?>"><?=$ad_num_bookmarks?></span></button>
                                        <? endif; ?>
                                    </div>
                                    <div class="span4">
                                        <button class="btn btn-small btn-block btn-ge disabled"><i class="fui-bubble"></i> <?=$ad_num_comments?></button>
                                    </div>
                                    <div class="span4">
                                        <button class="btn btn-small btn-block btn-ge"><i class="fui-export"></i> <?=$ad_num_shares?></button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!--./ge-item-image-->

                    <!--
                    <div class="row-fluid ge-share">
                        <a href="#fakelink" class="btn btn-small btn-social-facebook">
                          <i class="fui-facebook"></i>
                          Like
                          <span class="btn-tip">91</span>
                        </a>
                        <a href="#fakelink" class="btn btn-small btn-social-twitter">
                          <i class="fui-twitter"></i>
                          Tweet
                          <span class="btn-tip">45</span>
                        </a>
                        <a href="#fakelink" class="btn btn-small btn-social-pinterest">
                          <i class="fui-pinterest"></i>
                          Pin
                          <span class="btn-tip">100</span>
                        </a>

                        <a href="#fakelink" class="btn btn-small btn-social- pull-right">
                          FAV<i class="fui-star-2"></i>
                        </a>
                    </div> ./ge-item-share
                    -->

                </div>
            </div><!--./ge-item main-->

            <div class="span6 ge-item">
                <div class="well ge-well">

                    <div class="row-fluid ge-action">
                        <div class="span12">
                            <div class="control-group">
                                <div class="controls">
                                    <? if( $is_owner ): ?>
                                        <button class="btn btn-large btn-ge btn-block" type="button">EDIT GIFT</button>
                                    <? elseif ( !$user_is_business ): ?>
                                        <button class="btn btn-large btn-ge btn-block" type="button">REQUEST GIFT</button>
                                    <? endif; ?>
                                </div>
                            </div>
                        </div>
                    </div><!--./ge-action-->

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
                                    
                                    <a class="text-note link" onclick="show_description_rest(this)">read more</a>
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
                    <div class="row-fluid ge-comments">
                        <div class="span12">

                            <div class="row-fluid ge-messagelist">
                                <div class="span12">
                                    <div class="row-fluid ge-message">
                                        <div class="ge-user-image">
                                            <a href="#profile"><img src="temp-sample/ge-user.jpg" class="img img-rounded"></a>
                                        </div>
                                        <div class="ge-text">
                                            <a class="ge-name" href="#profile">Jean Claude</a><span class="ge-date">- 1 day ago</span>
                                            <span class="ge-block">This tutorial examines the different layout properties available in CSS: position:static ibla...</span>
                                        </div>
                                    </div><!--./ge-message-->

                                    <div class="row-fluid ge-message">
                                        <div class="ge-user-image">
                                            <a href="#profile"><img src="temp-sample/ge-me.jpg" class="img img-rounded"></a>
                                        </div>
                                        <div class="ge-text">
                                            <a class="ge-name" href="#profile">Jean Claude</a><span class="ge-date">- 1 day ago</span>
                                            <span class="ge-block">This tutorial examines the different layout properties available in CSS: position:static...</span>
                                        </div>
                                    </div><!--./ge-message-->

                                    <div class="row-fluid ge-message">
                                        <div class="ge-user-image">
                                            <a href="#profile"><img src="temp-sample/ge-user.jpg" class="img img-rounded"></a>
                                        </div>
                                        <div class="ge-text">
                                            <a class="ge-name" href="#profile">Jean Claude</a><span class="ge-date">- 1 day ago</span>
                                            <span class="ge-block">This tutorial examines the different layout properties available in CSS: position:static ibla...</span>
                                        </div>
                                    </div><!--./ge-message-->

                                    <div class="row-fluid ge-message">
                                        <div class="ge-user-image">
                                            <a href="#profile"><img src="temp-sample/ge-me.jpg" class="img img-rounded"></a>
                                        </div>
                                        <div class="ge-text">
                                            <a class="ge-name" href="#profile">Jean Claude</a><span class="ge-date">- 1 day ago</span>
                                            <span class="ge-block">This tutorial examines the different layout properties available in CSS: position:static...</span>
                                        </div>
                                    </div><!--./ge-message-->

                                    <div class="row-fluid ge-message">
                                        <div class="ge-user-image">
                                            <a href="#profile"><img src="temp-sample/ge-user.jpg" class="img img-rounded"></a>
                                        </div>
                                        <div class="ge-text">
                                            <a class="ge-name" href="#profile">Jean Claude</a><span class="ge-date">- 1 day ago</span>
                                            <span class="ge-block">This tutorial examines the different layout properties available in CSS: position:static ibla...</span>
                                        </div>
                                    </div><!--./ge-message-->

                                    <div class="row-fluid ge-message">
                                        <div class="ge-user-image">
                                            <a href="#profile"><img src="temp-sample/ge-me.jpg" class="img img-rounded"></a>
                                        </div>
                                        <div class="ge-text">
                                            <a class="ge-name" href="#profile">Jean Claude</a><span class="ge-date">- 1 day ago</span>
                                            <span class="ge-block">This tutorial examines the different layout properties available in CSS: position:static...</span>
                                        </div>
                                    </div><!--./ge-message-->
                                </div>
                            </div><!--./ge-messagelist-->

                            <div class="row-fluid ge-input">
                                <div class="span12">
                                    <div class="row-fluid ge-message">
                                        <div class="span9 ge-text">
                                            <textarea placeholder="Your message ..."></textarea>
                                        </div>
                                        <div class="span3 ge-text">
                                            <a href="#fakelink" class="btn btn-mini btn-block">Add</a>
                                        </div>
                                    </div>
                                </div>
                            </div><!--./ge-input-->

                        </div>
                    </div><!--./ge-comments-->
                </div>
            </div><!--./ge-item messages-->
        </div>

    </div><!-- ./row -->
    
    
    <? /** ?>
    
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
                    
                    
                    <label class="checkbox inline pull-right">
                        <input id="bookmarkCheckbox" <?=$ad_is_bookmarked ? 'checked="checked"' : ''?> type="checkbox" data-toggle="checkbox"/>
                        Bookmark
                    </label>
                    
                    
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

    </div>
    
    <? /**/ ?>
    
</div><!--./ge-container-->