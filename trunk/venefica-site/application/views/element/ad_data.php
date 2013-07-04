<?

/**
 * Input params:
 * 
 * ad: Ad_model
 * user: User_model
 */

$title = $ad->title;
$subtitle = $ad->subtitle;
$view_link = $ad->getViewUrl();

$distance = null;
if (
    $user->address != null &&
    $user->address->latitude &&
    $user->address->longitude &&
    $ad->address != null &&
    $ad->address->latitude &&
    $ad->address->longitude
) {
    $distance = distance_haversine(
        $user->address->latitude,
        $user->address->longitude,
        $ad->address->latitude,
        $ad->address->longitude
    );
}

$title = safe_content($title);
//$title_as_parameter = safe_parameter($title);
$subtitle = safe_content($subtitle);

if ( trim($subtitle) != '' ) $subtitle = $subtitle.' % off';

?>

<div class="row-fluid ge-text ge-description">
    <div class="row-fluid">
        <p class="ge-title">
            <i class="fui-tag"></i>
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
