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
            Top Giftengers
        </div>
    </div>
</div>

<? if ( isset($users) && is_array($users) && count($users) > 0 ): ?>
    
    <div class="row">
    <? for ( $iii = 4; $iii > 0; $iii-- ): ?>
    <div class="span3">
    
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
        
        <div class="ge-user-image">
            <a href="<?= $profile_link ?>"><img src="<?= $img ?>" class="img img-rounded"></a>
        </div>
        <div class="ge-detail">
            <div class="ge-name">
                <a href="<?= $profile_link ?>"><?= $name ?></a>
            </div>
            <? if ($joined != ''): ?>
                <div class="ge-age">Giftenger since <?= $joined ?></div>
            <? endif; ?>
            <? if ($location != ''): ?>
                <div class="ge-location"><?= $location ?></div>
            <? endif; ?>
            <? if ($points != ''): ?>
                <div class="ge-points">
                    Generosity Score <?= $points ?>
                </div>
            <? endif; ?>
        </div>
    <? endforeach; ?>
    
    </div>
    <? endfor; ?>
    </div>

<? endif; ?>
