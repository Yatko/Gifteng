<?

/**
 * Input params:
 * 
 * ad: Ad_model
 * currentUser: User_model
 * comments: array of Comment_model
 * isAdmin: boolean
 */

$user_is_business = $currentUser->businessAccount;
$ad_is_business = $ad->isBusiness();
$ad_title = $ad->getSafeTitle();
$ad_subtitle = $ad->getSafeSubtitle();
$ad_description = $ad->getSafeDescription();
$creator_phone_number = $ad->creator->phoneNumber;

if ( !empty($ad->expires) && $ad->expires ) {
    $ad_expire = $ad->getExpireDate();
} else {
    $ad_expire = 'never';
}

if ( $ad_is_business ) {
    $ad_needs_reservation = $ad->needsReservation;
    if ( empty($ad_needs_reservation) ) $ad_needs_reservation = false;

    $ad_available_days = $ad->availableDays;

    if ( !empty($ad->availableAllDay) && $ad->availableAllDay ) {
        $ad_available_time = "all day";
    } else {
        $ad_available_time = $ad->availableFromTime . ' to ' . $ad->availableToTime;
    }
}

?>

!!! NOT YET IMPLEMENTED !!!

<!--left side-->
<div class="span6">
    <div class="ge-box">
        <div class="ge-item">

            <!--image-->
            <div class="row-fluid">
            	<div class="ge-item-image">
                	<img src="temp-sample/user.jpg" class="img">
                </div>
            </div><!--/image-->

            <!--text-->
                <div class="ge-text ge-description">
	            <div class="row-fluid">
	                <div class="ge-spacer"></div>
	                <div class="ge-title">
	                    <?=$ad_title?>
	
	                    <? if( !empty($ad_subtitle) ): ?>
	                        <em class="geSpacer"><span class="label label-info">Plus</span></em>
	                        <span class="geSubTitle"><?=$ad_subtitle?> % off the bill</span>
	                    <? endif; ?>
	                </div>
	
	
	                <? if( $ad_is_business ): ?>
	                    <? if( !empty($ad_description) ): ?>
	                    <div class="ge-description">
	                        <em>Offer terms:</em>
	                        <?=$ad_description?>
	                    </div>
	                    <? endif; ?>
	                <? else: ?>
	                    <? if( !empty($ad_description) ): ?>
	                    <div class="ge-description">
	                        <em>Description:</em>
	                        <?=$ad_description?>
	                    </div>
	                    <? endif; ?>
	                <? endif; ?>
	
	
	                <div class="ge-details">
	                <ul>
	                    <? if( $ad_is_business ): ?>
	                        <? if( $ad_needs_reservation ): ?>
	                        <li>Appointment/reservation needed.</li>
	                        <? endif; ?>
	
	                        <li><em>Available: </em>Monday, Tuesday, XXX</li>
	                        <li><em>Between: </em><?=$ad_available_time?></li>
	                    <? endif; ?>
	
	                    <li><em>Expires: </em><?=$ad_expire?></li>
	                    <li><em>Phone number: </em><?=$creator_phone_number?></li>
	                </ul>
	                </div>
                        <div class="control-group">
                          <div class="controls">
	                    <button class="btn btn-small" type="button">How to redeem</button>
	                   </div>
	                </div>
	            </div><!--/text-->
                </div>

            <!--space--><div class="row-fluid ge-spacer"></div>

            <!--map-->
            <div class="row-fluid">
            	<div class="ge-map">
	                <div class="well"><!--TODO remove well and contents-->
	                    <label>Loading maps...</label>
	                    <div class="progress progress-ge">
	                        <div class="bar" style="width: 60%;"></div>
	                    </div>
	                </div>
                </div>
            </div><!--/map-->

            <!--space--><div class="row-fluid ge-spacer"></div>

            <!--share-->
            <div class="row-fluid">
                <div class="btn-toolbar">
                    <div class="btn-group">
                        <button class="btn">Share:</button>
                        <button class="btn btn-info"><i class="icon-geFacebook"></i></button>
                        <button class="btn btn-info"><i class="icon-geTwitter"></i></button>
                        <button class="btn btn-info"><i class="icon-gePinterest"></i></button>
                    </div>
                </div>
            </div><!--/share-->

            <!--space--><div class="row-fluid ge-spacer"></div>

            <!--message-->
            <div class="row-fluid">
                <div class="ge-subject">
                    <a class="ge-title">Messages</a>
                </div><!--./ge-subject-->
            </div>

            <div class="row-fluid">
                <div class="ge-conversation">
                    <div class="span12">
                    
                    <? foreach ($messages as $message): ?>
                        
                        <? $this->load->view('element/message', array('message' => $message, 'showTitle' => true, 'showDelete' => true, 'currentUser' => $currentUser)); ?>
                        
                    <? endforeach; ?>

                    </div>
                </div><!--./ge-conversation-->
            </div><!--/message-->

        </div><!--/geItemBox-->
    </div><!--/geBox-->
</div><!--/left side-->

<!--right side-->
<div class="span6">

    <? if( !$user_is_business ): ?>
    <!--request-->
    <div class="row-fluid">
        <div class="ge-box">
            <div class="control-group">
                <div class="controls">
                    <button class="btn btn-large btn-block btn-ge" type="button">REQUEST GIFT</button>
                </div>
            </div>
        </div><!--/geBox-->		
    </div><!--/request-->
    <? endif; ?>

    <!--space--><div class="row-fluid ge-spacer"></div>

    <!--user panel-->
    
	<div class="ge-user">
            <? $this->load->view('element/user', array('user' => $ad_creator, 'canEdit' => false, 'small' => true)); ?>
	</div><!--./ge-user-->

</div><!--/right side-->