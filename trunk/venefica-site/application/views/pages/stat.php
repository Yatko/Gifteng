<?

/**
 * Input params:
 * 
 * users: array of User_model
 */

?>

	<div class="row">
	    <div class="span12">
	        <div class="text-center ge-bottomspace">
	            Top Giftengers
	        </div>
	    </div>
	</div>

<? if ( isset($users) && is_array($users) && count($users) > 0 ): ?>
    
    <div class="row">
    	<div class="ge-profile ge-detail-view">
    <? for ( $iii = 4; $iii > 0; $iii-- ): ?>
    		<div class="span4">
    			<div class="well ge-well">
    <? foreach ($users as $index => $user): ?>
        <?
        if ( ($index + $iii) % 4 != 0 ) {
            continue;
        }
        
        $name = $user->getFullName();
        $profile_link = $user->getProfileUrl();
        $img = $user->getAvatarUrl();
        $joined = $user->getJoinDateHumanTiming();
        $location = $user->getLocation();
        $points = $user->getPoints(false);
        ?>
					<div class="row-fluid">
						<div class="ge-user">
							<div class="span12">
								<div class="ge-user-image">
									<a href="<?= $profile_link ?>"><img src="<?= $img ?>" alt="" class="img img-rounded"></a>
								</div>
								<div class="ge-detail">
									<div class="ge-name"><a href="<?= $profile_link ?>"><?= $name ?></a></div>
								<? if ($joined != ''): ?>
							        <div class="ge-age">Giftenger since <?= $joined ?></div>
							    <? endif; ?>
							    <? if ($location != ''): ?>
							        <div class="ge-location"><?= $location ?></div>
							    <? endif; ?>
							    <? if ($points != ''): ?>
							        <div class="ge-points">
							        	<span class="label">Generosity Score <?= $points ?></span>
							        	<span class="label label-ge inactive">Follow</span>					            
							        </div>
							    <? endif; ?>
							</div>
						</div>
					</div>
    <? endforeach; ?>
    			</div>
    		</div>
    <? endfor; ?>
    	</div>
    </div>

<? endif; ?>
