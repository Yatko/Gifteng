<?

/**
 * Input params:
 * 
 * ad: Ad_model
 * user_id: long
 */

$ad_id = $ad->id;
$is_owner = isOwner($user_id);
$revision = $ad->revision;
$approval = $ad->approval;
$ad_can_request = $ad->canRequest;

$has_approval = false;
if ( $approval != null ) {
    $has_approval = true;
}

$inactive = false;
if ( $is_owner ) {
    if ( !$ad->online ) {
        $inactive = false;
    } elseif ( $ad->sold ) {
        $inactive = true;
    } elseif ( $ad->expired ) {
        $inactive = false;
    } elseif ( !$ad->hasActiveRequest() ) {
        $inactive = false;
    } elseif ( $ad->hasSentRequest() ) {
        $inactive = true;
    }
} else {
    if ( $ad->expired ) {
        $inactive = true;
    } else if ( !$ad_can_request ) {
        $inactive = true;
    }
}

?>

<div class="ge-ad-item-box <?=($inactive ? 'ge-inactive' : 'ge-active')?>">
<div class="ge-box">
    <div class="well ge-well">
        <div class="row-fluid">
            <div class="span12">
                <div class="ge-item">

                    <? $this->load->view('element/ad_item', array('ad' => $ad, 'size' => LIST_AD_IMAGE_SIZE, 'show_num_comments' => true)); ?>

<? if( $is_owner ): ?>

    <? if( !$ad->online ): ?>
        
        <? if( $ad->approved ): ?>
                    
                    <div class="row-fluid ge-text ge-description">
                        <div class="span12">
                            <p class="text-center">
                                <span class="fui-arrow-right"></span>
                                Approved
                                <span class="fui-arrow-left"></span>
                            </p>
                        </div>
                    </div>
                    
        <? elseif ( !$has_approval ): ?>
                    
                    <div class="row-fluid ge-text ge-description">
                        <div class="span12">
                            <p class="text-center">
                                <span class="fui-arrow-right"></span>
                                Approval Pending
                                <span class="fui-arrow-left"></span>
                            </p>
                        </div>
                    </div>
                    
        <? else: ?>
                    
                    <div class="row-fluid ge-text ge-description">
                        <div class="span12">
                            <p class="text-center">
                                <span class="fui-arrow-right"></span>
                                Gift declined
                                <span class="fui-arrow-left"></span>
                            </p>
                        </div>
                    </div>
                    
                    <div class="row-fluid ge-text ge-description ge-action">
                        <div class="span8">
                            <button onclick="startApprovalModal(<?=$ad_id?>, <?=$revision?>);" type="button" class="btn btn-small btn-block btn-ge">View Reason</button>
                        </div>
                    </div>
                    
        <? endif; ?>
                    
    <? elseif( $ad->sold ): ?>

        <?
        $accepted_request = $ad->getAcceptedRequest();
        if ( $accepted_request != null ) {
            $requestor_img = $accepted_request->getUserAvatarUrl();
        } else {
            $requestor_img = DEFAULT_USER_URL;
        }
        ?>

                    <div class="row-fluid ge-text ge-description">
                        <div class="span12">
                            <p class="text-center">
                                <span class="fui-arrow-right"></span>
                                Gifted
                                <span class="fui-arrow-left"></span>
                            </p>
                        </div>
                    </div>

                    <div class="row-fluid ge-text ge-description ge-user-image ge-action">
                        <div class="span4">
                            <img src="<?=$requestor_img?>" class="img img-rounded">
                        </div>
                    </div>

    <? elseif( $ad->expired ): ?>
                    
                    <div class="row-fluid ge-text ge-description">
                        <div class="span12">
                            <p class="text-center">
                                <span class="fui-arrow-right"></span>
                                Expired
                                <span class="fui-arrow-left"></span>
                            </p>
                        </div>
                    </div>
                    
                    <div class="row-fluid ge-text ge-description">
                        <div class="span6">
                            
        <? if ( $ad->canRelist ): ?>
                            <button onclick="startAdRelistModal(this, <?=$ad_id?>, <?=$ad->canProlong ? 'true' : 'false'?>);" type="button" class="btn btn-small btn-block btn-ge">RELIST</button>
        <? endif; ?>
                            
                        </div>
                        <div class="span6">
                            <button onclick="startAdDeleteModal(this, <?=$ad_id?>);" type="button" class="ge-ad btn btn-small btn-block">DELETE</button>
                        </div>
                    </div>

        <? if( $ad->requests != null && is_array($ad->requests) && count($ad->requests) > 0 ): ?>

                    <div class="row-fluid ge-text ge-description ge-user-image ge-action">

                    <? foreach( $ad->requests as $request ): ?>
                        <?
                        if ( $request->isExpired() ) {
                            continue;
                        }
                        
                        $requestor_img = $request->getUserAvatarUrl();
                        ?>

                        <div class="span4"><img src="<?=$requestor_img?>" class="img img-rounded inactive"></div>
                    <? endforeach; ?>

                    </div>

        <? endif; ?>

    <? elseif( !$ad->hasActiveRequest() ): ?>
                    
        <?
        $title = $ad->getSafeTitle();
        $itemUrl = $ad->getViewUrl();
        $imgUrl = $ad->getImageUrl(LIST_AD_IMAGE_SIZE);
        ?>
                    

                    <div class="row-fluid ge-text ge-description">
                        <div class="span12">
                            <p class="text-center">
                                <span class="fui-arrow-right"></span>
                                Share to receive requests
                                <span class="fui-arrow-left"></span>
                            </p>
                        </div>
                    </div>

                    <div class="row-fluid ge-text ge-description ge-action">
                        <div class="span4 mobile-one">
                            <button onclick="shareOnFacebook('<?=$title?>', '<?=$itemUrl?>', '<?=$imgUrl?>');" class="btn btn-mini btn-block btn-social-facebook link fui-facebook"></button>
                        </div>
                        <div class="span4 mobile-one">
                            <button onclick="shareOnTwitter('<?=$title?>', '<?=$itemUrl?>', '<?=$imgUrl?>');" class="btn btn-mini btn-block btn-social-twitter link fui-twitter"></button>
                        </div>
                        <div class="span4 mobile-one">
                            <button onclick="shareOnPinterest('<?=$title?>', '<?=$itemUrl?>', '<?=$imgUrl?>');" class="btn btn-mini btn-block btn-social-pinterest link fui-pinterest"></button>
                        </div>
                    </div><!--./ge-action-->

    <? elseif( $ad->hasSentRequest() ): ?>

        <?
        $request = $ad->getSentRequest();
        $request_id = $request->id;
        $requestor_img = $request->getUserAvatarUrl();
        ?>

                    <div class="row-fluid ge-text ge-description">
                        <div class="span12">
                            <p class="text-center">
                                <span class="fui-arrow-right"></span>
                                Gifted
                                <span class="fui-arrow-left"></span>
                            </p>
                        </div>
                    </div>

                    <div class="row-fluid ge-text ge-description ge-user-image ge-action">
                        <div class="span4 mobile-one">
                            <img onclick="startRequestViewModal(<?=$request_id?>, 'giving', <?=$user_id?>);" src="<?=$requestor_img?>" class="img img-rounded link">
                        </div>
                    </div>

    <? elseif( $ad->hasAcceptedRequest() ): ?>

        <?
        $request = $ad->getAcceptedRequest();
        $request_id = $request->id;
        $requestor_img = $request->getUserAvatarUrl();
        ?>

                    <div class="row-fluid ge-text ge-description">
                        <div class="span12">
                            <p class="text-center">
                                <span class="fui-arrow-right"></span>
                                Recipient selected
                                <span class="fui-arrow-left"></span>
                            </p>
                        </div>
                    </div><!--./ge-text ge-description-->

                    <div class="row-fluid ge-text ge-description ge-user-image ge-action">
                        <div class="span4 mobile-one">
                            <img src="<?=$requestor_img?>" class="img img-rounded">
                            
                            <? /** ?>
                            <img onclick="startRequestViewModal(<?=$request_id?>, 'giving', <?=$user_id?>);" src="<?=$requestor_img?>" class="img img-rounded link">
                            <? /**/ ?>
                        </div>
                        <div class="span8">
                            <button onclick="startRequestViewModal(<?=$request_id?>, 'giving', <?=$user_id?>);" type="button" class="ge-request btn btn-small btn-block btn-ge">Action required</button>
                            
                            <? /** ?>
                            <div class="row-fluid">
                                <button onclick="request_cancel(this, 'giving', <?=$request_id?>, <?=$ad_id?>, <?=$user_id?>, null);" type="button" class="ge-request btn btn-small btn-block">Decline Request</button>
                            </div>
                            <div class="row-fluid">
                                <button onclick="request_send(this, <?=$request_id?>, <?=$ad_id?>, <?=$user_id?>);" type="button" class="btn btn-small btn-block btn-ge">Mark Gifted</button>
                            </div>
                            <? /**/ ?>
                        </div>
                    </div>

    <? else: ?>

                    <div class="row-fluid ge-text ge-description">
                        <div class="span12">
                            <p class="text-center">
                                <span class="fui-arrow-right"></span>
                                Select recipient
                                <span class="fui-arrow-left"></span>
                            </p>
                        </div>
                    </div><!--./ge-text ge-description-->

                    <div class="row-fluid ge-text ge-description ge-user-image ge-action">

                    <? foreach( $ad->requests as $request ): ?>
                        <?
                        if ( $request->isExpired() ) {
                            continue;
                        }

                        $request_id = $request->id;
                        $requestor_img = $request->getUserAvatarUrl();
                        ?>

                        <div class="span4 mobile-one"><img onclick="startRequestViewModal(<?=$request_id?>, 'giving', <?=$user_id?>);" src="<?=$requestor_img?>" class="img img-rounded link"></div>
                    <? endforeach; ?>

                    </div><!--./ge-action-->

    <? endif; ?>

<? endif; ?>

                </div><!--./ge-item-->
            </div>
        </div>
    </div>
</div><!--./ge-box-->
</div>