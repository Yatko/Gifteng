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
                    itemSelector: '.box',
                    columnWidth: 0
                });
            });
            $container.infinitescroll({
                navSelector: "#nextPage:last",
                nextSelector: "a#nextPage:last",
                itemSelector: '.box',
                //debug: true,
                loading: {
                    finishedMsg: 'No more pages to load.',
                    msgText: 'Loading...',
                    img: 'http://i.imgur.com/6RMhx.gif',
                    selector: '#loading'
                },
                path: function(page) {
                    return ['<?=base_url()?>browse/ajax'];
                },
                prefill: true
            }, function(newElements) {
                var $newElems = $(newElements).css({opacity: 0});
                $newElems.imagesLoaded(function(){
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
<div id="boxContainer" class="transitions-enabled infinite-scroll clearfix">
<? endif; ?>

    
    <? if ( isset($ads) && is_array($ads) && count($ads) > 0 ): ?>
        <? foreach ($ads as $ad): ?>
            <?
            $creator_img = $ad->getCreatorAvatarUrl();
            $creator_name = $ad->getCreatorFullName();
            $creator_joined = $ad->getCreatorJoinDate();
            $creator_location = $ad->getCreatorLocation();

            if ( trim($creator_name) == '' ) $creator_name = '&nbsp;';
            if ( trim($creator_location) == '' ) $creator_location = '&nbsp;';

            $ad_id = $ad->id;
            $ad_img = $ad->getImageUrl();
            $ad_title = $ad->title;
            $ad_description = $ad->description;
            ?>

            <div class="box">
                <div class="container">
                    <div class="profileBox">
                        <div class="profileImage" style="background: url(<?=$creator_img?>); background-size:38px 38px;">
                        </div>
                        <div class="details">
                            <div><?=$ad_id?></div>
                            <div class="username"><?=$creator_name?></div>
                            <div class="age">Giftenger since <?=$creator_joined?></div>
                            <div class="location"><?=$creator_location?></div>
                        </div>
                    </div>
                </div>
                <div class="container">
                    <div class="itemBox">
                        <div class="thumbnail"><a href="#"><img src="<?=$ad_img?>" alt="img"></a></div>
                        <div class="title"><a href="#"><?=$ad_title?></a></div>
                        <div class="description"><?=$ad_description?></div>
                        <div class="location">??? miles</div>
                    </div>
                </div>
                <div class="container">
                    <div class="messageContainer">
            <? if ( isset($ad->comments) && is_array($ad->comments) && count($ad->comments) > 0 ): ?>
            <? foreach ( $ad->comments as $comment ): ?>
                <?
                $commentor_img = $comment->getPublisherAvatarUrl();
                $commentor_name = $comment->publisherFullName;

                $comment_text = $comment->text;
                ?>

                <div class="messageBox">
                    <div class="profileImage" style="background: url(<?=$commentor_img?>);background-size:38px 38px;">
                    </div>
                    <div class="details">
                        <span class="username"><?=$commentor_name?></span>&nbsp;
                        <span class="message"><?=$comment_text?></span>
                    </div>
                </div>
            <? endforeach; ?>
            <? endif; ?>
                        <div class="showMore"></div>
                    </div>
                </div>
            </div>
        <? endforeach; ?>
    <? endif; ?>
        
    
<? if ( !$boxContainer_exists ): ?>
</div>

<div id="loading"></div>
<? endif; ?>
