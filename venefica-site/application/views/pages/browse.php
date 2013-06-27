<? if ( isset($is_ajax) && $is_ajax ): ?>
    <?
    $boxContainer_exists = true;
    ?>
    
    <!-- on ajax call there is no need for javascripts -->
<? else: ?>
    <?
    $boxContainer_exists = false;
    ?>
    
    <script langauge="javascript">
        function comment(element, adId) {
            var $commentAdId = $("#comment_post_form input[name=commentAdId]");
            $commentAdId.val(adId);
            
            $('#commentContainer').modal('show');
        }
        
        $(function() {
            if ( $('#boxContainer').length > 0 ) {
                var $container = $('#boxContainer');
                $container.imagesLoaded(function() {
                    $container.masonry({
                        itemSelector: '.ge-ad-item-box',
                        isAnimated: false
                    });
                });
                $container.infinitescroll({
                    navSelector: ".nextPage:last",
                    nextSelector: "a.nextPage:last",
                    itemSelector: '.ge-ad-item-box',
                    //debug: true,
                    loading: {
                        finishedMsg: 'No more pages to load.',
                        msgText: 'Loading...',
                        img: 'http://i.imgur.com/6RMhx.gif',
                        selector: '#loadingPage'
                    },
                    path: function(page) {
                        if ( $(".ge-ad-id:last").length === 0 ) {
                            return;
                        }
                        
                        var lastAdId = $(".ge-ad-id:last").attr("id");
                        lastAdId = lastAdId.split('_')[1];
                        return ['<?=base_url()?>browse/ajax/get_more?lastAdId=' + lastAdId];
                    },
                    prefill: true
                }, function(newElements) {
                    if ( $(newElements).length === 0 ) {
                        return;
                    }
                    
                    var $newElems = $(newElements).css({opacity: 0});
                    $newElems.imagesLoaded(function() {
                        $newElems.animate({opacity: 1});
                        $container.masonry('appended', $newElems, true); 
                    });
                });
            }
            
            $('#addCommentBtn').click(function() {
                var $commentAdId = $("#comment_post_form input[name=commentAdId]");
                var $commentText = $("#comment_post_form textarea[name=commentText]");
                var adId = $commentAdId.val();
                
                $.ajax({
                    type: 'POST',
                    url: '<?=base_url()?>ajax/comment',
                    dataType: 'json',
                    cache: false,
                    data: {
                        commentAdId: $commentAdId.val(),
                        commentText: $commentText.val()
                    }
                }).done(function(response) {
                    if ( !response || response == '' ) {
                        //TODO: empty result
                    } else if ( response.<?=AJAX_STATUS_ERROR?> ) {
                        //TODO
                    } else if ( response.<?=AJAX_STATUS_RESULT?> ) {
                        $commentAdId.val('');
                        $commentText.val('');
                        
                        var num_comments = response.<?=AJAX_STATUS_RESULT?>;
                        $('.ad_comment_' + adId).text(num_comments);
                        
                        $('#commentContainer').modal('hide');
                    } else {
                        //TODO: unknown response received
                    }
                }).fail(function(data) {
                    //TODO
                });
            });
        });
    </script>
<? endif; ?>


<!-- this is required by the infinite scroll javascript library -->
<a class="nextPage hide" href="#"></a>

<? if ( !$boxContainer_exists ): ?>


<div id="commentContainer" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-body">
        
        <label class="control-label" for="fieldset">
            <blockquote>
                <p>
                    Comment
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                </p>
            </blockquote>
        </label>
        
        <?=form_open('/ajax/comment', array('id' => 'comment_post_form'))?>
        
            <input type="hidden" name="commentAdId" value=""/>

            <div class="row-fluid ge-message ge-input ge-text">
                <div class="span9">
                    <textarea name="commentText" placeholder="Your comment ..."></textarea>
                </div>
                <div class="span3">
                    <a id="addCommentBtn" class="btn btn-mini btn-block">Add</a>
                </div>
            </div>
        
        <?=form_close()?>
        
    </div>
</div>


<div class="container ge-topspace">
    <div class="row ge-tile-view ge-browse">

<div id="boxContainer" class="transitions-enabled infinite-scroll clearfix">

<? endif; ?>

    
    <? if ( isset($ads) && is_array($ads) && count($ads) > 0 ): ?>
        <?
        $user_avatar_img = $user->getAvatarUrl();
        
        if ( trim($user_avatar_img) == '' ) $user_avatar_img = BASE_PATH.'temp-sample/ge-user.jpg';
        ?>
        
        <? foreach ($ads as $ad): ?>
            <?
            $is_owner = $ad->owner;
            //$is_in_followers = $ad->creator->inFollowers;
            $is_in_followings = $ad->creator->inFollowings;
            
            $creator_username = $ad->creator->name;
            $creator_img = $ad->getCreatorAvatarUrl();
            $creator_id = $ad->creator->id;
            $creator_name = $ad->getCreatorFullName();
            $creator_joined = $ad->getCreatorJoinDate();
            $creator_location = $ad->getCreatorLocation();
            $creator_points = $ad->getCreatorPoints();
            
            $ad_is_bookmarked = $ad->inBookmarks;
            $ad_id = $ad->id;
            $ad_img = $ad->getImageUrl();
            $ad_title = $ad->title;
            $ad_subtitle = $ad->subtitle;
            //$ad_description = $ad->description;
            //$ad_location = $ad->getLocation();
            
            $ad_distance = null;
            if (
                $user->address != null &&
                $user->address->latitude &&
                $user->address->longitude &&
                $ad->address != null &&
                $ad->address->latitude &&
                $ad->address->longitude
            ) {
                $ad_distance = distance_haversine(
                    $user->address->latitude,
                    $user->address->longitude,
                    $ad->address->latitude,
                    $ad->address->longitude
                );
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
            
            $creator_name = safe_content($creator_name);
            $ad_title = safe_content($ad_title);
            $ad_title_as_parameter = safe_parameter($ad_title);
            $ad_subtitle = safe_content($ad_subtitle);
            //$ad_description = safe_content($ad_description);
            
            if ( trim($ad_subtitle) != '' ) $ad_subtitle = $ad_subtitle.' % off';
            ?>
            
            <div class="ge-ad-item-box">
            
            <div class="span4 ge-box">
                <div class="well ge-well">
                    
                    <div class="ge-ad-id hide" id="ad_<?=$ad_id?>"></div>
                    
                    <div class="row-fluid">
                        <div class="span12">
                            
                            <div class="ge-user">
                                <div class="ge-user-image">
                                    <a href="<?=base_url()?>profile/<?=$creator_username?>"><img src="<?=$creator_img?>" class="img img-rounded"></a>
                                </div>
                                <div class="ge-detail">
                                    <div class="ge-name">
                                        <a href="<?=base_url()?>profile/<?=$creator_username?>"><?=$creator_name?></a>
                                        <? if(!$is_owner): ?>
                                            <? if( $is_in_followings ): ?>
                                                <span onclick="follow_unfollow(<?=$creator_id?>)" class="user_<?=$creator_id?> ge-user-follow label label-small label-ge active link">Unfollow</span>
                                            <? else: ?>
                                                <span onclick="follow_unfollow(<?=$creator_id?>)" class="user_<?=$creator_id?> ge-user-unfollow label label-small label-ge active link">Follow</span>
                                            <? endif; ?>
                                        <? endif; ?>
                                    </div>
                                    <? if( $creator_joined != '' ): ?>
                                        <div class="ge-age">Giftenger since <?=$creator_joined?></div>
                                    <? endif; ?>
                                    <? if( $creator_location != '' ): ?>
                                        <div class="ge-location"><?=$creator_location?></div>
                                    <? endif; ?>
                                    <? if( $creator_points != '' ): ?>
                                        <div class="ge-points"><span class="label label-small"><?=$creator_points?></span></div>
                                    <? endif; ?>
                                </div>
                            </div>
                            
                            <div class="ge-item">	
                                <div class="row-fluid ge-item-image" style="background: url('<?=$ad_img?>');">
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
                                                    <button onclick="comment(this, <?=$ad_id?>)" class="btn btn-small btn-block btn-ge"><i class="fui-bubble"></i> <span class="ad_comment_<?=$ad_id?>"><?=$ad_num_comments?></span></button>
                                                </div>
                                                <div class="span4">
                                                    <button class="btn btn-small btn-block btn-ge"><i class="fui-export"></i> <?=$ad_num_shares?></button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div><!--./ge-item-image-->

                                <div class="row-fluid ge-item-text">
                                    <div class="row-fluid">
                                        <p class="ge-title">
                                            <i class="fui-tag"></i>
                                            <a href="<?=base_url()?>view/<?=$ad_id?>"><?=$ad_title?></a>
                                            <? if( $ad_subtitle != '' ): ?>
                                                <span class="ge-subtitle">
                                                    <em>&amp;</em>
                                                    <?=$ad_subtitle?>
                                                </span>
                                            <? endif; ?>
                                        </p>
                                        <? if( $ad_distance != null && $ad_distance != '' ): ?>
                                            <p class="ge-location"><i class="fui-location"></i> <?=$ad_distance?> mi</p>
                                        <? endif; ?>
                                    </div><!--/text-->
                                </div>
                            </div><!--./ge-item-->
                            
            <? if ( isset($ad->comments) && is_array($ad->comments) && count($ad->comments) > 0 ): ?>
                            
                            <div class="row-fluid ge-comments">
                                <div class="span12">
                                    <div class="row-fluid ge-messagelist">
                                        <div class="span12">
                                            
                <? foreach ( $ad->comments as $comment ): ?>
                    <?
                    $commentor_username = $comment->publisherName;
                    $commentor_img = $comment->getPublisherAvatarUrl();
                    $commentor_name = $comment->publisherFullName;
                    $comment_text = $comment->text;
                    $comment_since = $comment->getCreateDateHumanTiming();

                    if ( trim($commentor_img) == '' ) $commentor_img = BASE_PATH.'temp-sample/ge-user.jpg';
                    if ( trim($comment_since) != '' ) $comment_since = $comment_since . ' ago';
                    ?>
                    
                    <div class="row-fluid ge-message">
                        <div class="ge-user-image">
                            <a href="<?=base_url()?>profile/<?=$commentor_username?>"><img src="<?=$commentor_img?>" class="img img-rounded"></a>
                        </div>
                        <div class="ge-text">
                            <a class="ge-name" href="<?=base_url()?>profile/<?=$commentor_username?>"><?=$commentor_name?></a><span class="ge-date"><?=$comment_since?></span>
                            <span class="ge-block"><?=$comment_text?></span>
                        </div>
                    </div><!--./ge-message-->
                <? endforeach; ?>
                                            
                                        </div>
                                    </div><!--./ge-messagelist-->
                                </div>
                            </div><!--./ge-comments-->
            <? endif; ?>
                        
                        </div>
                    </div>
                    
                </div>
            </div>
            
            </div>
            
        <? endforeach; ?>
    <? endif; ?>
    
    
<? if ( !$boxContainer_exists ): ?>
</div>
                <div id="loadingPage"></div>
        
    </div>
</div>
<? endif; ?>
