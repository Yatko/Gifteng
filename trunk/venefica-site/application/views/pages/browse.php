<?

/**
 * Input params:
 * 
 * is_ajax: boolean
 * ads: array of Ad_model
 * currentUser: User_model
 * query: string
 */

?>

<? if ( isset($is_ajax) && $is_ajax ): ?>
    <?
    $boxContainer_exists = true;
    
    //on ajax call there is no need for javascripts
    ?>
<? else: ?>
    <?
    $boxContainer_exists = false;
    ?>
    
    <script langauge="javascript">
        /**
        var vg = $("#boxContainer").vgrid({
            easing: "easeOutQuint",
            time: 500,
            delay: 20,
            fadeIn: {
                time: 300,
                delay: 50
            }
        });
        $(window).load(function(e) {
            vg.vgrefresh();
        });
        /**/
        
        function load_more(callerElement) {
            if ( $(".ge-ad-id:last").length === 0 ) {
                return;
            }
            
            if ( callerElement !== null ) {
                var $element = $(callerElement);
                $element.html('PLEASE WAIT ... LOADING GIFTS');
                $element.addClass('disabled');
                $element.attr("disabled", true);
            }
            
            var ids = $(".ge-ad-id[id]").map(function() {
                var id = this.id.split('_')[1];
                return parseInt(id, 10);
            }).get();
            var lastAdId = Math.min.apply(Math, ids);
            
            var $container = $('#boxContainer');
            var url = '<?=base_url()?>browse/ajax/get_more?lastAdId=' + lastAdId + '&q=<?=$query?>';
            
            $.get(url, function(newElements) {
                if ( newElements === null || $(newElements).length === 0 ) {
                    if ( callerElement !== null ) {
                        var $element = $(callerElement);
                        $element.addClass('hide');
                    }
                    
                    return;
                }
                $container.append($(newElements));
                
                if ( callerElement !== null ) {
                    var $element = $(callerElement);
                    $element.html('VIEW MORE');
                    $element.removeClass('disabled');
                    $element.removeAttr("disabled");
                }
                
                /**
                var items = $(newElements).fadeTo(0, 0);
                vg.prepend(items);
                vg.vgrefresh(null, null, null, function(){
                    items.fadeTo(300, 1);
		});
                /**/
            });
        }
        
        $(function() {
            if ( $('#boxContainer').length > 0 ) {
//                var $container = $('#boxContainer');
                
                /**
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
                /**/
                
//                $container.infinitescroll({
//                    navSelector: ".nextPage:last",
//                    nextSelector: "a.nextPage:last",
//                    itemSelector: '.ge-ad-item-box',
//                    //debug: true,
//                    loading: {
//                        finishedMsg: 'No more pages to load.',
//                        msgText: 'Loading...',
//                        img: 'http://i.imgur.com/6RMhx.gif',
//                        selector: '#loadingPage'
//                    },
//                    path: function(page) {
//                        if ( $(".ge-ad-id:last").length === 0 ) {
//                            return;
//                        }
//                        
//                        var lastAdId = $(".ge-ad-id:last").attr("id");
//                        lastAdId = lastAdId.split('_')[1];
//                        return ['<?=base_url()?>browse/ajax/get_more?lastAdId=' + lastAdId + '&q=<?=$query?>'];
//                    },
//                    prefill: true
//                }, function(newElements) {
//                    if ( newElements === null || $(newElements).length === 0 ) {
//                        return;
//                    }
//                    
//                    $container.append($(newElements));
//                    
//                    /**
//                    var $newElems = $(newElements).css({opacity: 0});
//                    $newElems.imagesLoaded(function() {
//                        $newElems.animate({opacity: 1});
//                        $container.masonry('appended', $newElems, true); 
//                    });
//                    /**/
//                });
            }
        });
    </script>
<? endif; ?>


<? /* this is required by the infinite scroll javascript library */ ?>
<a class="nextPage hide" href="#"></a>

<? if ( !$boxContainer_exists ): ?>

<div class="row">
    <div class="span12">
        <div class="text-center">
            New Gifts are available daily at 12pm ET. Make someone happy tomorrow with yours!<br/><br/>
        </div>
    </div>
</div>

<div class="row">
    <div class="ge-tile-view ge-browse">
        <div id="boxContainer" class="transitions-enabled infinite-scroll clearfix masonry">

<? endif; ?>


<? if ( isset($ads) && is_array($ads) && count($ads) > 0 ): ?>
    
    <div class="row">
    <? for ( $iii = 3; $iii > 0; $iii-- ): ?>
    <div class="span4">
    
    <? foreach ($ads as $index => $ad): ?>
        <?
        if ( ($index + $iii) % 3 != 0 ) {
            continue;
        }
        
        $ad_id = $ad->id;
        $ad_can_request = $ad->canRequest;
        $is_owner = $ad->owner;
        $can_bookmark = $ad_can_request ? true : false;
        $can_share = ($is_owner || $ad_can_request) ? true : false;
        $can_comment = ($is_owner || $ad_can_request) ? true : false;
        
        $inactive = false;
        if ( !$ad->owner && !$ad_can_request ) {
            $inactive = true;
        }
        ?>

        <div class="ge-ad-item-box masonry-brick <?=($inactive ? 'ge-inactive' : 'ge-active')?>">
            <div class="ge-box">
                <div class="well ge-well">
                    <div class="ge-ad-id hide" id="ad_<?=$ad_id?>"></div>

                    <div class="row-fluid">
                        <div class="span12">

                            <div class="ge-user">
                                <? $this->load->view('element/user', array('user' => $ad->creator, 'small' => true)); ?>
                            </div>

                            <div class="ge-item">
                                <? $this->load->view('element/ad_item', array('ad' => $ad, 'canBookmark' => $can_bookmark, 'canComment' => $can_comment, 'canShare' => $can_share)); ?>
                                <? $this->load->view('element/ad_data', array('ad' => $ad, 'currentUser' => $currentUser)); ?>
                            </div><!--./ge-item-->

                            <? $this->load->view('element/comments', array('comments' => $ad->comments, 'canComment' => false)); ?>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    <? endforeach; ?>

    </div>
    <? endfor; ?>
    </div>
    
<? else: ?>
    
    <? if( key_exists('q', $_GET) ): ?>
        No results for "<?=$_GET['q']?>"
    <? else: ?>
        There are no gifts!
    <? endif; ?>
    
<? endif; ?>


<? if ( !$boxContainer_exists ): ?>
        
        </div>
        <div id="loadingPage"></div>
    
        <? if( isset($ads) && is_array($ads) && count($ads) > 0 ): ?>
            <button onclick="load_more(this);" class="btn btn-block btn-ge">VIEW MORE</button>
        <? else: ?>
            
        <? endif; ?>
        
    </div>
</div>
<? endif; ?>
