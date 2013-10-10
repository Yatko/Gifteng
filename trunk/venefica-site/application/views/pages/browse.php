<?

/**
 * Input params:
 * 
 * is_ajax: boolean
 * ads: array of Ad_model
 * last_ad_id: long
 * currentUser: User_model
 * categories: array of Category_model
 * selected_q: string
 * selected_category: long
 * selected_type: string
 */

if ( isset($is_ajax) && $is_ajax ) {
} else {
    $is_ajax = false;
}

if ( isset($ads) && is_array($ads) ) {
    $ads_size = count($ads);
} else {
    $ads_size = 0;
}
?>

<? if ( $is_ajax ): ?>
    <script langauge="javascript">
        lastAdId = <?=$last_ad_id?>;
        
    <? if( $ads_size < Browse::CONTINUING_AD_NUM ): ?>
        $('#load_more_btn').addClass('hide');
    <? endif; ?>
    
    </script>
<? else: ?>
    <script langauge="javascript">
        var lastAdId = <?=$last_ad_id?>;
        
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
            
            //var ids = $(".ge-ad-id[id]").map(function() {
            //    var id = this.id.split('_')[1];
            //    return parseInt(id, 10);
            //}).get();
            //var lastAdId = Math.min.apply(Math, ids);
            
            //var lastAdId = $(".ge-ad-id:last").attr("id");
            //lastAdId = lastAdId.split('_')[1];
            
            var $container = $('#boxContainer');
            var url = '<?=base_url()?>browse/ajax/get_more?lastAdId=' + lastAdId;
            var q = '<?=$selected_q?>';
            var category = '<?=$selected_category?>';
            var type = '<?=$selected_type?>';
            
            $.ajax({
                type: 'POST',
                url: url,
                dataType: 'html',
                cache: false,
                data: {
                    q: q,
                    category: category,
                    type: type
                }
            }).done(function(response) {
                var newElements = response;
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
            }).fail(function(data) {
                //TODO
            });
        }
        
        $(function() {
//            if ( $('#boxContainer').length > 0 ) {
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
//                        return ['<?=base_url()?>browse/ajax/get_more?lastAdId=' + lastAdId];
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
//            }
            
            $('#browse_search_form input[name=q]').keypress(function(e) {
                if (e.which === 13) {
                    $('#browse_search_form').submit();
                    return false;
                }
            });
            $('#browse_search_form select').change(function() {
                $(this).closest('form').trigger('submit');
            });
        });
    </script>
<? endif; ?>


<? /* this is required by the infinite scroll javascript library */ ?>
<a class="nextPage hide" href="#"></a>

<? if ( !$is_ajax ): ?>

<div class="row">
    <div class="span12">
        <div class="text-center">
            New Gifts are available daily at 12pm ET. Make someone happy tomorrow with yours!<br/><br/>
        </div>
    </div>
</div>

<div class="row hidden-phone">
    <!-- search -->
    <form action="<?=base_url()?>browse" method="post" id="browse_search_form">
    
    <div class="span10 offset1">
        <div class="ge-form">
            <div class="row-fluid">
                <div class="span4">
                    <div class="control-group">
                        <input type="text" name="q" placeholder="Find Friends & Search Gifts" class="span12" value="<?=$selected_q?>">
                    </div>
                </div>
                <div class="span4">
                    <div class="control-group">
                        <select name="category" class="select-block mbl select-info" data-size="10">
                            <option value="">All Categories</option>
                            <? foreach ($categories as $cat): ?>
                                <?
                                $category_id = $cat->id;
                                $category_name = $cat->name;
                                ?>

                                <option value="<?=$category_id ?>" <?=$selected_category == $category_id ? 'selected="selected"' : ''?>><?=$category_name ?></option> 
                            <? endforeach; ?>
                        </select>
                    </div>
                </div>
                <div class="span4">
                    <div class="control-group">
                        <select name="type" class="select-block mbl select-info">
                            <? foreach ( lang('browse_type_list') as $key => $value ): ?>
                                <option value="<?=$key?>" <?=$selected_type == $key ? 'selected="selected"' : ''?>><?=$value?></option> 
                            <? endforeach; ?>
                        </select>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- #/search -->
    
    </form>
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
                                <? $this->load->view('element/user', array('user' => $ad->creator, 'small' => true, 'size' => LIST_USER_IMAGE_SIZE)); ?>
                            </div>

                            <div class="ge-item">
                                <? $this->load->view('element/ad_item', array('ad' => $ad, 'canBookmark' => $can_bookmark, 'canComment' => $can_comment, 'canShare' => $can_share, 'size' => LIST_AD_IMAGE_SIZE)); ?>
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
    
    <? if( !empty($selected_q) ): ?>
        No results for "<?=$selected_q?>"
    <? else: ?>
        No gifts found
    <? endif; ?>
    
<? endif; ?>


<? if ( !$is_ajax ): ?>
        
        </div>
        <div id="loadingPage"></div>
    
        <? if( $ads_size >= Browse::STARTING_AD_NUM ): ?>
            <button onclick="load_more(this);" id="load_more_btn" class="btn btn-block btn-ge">VIEW MORE</button>
        <? else: ?>
            
        <? endif; ?>
        
    </div>
</div>
<? endif; ?>
