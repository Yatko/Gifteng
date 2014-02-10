<?

/**
 * Input params:
 * 
 * user: User_model
 * ratings: array of Rating_model
 */

?>

<? /** ?>
    <? if( $ratings_num > 0 ): ?>
    <div class="review">
        <div class="title">Reviews</div>
        <? foreach( $ratings as $rating ): ?>
            <?
            $from_user_img = $rating->getFromAvatarUrl();
            $from_user_name = $rating->getFromFullName();
            $rating_date = $rating->getRateDate();
            $rating_text = $rating->text;

            if ( trim($from_user_name) == '' ) $from_user_name = '&nbsp;';
            ?>

            <div class="profileBox">
                <div class="profileImage" style="background: url(<?=$from_user_img?>);"></div>
                <div class="details">
                    <div class="username"><?=safe_content($from_user_name)?></div>
                    <div class="age"><?=$rating_date?></div>
                </div>
                <div class="details">
                    <div><?=safe_content($rating_text)?></div>
                </div>
            </div>
        <? endforeach; ?>
    </div>
    <? endif; ?>
<? /**/ ?>
