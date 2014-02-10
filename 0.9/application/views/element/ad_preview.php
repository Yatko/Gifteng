<?php

/**
 * Input params:
 * 
 * ad: Ad_model
 * standalone: boolean (default: true)
 */

if ( !isset($standalone) ) $standalone = true;

if ( isset($ad->image) && $ad->image ) {
    $image_link = $ad->image->getDetectedImageUrl(IMAGE_TYPE_AD, POST_AD_IMAGE_SIZE);
} else if ( isset($ad) && $ad ) {
    $image_link = $ad->getImageUrl(POST_AD_IMAGE_SIZE);
} else {
    $image_link = "";
}

$title = $ad->getSafeTitle();
$description = $ad->getSafeDescription(true);
$category = $ad->category;
$price = $ad->price;
$pickUp = $ad->getPickUpForFormElement();
$freeShipping = $ad->getFreeShippingForFormElement();

?>

<? if( $standalone ): ?>
<div class="well ge-well ge-form">
<? endif; ?>

<div class="row-fluid">
    <div class="ge-item-image">
    <img src="<?= $image_link ?>" class="img img-rounded" />
    </div>
</div><!--./ge-item-image-->

<div id="item_description" class="row-fluid">
    <div class="ge-text">
        <div class="ge-title"><?=$title?></div>
        <div class="ge-description">
            <em>Description:</em> <?=$description?>
        </div>
        <div class="ge-details">
            <ul>
                <li><em>Category: </em><?=$category?></li>
                <li><em>Gift Value: $</em><?=$price?></li>
            </ul>
            <div class="row-fluid">
                <div class="span6 mobile-two">
                    <label class="checkbox">
                        <input <?=(isset($pickUp) && $pickUp == '1') ? 'checked="checked"' : ''?> type="checkbox" data-toggle="checkbox" disabled="disabled">
                        Pick up
                    </label>
                </div>
                <div class="span6 mobile-two">
                    <label class="checkbox">
                        <input <?=(isset($freeShipping) && $freeShipping == '1') ? 'checked="checked"' : ''?> type="checkbox" data-toggle="checkbox" disabled="disabled">
                        Free shipping
                    </label>	
                </div>	
            </div>
        </div>
    </div>
</div><!--./ge-text-->

<? if( $standalone ): ?>
</div>
<? endif; ?>
