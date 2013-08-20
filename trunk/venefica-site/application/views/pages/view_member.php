<?

/**
 * Input params:
 * 
 * currentUser: User_model
 * ad: Ad_model
 * comments: array of Comment_model
 */

?>

<script langauge="javascript">
    function show_description_rest(callerElement) {
        $('.description_separator').addClass('hide');
        $('.description_rest').removeClass('hide').css('display', 'inline');
        $(callerElement).addClass('hide');
    }
    
    $(function() {
        init_map('view_map', 'view_longitude', 'view_latitude', 'view_marker_longitude', 'view_marker_latitude', false);
        
        $('.ge-ad').on('ad_deleted', function(event, adId) {
            window.location = '<?=base_url()?>profile?giving';
        });
        $('.ge-request').on('request_created', function(event, adId) {
            window.location.reload();
            //if ( $('#ad_control').length > 0 ) {
            //    $('#ad_control').addClass('hide');
            //}
        });
    });
</script>


<?

$distance = getDistance($currentUser, $ad);
$is_owner = $ad->owner;
$ad_is_business = $ad->isBusiness();
$ad_is_online = $ad->isOnline();
$ad_is_sold = $ad->sold;

if ( $comments == null || !is_array($comments) ) {
    $comments = array();
}

if ( $ad->statistics != null ) {
    $num_bookmarks = $ad->statistics->numBookmarks;
    $num_comments = $ad->statistics->numComments;
    $num_shares = $ad->statistics->numShares;
} else {
    $num_bookmarks = 0;
    $num_comments = 0;
    $num_shares = 0;
}

$ad_can_edit = $is_owner && !$ad->hasActiveRequest() && $num_bookmarks == 0 && $num_comments == 0 && $num_shares == 0;
$ad_can_delete = $is_owner && !$ad->hasActiveRequest();
$ad_can_request = $ad->canRequest;

$user_request = $ad->getRequestByUser($currentUser->id);

if ( $ad->address != null ) {
    $ad_longitude = $ad->address->longitude;
    $ad_latitude = $ad->address->latitude;
} else {
    $ad_longitude = 0;
    $ad_latitude = 0;
}

if ( $ad_is_sold || !$ad_can_request ) {
    $canComment = false;
} else {
    $canComment = true;
}

$ad_id = $ad->id;
$ad_title = $ad->getSafeTitle();
$ad_subtitle = $ad->getSafeSubtitle();
$ad_category = trim($ad->category);
$ad_price = trim($ad->price);
$ad_pickUp = $ad->pickUp;
$ad_freeShipping = $ad->freeShipping;
$ad_description = $ad->getSafeDescription();

$ad_price = trim($ad_price, '0'); //remove zeros from beginning/end of number ie. 00140.00000 becomes 140.
$ad_price = trim($ad_price, '.'); //remove decimal point if an integer ie. 140. becomes 140

if ( $ad_subtitle != '' ) $ad_subtitle = $ad_subtitle . ' % off the bill';
if ( $ad_price != '' ) $ad_price = '$' . $ad_price;

if ( strlen($ad_description) > DESCRIPTION_MAX_LENGTH ) {
    $ad_description_rest = substr($ad_description, DESCRIPTION_MAX_LENGTH);
    $ad_description = substr($ad_description, 0, DESCRIPTION_MAX_LENGTH);
} else {
    $ad_description_rest = '';
}

?>

<? if( $this->agent->is_referral() ): ?>

    <div class="row">
        <div class="span6">
            <a href="<?=$this->agent->referrer()?>" class="btn btn-large">Back</a>
            <br/><br/>
        </div>
    </div>

<? endif; ?>


<div class="row">
    <div class="ge-detail-view">
        
        <div class="span6">
            <div class="well ge-well">
                <div class="row-fluid">
                    <div class="span12">

                        <div class="ge-user">
                            <? $this->load->view('element/user', array('user' => $ad->creator, 'canEdit' => false, 'small' => false)); ?>
                        </div>

                        <div class="ge-item">
                            <? $this->load->view('element/ad_item', array('ad' => $ad, 'canBookmark' => true, 'canComment' => false, 'canShare' => true)); ?>
                        </div>

                    </div>
                </div>
            </div>
        </div><!--./ge-item main-->

        <div class="span6">
        	<div class="ge-item">
	            <div class="well ge-well">
	                
	                <div id="ad_control" class="row-fluid">
	                	<div class="ge-action">
		                    <div class="span12">
		                        <div class="control-group">
		                            <div class="controls">
		                
		                <? if( $is_owner ): ?>
		                    
		                    <?
		                    if ( $ad_can_edit ) {
		                        //there is no active request for this ad (and no comment/bookmark/share on it)
		                        $edit_js = 'onclick="startEditPostModal(' . $ad_id . ');"';
		                        $edit_class = 'class="btn btn-large btn-ge btn-block"';
		                    } else {
		                        //there is at least one active request (or at least one comment/bookmark/share on it)
		                        $edit_js = '';
		                        $edit_class = 'class="btn btn-large btn-block disabled"';
		                    }
                                    
                                    if ( $ad_can_delete ) {
                                        //there is no active request for this ad
                                        $delete_class = 'class="ge-ad btn btn-large btn-ge btn-block"';
		                        $delete_js = 'onclick="startAdDeleteModal(this, ' . $ad_id . ');"';
                                    } else {
                                        //there is at least one active request
                                        $delete_js = '';
		                        $delete_class = 'class="btn btn-large btn-block disabled"';
                                    }
		                    ?>
		                    
		                                <div class="span6">
		                                    <button <?=$edit_js?> <?=$edit_class?> type="button">EDIT GIFT</button>
		                                </div>
		                                <div class="span6">
		                                    <button <?=$delete_js?> <?=$delete_class?> type="button">DELETE GIFT</button>
		                                </div>
		                                
		                <? else: ?>
		                
		                    <?
                                    if ( $user_request != null ) {
                                        //there is a user request for this ad
                                        if ( $ad_is_sold ) {
                                            $request_js = '';
                                            $request_class = 'class="btn btn-large btn-block disabled"';
                                            $request_text = 'GIFT RECEIVED';
                                        } else if ( $user_request->isDeclined() ) {
                                            $request_js = '';
                                            $request_class = 'class="btn btn-large btn-block disabled"';
                                            $request_text = 'EXPIRED';
                                        } else {
                                            $request_js = '';
                                            $request_class = 'class="btn btn-large btn-block disabled"';
                                            $request_text = 'REQUEST SENT';
                                        }
                                    } else if ( $ad_is_sold ) {
                                        $request_js = '';
		                        $request_class = 'class="btn btn-large btn-block disabled"';
		                        $request_text = 'GIFTED';
                                    } else if ( $ad_can_request ) {
                                        $request_js = 'onclick="startRequestModal(this, \'' . ($ad_is_business ? 'business' : 'member') . '\', ' . $ad_id . ');"';
		                        $request_class = 'class="ge-request btn btn-large btn-ge btn-block"';
		                        $request_text = 'REQUEST GIFT';
                                    } else {
                                        $request_js = '';
		                        $request_class = 'class="btn btn-large btn-block disabled"';
		                        $request_text = 'INACTIVE';
                                    }
                                    
                                    /**
		                    if ( $ad_is_sold ) {
		                        $request_js = '';
		                        $request_class = 'class="btn btn-large btn-block disabled"';
		                        $request_text = 'GIFTED';
		                    } elseif ( $user_request != null && $user_request->isDeclined() ) {
		                        $request_js = '';
		                        $request_class = 'class="btn btn-large btn-block disabled"';
		                        $request_text = 'EXPIRED';
		                    } elseif ( $ad_can_request ) {
		                        $request_js = 'onclick="startRequestModal(this, \'' . ($ad_is_business ? 'business' : 'member') . '\', ' . $ad_id . ');"';
		                        $request_class = 'class="ge-request btn btn-large btn-ge btn-block"';
		                        $request_text = 'REQUEST GIFT';
		                    } else if ( $user_request != null ) {
		                        $request_js = '';
		                        $request_class = 'class="btn btn-large btn-block disabled"';
		                        $request_text = 'REQUEST SENT';
		                    } else {
		                        $request_js = '';
		                        $request_class = 'class="btn btn-large btn-block disabled"';
		                        $request_text = 'INACTIVE';
		                    }
                                    /**/
		                    ?>
		                                
		                                <button <?=$request_js?> <?=$request_class?> type="button"><?=$request_text?></button>
		                                
		                <? endif; ?>
		                                
		                            </div>
		                        </div>
		                    </div>
	                	</div>
	                </div><!--./ge-action-->
	                
	                
	                <div class="row-fluid">
	                    <div class="ge-text ge-description">
	                        <div class="ge-title">
	                            <?=$ad_title?>
	                            <? if( $ad_subtitle != '' ): ?>
	                                <span class="ge-subtitle">
	                                    <em>&amp;</em>
	                                    <?=$ad_subtitle?>
	                                </span>
	                            <? endif; ?>
	                        </div>
	
	                        <div class="ge-description">
	                            <em>Description:</em>
	                            <?=$ad_description?>
	
	                            <? if( $ad_description_rest != '' ): ?>
	                                <span class="description_separator">...</span>
	                                <span class="description_rest hide"><?=$ad_description_rest?></span>
	
	                                <a class="text-note link" onclick="show_description_rest(this);">read more</a>
	                            <? endif; ?>
	                        </div>
	
	                        <div class="ge-details">
		                        <ul>
		                            <li><em>Category: </em><?=$ad_category?></li>
		                            <li><em>Current value: </em><?=$ad_price?></li>
		                        </ul>
		                        <div class="row-fluid">
		                            <div class="span6">
		                                <label class="checkbox" for="pickup">
		                                    <input id="pickup" <?=(isset($ad_pickUp) && $ad_pickUp) ? 'checked="checked"' : ''?> type="checkbox" disabled="disabled" data-toggle="checkbox"/>
		                                    Pick up
		                                </label>
		                            </div>
		                            <div class="span6">
		                                <label class="checkbox" for="free_shipping">
		                                    <input id="free_shipping" <?=(isset($ad_freeShipping) && $ad_freeShipping) ? 'checked="checked"' : ''?> type="checkbox" disabled="disabled" data-toggle="checkbox"/>
		                                    Free shipping
		                                </label>
		                            </div>	
		                        </div>
	                        </div>
	                    </div>
	                </div><!--./ge-text-->
	
	
	                <? if( $ad_is_business && $ad_is_online ): ?>
	                
	                    <div class="row-fluid">
	                    	<div class="ge-map">
	                        	<img src="<?=BASE_PATH?>temp-sample/ge-map-online.png" class="img">
	                        </div>
	                    </div><!--./ge-map-->
	                    
	                <? else: ?>
	                    
	                    <input id="view_marker_longitude" type="hidden" value="<?=$ad_longitude?>">
	                    <input id="view_marker_latitude" type="hidden" value="<?=$ad_latitude?>">
	
	                    <div class="row-fluid">
	                    	<div class="ge-map">
	                        	<div id="view_map" class="map"></div>
	                        </div>
	                    </div><!--./ge-map-->
	                    
	                    <? if ($distance != null && $distance != ''): ?>
	                        <div class="ge-location">
	                        	<p class="ge-location"><i class="fui-location"></i> <?= $distance ?> mi</p>
	                        </div>
	                    <? endif; ?>
	                    
	                <? endif; ?>
	
	            </div>
        	</div>
        </div><!--./ge-item details-->

        <? if( (isset($comments) && is_array($comments) && count($comments) > 0) || $canComment ): ?>
        
        <div class="span6">
        	<div class="ge-item">
	            <div class="well ge-well">
	                <? $this->load->view('element/comments', array('comments' => $comments, 'ad' => $ad, 'canComment' => $canComment)); ?>
	            </div>
            </div>
        </div>
        
        <? endif; ?>
    </div>

</div><!-- ./row -->
