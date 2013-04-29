<? if( !$isLogged ): ?>
    You NEED to log in!
<? elseif( $user == null ): ?>
    User does not exists!
<? else: ?>
    <?
    $user_avatar_img = $user->getAvatarUrl();
    $user_full_name = $user->getFullName();
    $user_joined = $user->getJoinDate();
    
    if ( trim($user_full_name) == '' ) $user_full_name = '&nbsp;';
    
    ?>
    
    <div class="profile">
        <div class="profileBox">
            <div class="profileImage" style="background: url(<?=$user_avatar_img?>);"></div>
            <div class="details">
                <div class="username"><?=$user_full_name?></div>
                <div class="age">Giftenger since <?=$user_joined?></div>
            </div>
        </div>
        <div class="statistics">
            <div class="fl giving">Giving<br><?=$givings_num?></div>
            <div class="fl receiving">Receiving<br><?=$receivings_num?></div>
            <div class="fl favorite">Favorited<br><?=$favorites_num?></div>
            <div class="fl follower">Followers<br><?=$followers_num?></div>
            <div class="fl following">Followings<br><?=$followings_num?></div>
            <div class="fl review">Reviews<br><?=$reviews_num?></div>
            <div class="clear"></div>
        </div>
        
        <? if( $givings_num > 0 ): ?>
        <div class="giving">
            <div class="title">Giving</div>
            <? foreach( $givings as $ad ): ?>
                <?
                $ad_id = $ad->id;
                $ad_img = $ad->getImageUrl();
                $ad_title = $ad->title;
                ?>
                
                <div class="thumbnail"><a href="#"><img src="<?=$ad_img?>" alt="<?=safe_parameter($ad_title)?>"></a></div>
            <? endforeach; ?>
        </div>
        <? endif; ?>
        
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
        
        <? if( $followers_num > 0 ): ?>
        <div class="follower">
            <div class="title">Followers</div>
            <? foreach( $followers as $user ): ?>
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
        
        <? if( $reviews_num > 0 ): ?>
        <div class="review">
            <div class="title">Reviews</div>
            <? foreach( $reviews as $review ): ?>
                <?
                $from_user_img = $review->getFromAvatarUrl();
                $from_user_name = $review->getFromFullName();
                $review_date = $review->getReviewDate();
                $review_text = $review->text;
                
                if ( trim($from_user_name) == '' ) $from_user_name = '&nbsp;';
                ?>
                
                <div class="profileBox">
                    <div class="profileImage" style="background: url(<?=$from_user_img?>);"></div>
                    <div class="details">
                        <div class="username"><?=safe_content($from_user_name)?></div>
                        <div class="age"><?=$review_date?></div>
                    </div>
                    <div class="details">
                        <div><?=safe_content($review_text)?></div>
                    </div>
                </div>
            <? endforeach; ?>
        </div>
        <? endif; ?>
    </div>
<? endif; ?>
