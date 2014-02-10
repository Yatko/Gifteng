<?php

/**
 * Input params:
 * 
 * address: Address_model
 * image: string
 * bar_code: string
 */

$address_id = $address->id;
$address_detail = $address->name;

?>

<!--location-->
<div class="row-fluid">

    <div class="span6">
        <input id="address_<?=$address_id?>" type="text" class="span1 input-block small" placeholder="<?=$address_detail?>" readonly>
    </div>
    <div class="span2">
        <input name="quantity_<?=$address_id?>" value="<?=set_value('quantity_'.$address_id) ?>" type="text" class="span1 input-mini small" placeholder="0">
    </div>
    <div class="span2">
        <button class="btn btn-small file" for="image_<?=$address_id?>" type="button">
            <? if( $image && !is_empty($image) ): ?>
            <img src="<?=base_url()?>get_photo/<?=$image?>/30/30" />
            <? endif; ?>
            Add
        </button>
        <input type="file" name="image_<?=$address_id?>" id="image_<?=$address_id?>" />
        <input type="hidden" name="image_posted_<?=$address_id?>" value="<?=$image?>" />
    </div>
    <div class="span2">
        <button class="btn btn-small file" for="bar_code_<?=$address_id?>" type="button">
            <? if( $bar_code && !is_empty($bar_code) ): ?>
            <img src="<?=base_url()?>get_photo/<?=$bar_code?>/30/30" />
            <? endif; ?>
            Add
        </button>
        <input type="file" name="bar_code_<?=$address_id?>" id="bar_code_<?=$address_id?>" />
        <input type="hidden" name="bar_code_posted_<?=$address_id?>" value="<?=$bar_code?>" />
    </div>

</div><!--/location-->
