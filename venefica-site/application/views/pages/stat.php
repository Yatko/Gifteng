<?

/**
 * Input params:
 * 
 * users: array of User_model
 */

?>

<div class="row">
    <div class="span12">
        <div class="text-center">
            <h4>Top Giftengers</h4>
        </div>
    </div>
</div>

<? if ( isset($users) && is_array($users) && count($users) > 0 ): ?>
    
    <div class="row">
    	<div class="ge-profile ge-detail-view">
            
    <? for ( $iii = 3; $iii > 0; $iii-- ): ?>
            
        <div class="span4">
        <? foreach ($users as $index => $user): ?>
            <?
            if ( ($index + $iii) % 3 != 0 ) {
                continue;
            }

            $id = $user->id;
            $name = $user->getFullName();
            $profile_link = $user->getProfileUrl();
            $img = $user->getAvatarUrl(SELF_USER_IMAGE_SIZE);
            $joined = $user->getJoinDateHumanTiming();
            $location = $user->getLocation();
            $points = $user->getPoints(false);
            $is_owner = isOwner($user);
            $is_in_followings = $user->inFollowings;
            ?>

            <div class="well ge-well">
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
                                        
                                        <? if (!$is_owner): ?>
                                            <? if ($is_in_followings): ?>
                                                <span onclick="follow_unfollow(<?= $id ?>);" class="user_<?= $id ?> ge-user-follow label label-ge active link">Unfollow</span>
                                            <? else: ?>
                                                <span onclick="follow_unfollow(<?= $id ?>);" class="user_<?= $id ?> ge-user-unfollow label label-ge active link">Follow</span>
                                            <? endif; ?>
                                        <? endif; ?>
                                    </div>
                                <? endif; ?>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
        <? endforeach; ?>
        </div>
    
    <? endfor; ?>
            
    	</div>
    </div>

<? endif; ?>
