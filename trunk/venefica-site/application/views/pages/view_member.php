<script langauge="javascript">
    function show_description_rest(callerElement) {
        $('.description_separator').addClass('hide');
        $('.description_rest').removeClass('hide').css('display', 'inline');
        $(callerElement).addClass('hide');
    }
    function show_comment_rest(callerElement, commentId) {
        $('.comment_' + commentId + '_separator').addClass('hide');
        $('.comment_' + commentId + '_rest').removeClass('hide').css('display', 'inline');
        $(callerElement).addClass('hide');
    }
    
    function request(callerElement, adId) {
        var $requestAdId = $("#request_post_form input[name=requestAdId]");
        $requestAdId.val(adId);

        $('#requestContainer').modal('show');
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
                    if ( !response || response === '' ) {
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
                    if ( !response || response === '' ) {
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
$is_in_followings = $ad->creator->inFollowings;
$user_is_business = $user->businessAccount;
$ad_is_business = $ad->isBusiness();
$ad_is_online = $ad->isOnline();
$ad_is_bookmarked = $ad->inBookmarks;
$ad_is_requested = $ad->requested;

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
$creator_profile_link = base_url().'profile/'.$creator_username;
$creator_joined = $ad->getCreatorJoinDateHumanTiming(); //$ad->getCreatorJoinDate();
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


<? if( $is_owner ): ?>
    
    
    
<? elseif ( !$user_is_business && !$ad_is_requested ): ?>
    
    <script language="javascript">
        $(function() {
            $('#requestBtn').on('requested', function() {
                $('#ad_control').addClass('hide');
            });
        });
    </script>
    
    <div id="requestContainer" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-body">

    <? if( $ad_is_business ): ?>
        
        <?=form_open('/ajax/request', array('id' => 'request_post_form'))?>
            <input type="hidden" name="requestAdId" value="<?=$ad_id?>"/>
            <input type="hidden" name="requestText" value=""/>

            <label class="control-label" for="fieldset">
                <blockquote>
                    <p>Yippy! You are about to request a gift! Because its a "Company Gift" your request is instantly accepted.</p>
                </blockquote>
            </label>
            <fieldset>
                <div class="row-fluid">
                    <div class="span12">
                        <div class="control-group control-form">
                            <div class="controls">
                                <button type="button" class="span4 btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
                                <button id="requestBtn" type="button" class="span8 btn btn-ge">Send Request</button>
                            </div>
                        </div>
                    </div>
                </div>
            </fieldset>

        <?=form_close()?>

    <? else: ?>
    
        <script language="javascript">
            $(function() {
                $('#request_step_1_submit').click(function() {
                    $('#request_step_1').addClass('hide');
                    $('#request_step_2').removeClass('hide');
                });
                $('#requestContainer').on('hidden', function() {
                    $('#request_step_1').removeClass('hide');
                    $('#request_step_2').addClass('hide');
                });
            });
        </script>
        
        <?=form_open('/ajax/request', array('id' => 'request_post_form'))?>
            <input type="hidden" name="requestAdId" value="<?=$ad_id?>"/>

            <div id="request_step_1">
                <label class="control-label" for="fieldset">
                    <blockquote>
                        <p>Yippy! You are about to request a gift! Because it’s a "Member’s Gift" your request will be pending until the giver accepts it.</p>
                    </blockquote>
                </label>
                <fieldset>
                    <div class="row-fluid">
                        <div class="span12">
                            <div class="control-group control-form">
                                <div class="controls">
                                    <button type="button" id="request_step_1_submit" class="span8 pull-right btn btn-ge" >Got it!</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </fieldset>
            </div>

            <div id="request_step_2" class="hide">
                <fieldset>
                    <div class="row-fluid ge-input">
                        <div class="span12">
                            <div class="row-fluid">
                                <div class="span12 ge-text">
                                    <textarea name="requestText" placeholder="Your message ...">I would love to have your gift! Please accept my request!</textarea>
                                </div>
                            </div>
                        </div>
                    </div><!--./ge-input-->
                    <div class="row-fluid">
                        <div class="span12">
                            <div class="control-group control-form">
                                <div class="controls">
                                    <button type="button" class="span4 btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
                                    <button id="requestBtn" type="button" class="span8 btn btn-ge">Send Request</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </fieldset>
            </div>

        <?=form_close()?>

    <? endif; ?>
        
        </div>
    </div>

<? endif; ?>



<div class="container ge-topspace">
    
    <div class="row">

        <div class="ge-detail-view">
            <div class="span6 ge-item">
                <div class="well ge-well ge-item">

                    <div class="row-fluid ge-user">
                        <div class="span12">
                            <div class="ge-user-image">
                                <a href="<?=$creator_profile_link?>"><img src="<?=$creator_img?>" class="img img-rounded"></a>
                            </div>
                            <div class="ge-detail">
                                <div class="ge-name"><a href="<?=$creator_profile_link?>"><?=$creator_name?></a></div>
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
                                        <span onclick="follow_unfollow(<?=$creator_id?>);" class="user_<?=$creator_id?> ge-user-follow label label-small label-ge link">Unfollow</span>
                                    <? else: ?>
                                        <span onclick="follow_unfollow(<?=$creator_id?>);" class="user_<?=$creator_id?> ge-user-unfollow label label-small label-ge link">Follow</span>
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
                                            <button onclick="bookmark(this, <?=$ad_id?>);" class="btn btn-small btn-block btn-ge"><i class="fui-star-2"></i> <span class="ad_bookmark_<?=$ad_id?>"><?=$ad_num_bookmarks?></span></button>
                                        <? endif; ?>
                                    </div>
                                    <div class="span4">
                                        <button class="btn btn-small btn-block btn-ge disabled"><i class="fui-bubble"></i> <span class="ad_comment_<?=$ad_id?>"><?=$ad_num_comments?></span></button>
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
                    
                    <? if( $is_owner || (!$user_is_business && !$ad_is_requested) ): ?>
                    
                    <div id="ad_control" class="row-fluid ge-action">
                        <div class="span12">
                            <div class="control-group">
                                <div class="controls">
                                    <? if( $is_owner ): ?>
                                        <button class="btn btn-large btn-ge btn-block" type="button">EDIT GIFT</button>
                                    <? elseif ( !$user_is_business && !$ad_is_requested ): ?>
                                        <button onclick="request(this, <?=$ad_id?>);" class="btn btn-large btn-ge btn-block" type="button">REQUEST GIFT</button>
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
                    <div class="row-fluid ge-comments">
                        <div class="span12">

                            <div class="row-fluid ge-messagelist">
                                <div class="span12">
                                    
<? if ( isset($comments) && is_array($comments) && count($comments) > 0 ): ?>

    <? foreach ( $comments as $comment ): ?>
        <?
        $commentor_username = $comment->publisherName;
        $commentor_img = $comment->getPublisherAvatarUrl();
        $commentor_name = $comment->publisherFullName;
        $commentor_profile_link = base_url().'profile/'.$commentor_username;
        $comment_id = $comment->id;
        $comment_text = $comment->text;
        $comment_since = $comment->getCreateDateHumanTiming();

        if ( trim($commentor_img) == '' ) $commentor_img = BASE_PATH.'temp-sample/ge-user.jpg';
        if ( trim($comment_since) != '' ) $comment_since = $comment_since . ' ago';
        
        if ( strlen($comment_text) > COMMENT_MAX_LENGTH ) {
            $comment_text_rest = substr($comment_text, COMMENT_MAX_LENGTH);
            $comment_text = substr($comment_text, 0, COMMENT_MAX_LENGTH);
        } else {
            $comment_text_rest = '';
        }
        ?>
                                    
                                    <div class="row-fluid ge-message">
                                        <div class="ge-user-image">
                                            <a href="<?=$commentor_profile_link?>"><img src="<?=$commentor_img?>" class="img img-rounded"></a>
                                        </div>
                                        <div class="ge-text">
                                            <a class="ge-name" href="<?=$commentor_profile_link?>"><?=$commentor_name?></a><span class="ge-date"><?=$comment_since?></span>
                                            <span class="ge-block">
                                                <?=$comment_text?>
                                                
                                                <? if( $comment_text_rest != '' ): ?>
                                                    <span class="comment_<?=$comment_id?>_separator">...</span>
                                                    <span class="comment_<?=$comment_id?>_rest hide"><?=$comment_text_rest?></span>

                                                    <a class="text-note link" onclick="show_comment_rest(this, <?=$comment_id?>);">read more</a>
                                                <? endif; ?>
                                            </span>
                                        </div>
                                    </div><!--./ge-message-->
                                    
    <? endforeach; ?>

<? endif; ?>
                                    
                                </div>
                            </div><!--./ge-messagelist-->

                            <?=form_open('/ajax/comment', array('id' => 'comment_post_form'))?>
                            
                            <input type="hidden" name="commentAdId" value="<?=$ad_id?>"/>
                            
                            <div class="row-fluid ge-input">
                                <div class="span12">
                                    <div class="row-fluid ge-message">
                                        <div class="span9 ge-text">
                                            <textarea name="commentText" placeholder="Your message ..."></textarea>
                                        </div>
                                        <div class="span3 ge-text">
                                            <a id="addCommentBtn" class="btn btn-mini btn-block">Add</a>
                                        </div>
                                    </div>
                                </div>
                            </div><!--./ge-input-->
                            
                            <?=form_close()?>
                            
                        </div>
                    </div><!--./ge-comments-->
                </div>
            </div><!--./ge-item messages-->
        </div>

    </div><!-- ./row -->
    
</div><!--./ge-container-->