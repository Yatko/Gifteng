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
        function follow_unfollow(userId) {
            var elem = $('.user_' + userId)[0];
            var $elem = $(elem);
            if ( $elem.hasClass('ge-user-follow') ) {
                unfollow(userId);
            } else {
                follow(userId);
            }
        }
        function follow(userId) {
            $.ajax({
                type: 'POST',
                url: '<?=base_url()?>browse/ajax/follow?userId=' + userId,
                dataType: 'json',
                cache: false
            }).done(function(response) {
                if ( !response || response == '' ) {
                    //TODO: empty result
                } else if ( response.<?=Browse::AJAX_STATUS_ERROR?> ) {
                    //TODO
                } else if ( response.<?=Browse::AJAX_STATUS_RESULT?> ) {
                    $('.user_' + userId).each(function() {
                        var $this = $(this);
                        $this.removeClass('ge-user-unfollow');
                        $this.addClass('ge-user-follow');
                        //$this.removeClass('btn-ge');
                        //$this.addClass('btn-ge2');

                        $this.html('Unfollow');
                        $this.text('Unfollow');
                    });
                } else {
                    //TODO: unknown response received
                }
            }).fail(function(data) {
                //TODO
            });
        }
        function unfollow(userId) {
            $.ajax({
                type: 'POST',
                url: '<?=base_url()?>browse/ajax/unfollow?userId=' + userId,
                dataType: 'json',
                cache: false
            }).done(function(response) {
                if ( !response || response == '' ) {
                    //TODO: empty result
                } else if ( response.<?=Browse::AJAX_STATUS_ERROR?> ) {
                    //TODO
                } else if ( response.<?=Browse::AJAX_STATUS_RESULT?> ) {
                    $('.user_' + userId).each(function() {
                        var $this = $(this);
                        $this.removeClass('ge-user-follow');
                        $this.addClass('ge-user-unfollow');
                        //$this.removeClass('btn-ge2');
                        //$this.addClass('btn-ge');
                        
                        $this.html('Follow');
                        $this.text('Follow');
                    });
                } else {
                    //TODO: unknown response received
                }
            }).fail(function(data) {
                //TODO
            });
        }
        
        
        function comment(adId) {
            var $commentAdId = $("#comment_post_form input[name=commentAdId]");
            $commentAdId.val(adId);
            
            $('#commentContainer').modal('show');
        }
        
        
        $(function() {
            var $container = $('#boxContainer');
            
            $container.imagesLoaded(function() {
                $container.masonry({
                    itemSelector: '.ge-item',
                    isAnimated: false
                });
            });
            $container.infinitescroll({
                navSelector: ".nextPage:last",
                nextSelector: "a.nextPage:last",
                itemSelector: '.ge-item',
                //debug: true,
                loading: {
                    finishedMsg: 'No more pages to load.',
                    msgText: 'Loading...',
                    img: 'http://i.imgur.com/6RMhx.gif',
                    selector: '#loadingPage'
                },
                path: function(page) {
                    var lastAdId = $(".ge-ad-id:last").attr("id");
                    lastAdId = lastAdId.split('_')[1];
                    return ['<?=base_url()?>browse/ajax/get_more?lastAdId=' + lastAdId];
                },
                prefill: true
            }, function(newElements) {
                var $newElems = $(newElements).css({opacity: 0});
                $newElems.imagesLoaded(function() {
                    $newElems.animate({opacity: 1});
                    $container.masonry('appended', $newElems, true); 
                });
            });
            
            
            $('#addCommentBtn').click(function() {
                var $commentAdId = $("#comment_post_form input[name=commentAdId]");
                var $commentText = $("#comment_post_form textarea[name=commentText]");
                var adId = $commentAdId.val();
                
                $.ajax({
                    type: 'POST',
                    url: '<?=base_url()?>browse/ajax/comment',
                    dataType: 'json',
                    cache: false,
                    data: {
                        commentAdId: $commentAdId.val(),
                        commentText: $commentText.val()
                    }
                }).done(function(response) {
                    if ( !response || response == '' ) {
                        //TODO: empty result
                    } else if ( response.<?=Browse::AJAX_STATUS_ERROR?> ) {
                        //TODO
                    } else if ( response.<?=Browse::AJAX_STATUS_RESULT?> ) {
                        $commentAdId.val('');
                        $commentText.val('');
                        
                        var num_comments = response.<?=Browse::AJAX_STATUS_RESULT?>;
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
<a class="nextPage hidden" href="#"></a>

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
        
        <?=form_open('/browse/ajax/comment', array('id' => 'comment_post_form'))?>
        
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
        
        <div class="span12">
            <div class="row-fluid">

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
            $is_in_followers = $ad->creator->inFollowers;
            $is_in_followings = $ad->creator->inFollowings;
            
            $creator_img = $ad->getCreatorAvatarUrl();
            $creator_id = $ad->creator->id;
            $creator_name = $ad->getCreatorFullName();
            $creator_joined = $ad->getCreatorJoinDate();
            $creator_location = $ad->getCreatorLocation();
            $creator_points = '';
            
            $ad_id = $ad->id;
            $ad_img = $ad->getImageUrl();
            $ad_title = $ad->title;
            $ad_subtitle = $ad->subtitle;
            $ad_description = $ad->description;
            $ad_location = $ad->getLocation();
            
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
            $ad_description = safe_content($ad_description);
            
            if ( trim($ad_subtitle) != '' ) $ad_subtitle = $ad_subtitle.' % off';
            ?>
            
            <div class="span4 ge-item">
                <div class="well ge-well">
                    
                    <div class="ge-ad-id hidden" id="ad_<?=$ad_id?>"></div>
                    
                    <div class="row-fluid ge-user">
                        <div class="span12">
                            <div class="ge-user-image">
                                <img src="<?=$creator_img?>" class="img img-rounded">
                            </div>
                            <div class="ge-detail">
                                <div class="ge-name">
                                    <?=$creator_name?>
                                    <? if(!$is_owner): ?>
                                        <? if( $is_in_followings ): ?>
                                            <span onclick="follow_unfollow(<?=$creator_id?>)" class="user_<?=$creator_id?> ge-user-follow label label-small label-ge link">Unfollow</span>
                                        <? else: ?>
                                            <span onclick="follow_unfollow(<?=$creator_id?>)" class="user_<?=$creator_id?> ge-user-unfollow label label-small label-ge link">Follow</span>
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
                                    <div class="ge-points"><?=$creator_points?></div>
                                <? endif; ?>
                            </div>
                        </div>
                    </div><!--./ge-user-->
                    
                    <div class="row-fluid ge-item-image" style="background: url('<?=$ad_img?>');">
                        <div class="span12">
                            <div class="row-fluid">
                                <div class="span4">
                                    <button class="btn btn-small btn-block btn-ge"><i class="fui-star-2"></i> <?=$ad_num_bookmarks?></button>
                                </div>
                                <div class="span4">
                                    <button onclick="comment(<?=$ad_id?>)" class="btn btn-small btn-block btn-ge"><i class="fui-bubble"></i> <span class="ad_comment_<?=$ad_id?>"><?=$ad_num_comments?></span></button>
                                </div>
                                <div class="span4">
                                    <button class="btn btn-small btn-block btn-ge"><i class="fui-export"></i> <?=$ad_num_shares?></button>
                                </div>
                            </div>

                            <div class="row-fluid">
                                <div class="span12" style="height: 200px;"></div>
                            </div>
                        </div>
                    </div><!--./ge-item-image-->
                    
                    <div class="row-fluid ge-item-text">
                        <div class="span12">
                            <p class="ge-title">
                                <a href="<?=base_url()?>view/<?=$ad_id?>"><?=$ad_title?></a>
                                <? if( $ad_subtitle != '' ): ?>
                                    <em class="ge-spacer">&</em>
                                    <span class="ge-subtitle"><?=$ad_subtitle?></span>
                                <? endif; ?>
                            </p>
                        </div>
                        
                        <? /** ?>
                        <div class="span12">
                            <p class="ge-item-description">
                                <em>Description: </em>
                                <?=$ad_description?>
                            </p>
                        </div>
                        <? /**/ ?>
                        
                        <? if( $ad_distance != null && $ad_distance != '' ): ?>
                            <div class="span12">
                                <p class="ge-item-location"><?=$ad_distance?> miles</p>
                            </div>
                        <? endif; ?>
                    </div><!--/text-->
                    
                    <? if ( isset($ad->comments) && is_array($ad->comments) && count($ad->comments) > 0 ): ?>
                        <!--messages-->
                        <div class="row-fluid ge-messages">
                            <div class="span12">

                                <div class="row-fluid ge-messagelist">
                                <? foreach ( $ad->comments as $comment ): ?>
                                    <?
                                    $commentor_img = $comment->getPublisherAvatarUrl();
                                    $commentor_name = $comment->publisherFullName;
                                    $comment_text = $comment->text;
                                    $comment_since = $comment->getCreateDateHumanTiming();
                                    
                                    if ( trim($commentor_img) == '' ) $commentor_img = BASE_PATH.'temp-sample/ge-user.jpg';
                                    if ( trim($comment_since) != '' ) $comment_since = '- ' . $comment_since . ' ago';
                                    ?>

                                    <div class="row-fluid ge-message">
                                        <div class="ge-user-image">
                                            <img src="<?=$commentor_img?>" class="img img-rounded">
                                        </div>
                                        <div class="ge-text">
                                            <span class="ge-name"><?=$commentor_name?></span>
                                            <span class="ge-date"><?=$comment_since?></span>
                                            <span class="ge-block"><?=$comment_text?></span>
                                        </div>
                                    </div><!--./ge-message-->

                                <? endforeach; ?>
                                </div>

                                <div class="row-fluid ge-expand"></div>

                            </div>
                        </div><!--./ge-messages-->
                    <? endif; ?>
                    
                </div>
            </div><!--/item-->
            
            
            
        <? endforeach; ?>
    <? endif; ?>
    
    
<? if ( !$boxContainer_exists ): ?>
</div>
                <div id="loadingPage"></div>
        
            </div>
        </div>
        
    </div>
</div>
<? endif; ?>
