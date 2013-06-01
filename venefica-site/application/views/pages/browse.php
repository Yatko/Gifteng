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
        $(function() {
            var $container = $('#boxContainer');
            $container.imagesLoaded(function() {
                $container.masonry({
                    itemSelector: '.geItemBox',
                    isAnimated: false,
                    columnWidth: 0
                    /**
                    columnWidth: function( containerWidth ) {
                        var width = $(window).width();
                        var col = 300;
                        if ( width < 1200 && width >= 980 ) {
                            col = 240;
                        } else if ( width < 980 && width >= 768 ) {
                            col = 186;
                        }
                        return col;
                    }
                    /**/
                });
            });
            $container.infinitescroll({
                navSelector: "#nextPage:last",
                nextSelector: "a#nextPage:last",
                itemSelector: '.geItemBox',
                //debug: true,
                loading: {
                    finishedMsg: 'No more pages to load.',
                    msgText: 'Loading...',
                    img: 'http://i.imgur.com/6RMhx.gif',
                    selector: '#loadingPage'
                },
                path: function(page) {
                    var lastAdId = $("div.id:last").attr("id");
                    return ['<?=base_url()?>browse/ajax/' + lastAdId];
                },
                prefill: true
            }, function(newElements) {
                var $newElems = $(newElements).css({opacity: 0});
                $newElems.imagesLoaded(function() {
                    $newElems.animate({opacity: 1});
                    $container.masonry('appended', $newElems, true); 
                });
            });
        });
    </script>
<? endif; ?>


<!-- this is required by the infinite scroll javascript library -->
<a id="nextPage" href="#"></a>

<? if ( !$boxContainer_exists ): ?>
<div class="span12">
    <div class="row geBrowse">

<div id="boxContainer" class="transitions-enabled infinite-scroll clearfix">
<? endif; ?>

    
    <? if ( isset($ads) && is_array($ads) && count($ads) > 0 ): ?>
        <? foreach ($ads as $ad): ?>
            <?
            $creator_img = $ad->getCreatorAvatarUrl();
            $creator_name = $ad->getCreatorFullName();
            $creator_joined = $ad->getCreatorJoinDate();
            $creator_location = $ad->getCreatorLocation();
            $creator_points = "xxx";

            if ( trim($creator_name) == '' ) $creator_name = '&nbsp;';
            if ( trim($creator_location) == '' ) $creator_location = '&nbsp;';

            $ad_id = $ad->id;
            $ad_img = $ad->getImageUrl();
            $ad_title = $ad->title;
            $ad_subtitle = $ad->subtitle;
            $ad_description = $ad->description;
            $ad_location = $ad->getLocation();
            $ad_distance = "??? miles";
            ?>
            
            
            <!--item-->
            <div class="span3">
                <div class="geBox geItemBox">	
                    <!--user--><!--TODO make this the globally default user box (on every screen)-->
                    <div class="row-fluid geProfileBox">
                        <div class="span3">
                            <img src="<?=$creator_img?>" class="img-rounded geProfileImage">
                        </div>
                        <div class="span9">
                            <div class="geUsername"><?=safe_content($creator_name)?><span class="label geLabel-follow">Follow</span></div>
                            <div class="geAge">Giftenger since <?=$creator_joined?></div>
                            <div class="geLocation"><?=$creator_location?></div>
                            <div class="gePoints"><?=$creator_points?></div>
                        </div>
                    </div><!--/user-->

                    <!--image-->
                    <div class="row-fluid geItemImage">
                        <a href="<?=base_url()?>view/<?=$ad_id?>">
                        <img src="<?=$ad_img?>" class="img" alt="<?=safe_parameter($ad_title)?>">
                        </a>
                    </div><!--/image-->

                    <!--text-->
                    <div class="row-fluid geItemText">
                        <p class="geTitle">
                            <a href="<?=base_url()?>view/<?=$ad_id?>"><?=safe_content($ad_title)?></a>
                            <em class="geSpacer"><i class="icon-plus-sign icon-green"></i></em>
                            <span class="geSubTitle"><?=safe_content($ad_subtitle)?></span>
                        </p>
                        <p class="geDescription">
                            <em>Description: </em>
                            <?=safe_content($ad_description)?>
                        </p>
                        <p class="geLocation"><?=$ad_distance?></p>
                        <div class="id hidden" id="<?=$ad_id?>">ID: <?=$ad_id?></div>
                    </div><!--/text-->

            <? if ( isset($ad->comments) && is_array($ad->comments) && count($ad->comments) > 0 ): ?>
                    <!--messages-->
                    <div class="row-fluid geItemMessage">
                        <div class="span12">
                <? foreach ( $ad->comments as $comment ): ?>
                    <?
                    $commentor_img = $comment->getPublisherAvatarUrl();
                    $commentor_name = $comment->publisherFullName;

                    $comment_text = $comment->text;
                    ?>
                            
                            <div class="row-fluid geMessage">
                                <div class="span2">
                                    <img src="<?=$commentor_img?>" class="img-rounded geProfileImage">
                                </div>
                                <div class="span10">
                                    <span class="geUsername"><?=safe_content($commentor_name)?></span>&nbsp;
                                    <span><?=safe_content($comment_text)?></span>
                                </div>
                            </div><!--/message-->
                            
                <? endforeach; ?>
                        </div>
                    </div><!--/messages-->
            <? endif; ?>

                    <!--expand-->
                    <div class="row-fluid geBoxExpand"></div><!--/expand-->
                </div>
            </div><!--/item-->
            
        <? endforeach; ?>
    <? endif; ?>
    
    
<? if ( !$boxContainer_exists ): ?>
</div>
        
    </div>				
</div>

<div id="loadingPage"></div>
<? endif; ?>
