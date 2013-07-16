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
            if ( $('#boxContainer').length > 0 ) {
                var $container = $('#boxContainer');
                $container.imagesLoaded(function() {
                    $container.masonry({
                        itemSelector: '.ge-ad-item-box',
                        //columnWidth: function(containerWidth) {
                        //    var width = $(window).width();
                        //    var col;
                        //    if ( width < 1200 && width >= 980 ) {
                        //        col = 240;
                        //    } else if ( width < 980 && width >= 768 ) {
                        //        col = 186;
                        //    } else {
                        //        col = 300;
                        //    }
                        //    return col;
                        //},
                        isAnimated: false,
                        isResizable: true
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
        });
    </script>
<? endif; ?>


<!-- this is required by the infinite scroll javascript library -->
<a class="nextPage hide" href="#"></a>

<? if ( !$boxContainer_exists ): ?>

<div class="row">
    <div class="ge-tile-view ge-browse">
        <div id="boxContainer" class="transitions-enabled infinite-scroll clearfix">

<? endif; ?>


<? if ( isset($ads) && is_array($ads) && count($ads) > 0 ): ?>
    <? foreach ($ads as $ad): ?>
        <?
        $ad_id = $ad->id;
        ?>

        <div class="ge-ad-item-box">
            <div class="span4 ge-box">
                <div class="well ge-well">
                    <div class="ge-ad-id hide" id="ad_<?=$ad_id?>"></div>
                    <div class="row-fluid">
                        <div class="span12">

                            <div class="ge-user">
                                <? $this->load->view('element/user', array('user' => $ad->creator, 'small' => true)); ?>
                            </div>

                            <div class="ge-item">
                                <? $this->load->view('element/ad_item', array('ad' => $ad, 'canBookmark' => true, 'canComment' => true, 'canShare' => true)); ?>
                                <? $this->load->view('element/ad_data', array('ad' => $ad, 'user' => $currentUser)); ?>
                            </div><!--./ge-item-->

                            <? $this->load->view('element/comments', array('comments' => $ad->comments, 'canComment' => false)); ?>
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
