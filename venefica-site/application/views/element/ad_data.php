<?

/**
 * Input params:
 * 
 * ad: Ad_model
 * user: User_model
 */

$title = $ad->getSafeTitle();
$subtitle = $ad->getSafeSubtitle();
$view_link = $ad->getViewUrl();
$distance = getDistance($user, $ad);

//$title_as_parameter = safe_parameter($title);

if ( $subtitle != '' ) $subtitle = $subtitle . ' % off';

?>

<div class="row-fluid ge-text ge-description">
    <div class="row-fluid">
        <p class="ge-title">
            <i class="ge-icon-giftbox"></i>
            <a href="<?= $view_link ?>"><?= $title ?></a>
            <? if ($subtitle != ''): ?>
                <span class="ge-subtitle">
                    <em>&amp;</em>
                    <?= $subtitle ?>
                </span>
            <? endif; ?>
        </p>
        <? if ($distance != null && $distance != ''): ?>
            <p class="ge-location">
                <i class="fui-location"></i>
                <?= $distance ?> mi
            </p>
        <? endif; ?>
    </div>
</div>
