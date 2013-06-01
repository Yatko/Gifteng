<?
$user_is_business = $user->businessAccount;
$ad_is_business = ($ad->type == Ad_model::ADTYPE_BUSINESS);
$ad_title = $ad->title;
$ad_subtitle = $ad->subtitle;
$ad_description = $ad->description;
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

<!--left side-->
<div class="span6">
    <div class="geBox">
        <div class="geItemBox geLargeBox">

            <!--image-->
            <div class="row-fluid geItemImage">
                <img src="temp-sample/user.jpg" class="img">
            </div><!--/image-->

            <!--text-->
            <div class="row-fluid geItemText">
                <p class="geSpacer"></p>
                <p class="geTitle">
                    <?=$ad_title?>

                    <? if( !empty($ad_subtitle) ): ?>
                        <em class="geSpacer"><span class="label label-info">Plus</span></em>
                        <span class="geSubTitle"><?=$ad_subtitle?> % off the bill</span>
                    <? endif; ?>
                </p>


                <? if( $ad_is_business ): ?>
                    <? if( !empty($ad_description) ): ?>
                    <p class="geTerms">
                        <em>Offer terms:</em>
                        <?=$ad_description?>
                    </p>
                    <? endif; ?>
                <? else: ?>
                    <? if( !empty($ad_description) ): ?>
                    <p class="geDescription">
                        <em>Description:</em>
                        <?=$ad_description?>
                    </p>
                    <? endif; ?>
                <? endif; ?>


                <p class="geDetails">
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
                </p>
                <p class="geRedeem">
                    <button class="btn btn-small" type="button">How to redeem</button>
                </p>
            </div><!--/text-->

            <!--space--><div class="row-fluid geSpacer"></div>

            <!--map-->
            <div class="row-fluid geItemMap">
                <div class="well"><!--TODO remove well and contents-->
                    <label>Loading maps...</label>
                    <div class="progress progress-ge">
                        <div class="bar" style="width: 60%;"></div>
                    </div>
                </div>
            </div><!--/map-->

            <!--space--><div class="row-fluid geSpacer"></div>

            <!--share-->
            <div class="row-fluid geItemShare">
                <div class="btn-toolbar">
                    <div class="btn-group">
                        <button class="btn">Share:</button>
                        <button class="btn btn-info"><i class="icon-geFacebook"></i></button>
                        <button class="btn btn-info"><i class="icon-geTwitter"></i></button>
                        <button class="btn btn-info"><i class="icon-gePinterest"></i></button>
                    </div>
                </div>
            </div><!--/share-->

            <!--space--><div class="row-fluid geSpacer"></div>

            <!--message-->
            <div class="row-fluid geItemMessage">
                <div class="well"><!--TODO remove well and contents-->

                    <!-- Appended Input-->
                    <div class="control-group">
                        <div class="controls">
                            <div class="input-append">
                                <input id="appendedtext" name="appendedtext" class="input-xlarge" placeholder="type your message..." type="text">
                                <span class="add-on"><i class="icon-pencil"></i></span>
                            </div>
                        </div>
                    </div>

                    <div class="alert alert-info">
                        <a class="close" data-dismiss="alert" href="#">×</a>
                        <h4 class="alert-heading">Agota C.</h4>
                        Best check yo self, you're not...
                    </div>
                    <div class="alert alert-success">
                        <a class="close" data-dismiss="alert" href="#">×</a>
                        <h4 class="alert-heading">Kalman V.</h4>
                        Not?
                    </div>
                </div>
            </div><!--/message-->

        </div><!--/geItemBox-->
    </div><!--/geBox-->
</div><!--/left side-->

<!--right side-->
<div class="span6">

    <? if( !$user_is_business ): ?>
    <!--request-->
    <div class="row-fluid">
        <div class="geBox geLargeBox">
            <div class="control-group">
                <div class="controls">
                    <button class="btn btn-large btn-block btn-ge" type="button">REQUEST GIFT</button>
                </div>
            </div>
        </div><!--/geBox-->		
    </div><!--/request-->
    <? endif; ?>

    <!--space--><div class="row-fluid geSpacer"></div>

    <!--user panel-->
    <div class="row-fluid">
        <div class="geBox">

            <!--user profile-->
            <div class="row-fluid">	
                <div class="geLargeBox">
                    <div class="row-fluid">
                        <!--geProfileBox-->
                        <div class="geProfileBox">
                            <div class="row-fluid">
                                <div class="span3">
                                    <img src="temp-sample/user.jpg" class="img-rounded geProfileImage">
                                </div>
                                <div class="span9">
                                    <div class="geUsername">Samuel Jackson</div>
                                    <div class="geAge">Giftenger since October 2012</div>
                                    <div class="geLocation">New York, NY</div>
                                    <div class="gePoints">1,206</div>
                                </div>
                            </div>
                        </div><!--/geProfileBox-->
                    </div>
                </div><!--/geLargebox-->	
            </div><!--/user profile-->	

            <!--info bar-->
            <div class="row-fluid">
                <div class="geBox6 geSmall">
                    <div class="row-fluid">
                        <div class="span3 offset6">
                            <div class="geTile">
                                Followers<p>4</p>
                            </div>
                        </div>
                        <div class="span3">
                            <div class="geTile">
                                Reviews<p>2</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!--/info bar-->

        </div><!--/geBox-->
    </div><!--/user panel-->

</div><!--/right side-->