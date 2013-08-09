<?

/**
 * Input params:
 * 
 * ad: Ad_model
 * user_id: long
 */

$ad_id = $ad->id;
$is_owner = isOwner($user_id);

?>

<div class="span3 ge-box">
    <div class="well ge-well">
        <div class="row-fluid">
            <div class="span12">
                <div class="ge-item">

                    <? $this->load->view('element/ad_item', array('ad' => $ad)); ?>

<? if( $is_owner ): ?>

    <? if( $ad->sold ): ?>

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
                                Given to
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
                        <div class="span6">
                            <button onclick="ad_relist(<?=$ad_id?>);" type="button" class="btn btn-small btn-block btn-ge">RELIST</button>
                        </div>
                        <div class="span6">
                            <button onclick="startAdDeleteModal(this, <?=$ad_id?>);" type="button" class="ge-ad btn btn-small btn-block">DELETE</button>
                        </div>
                    </div>

        <? if( $ad->requests != null && is_array($ad->requests) && count($ad->requests) > 0 ): ?>

                    <div class="row-fluid ge-text ge-description ge-user-image ge-action">

                    <? foreach( $ad->requests as $request ): ?>
                        <?
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
        $imgUrl = $ad->getImageUrl();
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
                        <div class="span4">
                            <button onclick="shareOnFacebook('<?=$title?>', '<?=$itemUrl?>', '<?=$imgUrl?>');" class="btn btn-mini btn-block btn-social-facebook link ge-icon-facebook"></button>
                        </div>
                        <div class="span4">
                            <button onclick="shareOnTwitter('<?=$title?>', '<?=$itemUrl?>', '<?=$imgUrl?>');" class="btn btn-mini btn-block btn-social-twitter link ge-icon-twitter"></button>
                        </div>
                        <div class="span4">
                            <button onclick="shareOnPinterest('<?=$title?>', '<?=$itemUrl?>', '<?=$imgUrl?>');" class="btn btn-mini btn-block btn-social-pinterest link ge-icon-pinterest"></button>
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
                                Given to
                                <span class="fui-arrow-left"></span>
                            </p>
                        </div>
                    </div>

                    <div class="row-fluid ge-text ge-description ge-user-image ge-action">
                        <div class="span4">
                            <img onclick="request_view(<?=$request_id?>);" src="<?=$requestor_img?>" class="img img-rounded link">
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
                            <p class="text-center">Recipient selected</p>
                        </div>
                    </div><!--./ge-text ge-description-->

                    <div class="row-fluid ge-text ge-description ge-user-image ge-action">
                        <div class="span4">
                            <img onclick="request_view(<?=$request_id?>);" src="<?=$requestor_img?>" class="img img-rounded link">
                        </div>
                        <div class="span8">
                            <div class="row-fluid">
                                <button onclick="request_cancel(this, 'giving', <?=$request_id?>, <?=$ad_id?>, <?=$user_id?>);" type="button" class="ge-request btn btn-small btn-block">Decline Request</button>
                            </div>
                            <div class="row-fluid">
                                <button onclick="request_send(<?=$request_id?>, <?=$ad_id?>, <?=$user_id?>);" type="button" class="btn btn-small btn-block btn-ge">Mark Gifted</button>
                            </div>
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

                        <div class="span4"><img onclick="request_view(<?=$request_id?>);" src="<?=$requestor_img?>" class="img img-rounded link"></div>
                    <? endforeach; ?>

                    </div><!--./ge-action-->

    <? endif; ?>

<? endif; ?>

                </div><!--./ge-item-->
            </div>
        </div>
    </div>
</div><!--./ge-box-->