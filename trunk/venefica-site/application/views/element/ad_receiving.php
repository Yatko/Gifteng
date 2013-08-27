<?

/**
 * Input params:
 * 
 * ad: Ad_model
 * request: Request_model
 * user_id: long
 */

$ad_id = $ad->id;
$request_id = $request->id;

$inactive = false;
if( $ad->expired || $request->isExpired() ) {
    $inactive = false;
} elseif ( $ad->sold ) {
    $inactive = true;
}

?>

<div class="ge-ad-item-box <?=($inactive ? 'ge-inactive' : 'ge-active')?>">
<div class="span3 ge-box">
    <div class="well ge-well">
        <div class="row-fluid">
            <div class="span12">
                <div class="ge-user">
                    <? $this->load->view('element/user', array('user' => $ad->creator, 'canEdit' => false, 'small' => true)); ?>
                </div>

                <div class="ge-item">	
                    <? $this->load->view('element/ad_item', array('ad' => $ad)); ?>

                    <div class="row-fluid ge-action">

<? if( $ad->expired || $request->isExpired() ): ?>

                        <?
                        if ( $ad->expired ) {
                            $status_text = 'Gift expired';
                        } else {
                            $status_text = 'Request declined';
                        }
                        ?>
                        
                        <div class="row-fluid ge-text ge-description">
                            <div class="span12">
                                <p class="text-center">
                                    <span class="fui-arrow-right"></span>
                                    <?=$status_text?>
                                    <span class="fui-arrow-left"></span>
                                </p>
                            </div>
                        </div>

                        <div class="span12">
                            <button onclick="request_hide(<?= $request_id ?>);" class="btn btn-small btn-block">
                                Delete Gift
                                <i class=" fui-trash pull-left"></i>
                            </button>
                        </div>

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
                                    Gift received
                                    <span class="fui-arrow-left"></span>
                                </p>
                            </div>
                        </div>
                        
                        <? /** ?>
                        <div class="row-fluid ge-text ge-description ge-user-image ge-action">
                            <div class="span4">
                                <img src="<?=$requestor_img?>" class="img img-rounded">
                            </div>
                        </div>
                        <? /**/ ?>

<? elseif( $request->isPending() ): ?>

                        <div class="row-fluid ge-text ge-description">
                            <div class="span12">
                                <p class="text-center">
                                    <span class="fui-arrow-right"></span>
                                    Request pending
                                    <span class="fui-arrow-left"></span>
                                </p>
                            </div>
                        </div>

                        <div class="span12">
                            <button onclick="startRequestCancelModal(this, 'receiving', <?=$request_id?>, <?=$ad_id?>, 0);" class="ge-request btn btn-small btn-block">
                                Cancel Request
                                <i class="fui-cross pull-left"></i>
                            </button>
                        </div>

<? elseif( $request->accepted ): ?>

                        <div class="row-fluid ge-text ge-description">
                            <div class="span12">
                                <p class="text-center">
                                    <span class="fui-arrow-right"></span>
                                    Request accepted
                                    <span class="fui-arrow-left"></span>
                                </p>
                            </div>
                        </div>

                        <div class="row-fluid">
                            <div class="span4">
                                <button onclick="startRequestViewModal(<?=$request_id?>, 'receiving', <?=$user_id?>);" class="btn btn-small btn-block btn-ge">
                                    <i class="fui-mail"></i>
                                </button>
                            </div>
                            <div class="span8">
                                <button onclick="request_receive(<?=$request_id?>, <?=$ad_id?>, <?=$user_id?>);" class="btn btn-small btn-block btn-ge">
                                    Gift Received
                                </button>
                            </div>
                        </div>

<? endif; ?>

                    </div><!--./ge-action-->
                </div><!--./ge-item-->
                
            </div>
        </div>
    </div>
</div><!--./ge-box-->
</div>
