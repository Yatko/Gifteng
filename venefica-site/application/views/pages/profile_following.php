<? /** ?>
    <? if( $followings_num > 0 ): ?>
    <div class="following">
        <div class="title">Following</div>
        <? foreach( $followings as $user ): ?>
            <?
            $user_img = $user->getAvatarUrl();
            $user_name = $user->getFullName();
            $user_joined = $user->getJoinDate();
            $user_location = $user->getLocation();

            if ( trim($user_name) == '' ) $user_name = '&nbsp;';
            if ( trim($user_location) == '' ) $user_location = '&nbsp;';
            ?>

            <div class="profileBox">
                <div class="profileImage" style="background: url(<?=$user_img?>);"></div>
                <div class="details">
                    <div class="username"><?=safe_content($user_name)?></div>
                    <div class="age">Giftenger since <?=$user_joined?></div>
                    <div class="location"><?=$user_location?></div>
                </div>
            </div>
        <? endforeach; ?>
    </div>
    <? endif; ?>
<? /**/ ?>
