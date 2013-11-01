<?

/**
 * Input params:
 * 
 * is_new: boolean
 * is_modal: boolean
 * is_clone: boolean
 * adId: long
 * step: string
 * unique_id: string
 * ad: Ad_model
 * categories: array of Category_model
 * longitude: float
 * latitude: float
 * marker_longitude: float
 * marker_latitude: float
 * error: string
 */

if ( $is_modal ) {
    $form_action = '';
} else if ( $is_clone ) {
    $form_action = 'clone_post/member/' . $adId . ($is_modal ? '?modal' : '');
} else if ( $is_new ) {
    $form_action = 'post/member' . ($is_modal ? '?modal' : '');
} else {
    $form_action = 'edit_post/member/' . $adId . ($is_modal ? '?modal' : '');
}

?>

<script langauge="javascript">
    $(function() {
        init_map('post_map', 'post_longitude', 'post_latitude', 'post_marker_longitude', 'post_marker_latitude', true);
        
        $('.ge-post-image-btn').on('file_selected', function() {
            var $this = $(this);
            
            if ( get_file_size($('#image').get(0)) > <?=UPLOAD_FILE_MAX_SIZE?> ) {
                $(".ge-modal_header h3").html("<?=lang('validation_max_upload_size')?>");
                $this.html($this.attr('original_text'));
                return;
            }
            
            $('input[name=next_step]').val('<?=Post_member::STEP_START?>');
            submit_form('member_post_form');
        });
        
        disable_form_buttons_on_submit('member_post_form', '#member_post_form .ge-modal_footer button');
        disable_form_buttons_on_submit('member_post_form', '#postContainer .ge-modal_footer button');
        
        $('#member_post_form').on('submit', function(e) {
            var step = $('input[name=step]').val();
            if ( step === '<?=Post_member::STEP_PREVIEW?>' ) {
                var $submit = $('#member_post_submit');
                if ( $submit.length > 0 ) {
                    $submit.attr('value', 'Posting...');
                    $submit.html('Posting...');
                }
            }
        });
    });
</script>


<? if( !$is_modal ): ?>

<div class="row-fluid">
    <div class="span12 offset3">

<? endif; ?>
    
    <form <?=($form_action != '' ? 'action="' . base_url() . $form_action . '"' : '')?> method="post" id="member_post_form" enctype="multipart/form-data">
        <input type="hidden" name="step" value="<?=$step?>" />
        <input type="hidden" name="next_step" />
        <input type="hidden" name="unique_id" value="<?=$unique_id?>" />
        <input type="hidden" name="adId" value="<?=$adId?>" />
        
        
        <? if ($step == Post_member::STEP_START): ?>
            
            
            <?
            $message = isset($this->post_form) ? $this->post_form->error_string() : '';
            if ( $message == '' ) $message = 'Giving makes you live longer. Seriously, it\'s true.';
            
            $image = $ad->image;
            if ( isset($image) && $image ) {
                $image_link = $image->getDetectedImageUrl(IMAGE_TYPE_AD, POST_AD_IMAGE_SIZE);
            } else {
                $image_link = "";
            }
            
            if ( $is_new && is_empty($image_link) ) {
                $image_text = 'Add photo';
            } else {
                $image_text = 'Change photo';
            }
            ?>
            
            <div class="span6">
                <div class="well ge-well ge-form">
                    <div class="ge-modal_header">
                        <label class="control-label">
                            <h3><?=$message?></h3>
                        </label>
                    </div>
                    
                    <div class="row-fluid">
                        <div class="span12">
                            <div class="control-group">
                                <div class="controls">
                                    <button for="image" type="button" class="ge-post-image-btn btn btn-huge btn-block file">
                                        <?=$image_text?>
                                        <i class="fui-photo pull-right"></i>
                                    </button>
                                    <input name="image" id="image" type="file" />
                                </div>
                            </div>
                        </div>
                    </div><!--./upload-->

                    <div class="ge-item-image">
                        <div class="row-fluid">
                            <div class="span12">
                            
                            <? if( !is_empty($image_link) ): ?>
                                <img src="<?= $image_link ?>" class="ge-post-image img img-rounded file" for="image" />
                            <? else: ?>
                                <img src="<?=BASE_PATH?>temp-sample/ge-upload.png" class="ge-post-image noimage img img-rounded file" for="image" />
                            <? endif; ?>
                            
                            </div>
                        </div>
                    </div><!--./ge-item-image-->

                    <div class="ge-modal_footer">
                        <div class="row-fluid">
                            <div class="span12">
                                <div class="control-group control-form">
                                    <div class="controls">
                                        <? if( $is_modal ): ?>
                                            <button type="button" data-dismiss="modal" class="span2 mobile-one btn btn-huge"><i class="fui-cross"></i></button>
                                        <? endif; ?>
                                        <button type="button" onclick="submit_form('member_post_form');" class="span10 mobile-three btn btn-huge btn-ge pull-right">NEXT <i class="fui-arrow-right pull-right"></i></button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div><!--./ge well-->
            </div><!--./post-gift_1-->
            
            
        <? elseif ($step == Post_member::STEP_DETAILS): ?>
            
            
            <?
            $message = isset($this->post_form) ? $this->post_form->error_string() : '';
            if ( $message == '' ) $message = 'The More You Give, The More You Get. Promise! :)';
            
            $title = $ad->getSafeTitle();
            //$description = $ad->getSafeDescription();
            $description = $ad->description;
            $category = $ad->categoryId;
            $price = $ad->price;
            $zipCode = $ad->address->zipCode;
            $pickUp = $ad->getPickUpForFormElement();
            $freeShipping = $ad->getFreeShippingForFormElement();
            
            if ( empty($title) ) $title = hasElement($_POST, 'title') ? $_POST['title'] : null;
            if ( empty($description) ) $description = hasElement($_POST, 'description') ? $_POST['description'] : null;
            if ( empty($category) ) $category = hasElement($_POST, 'category') ? $_POST['category'] : null;
            if ( empty($price) ) $price = hasElement($_POST, 'price') ? $_POST['price'] : null;
            if ( empty($zipCode) ) $zipCode = hasElement($_POST, 'zipCode') ? $_POST['zipCode'] : null;
            if ( empty($pickUp) ) $pickUp = hasElement($_POST, 'pickUp') ? $_POST['pickUp'] : null;
            if ( empty($freeShipping) ) $freeShipping = hasElement($_POST, 'freeShipping') ? $_POST['freeShipping'] : null;
            ?>
            
            <div class="span6">
                <div class="well ge-well ge-form">
                    <div class="ge-modal_header">
                        <label class="control-label">
                            <h3><?=$message?></h3>
                        </label>
                    </div>
                    
                    <div class="row-fluid">
                        <div class="span12">
                            <div class="control-group large">
                                <div class="controls">
                                    <input id="title" name="title" value="<?=$title?>" type="text" maxlength="50" placeholder="Name your gift" class="span3" required="">
                                </div>
                            </div>
                        </div>
                    </div><!--./gift-->

                    <div class="row-fluid">
                    	<div class="ge-description">
	                        <div class="span12">
	                            <div class="control-group">
	                                <div class="controls">
	                                    <textarea id="description" name="description" rows="4" maxlength="1500" placeholder="Describe it ..."><?=$description?></textarea>
	                                </div>
	                            </div>
	                        </div>
                        </div>
                    </div><!--/description-->

                    <div class="row-fluid">
                        <div class="span12">
                            <select id="category" name="category" placeholder="Gift category" class="select-block mbl select-info" data-size="5">
                                <option value="">Select category</option>
                                <? foreach ($categories as $cat): ?>
                                    <?
                                    $category_id = $cat->id;
                                    $category_name = $cat->name;
                                    ?>

                                    <option value="<?=$category_id ?>" <?=$category == $category_id ? 'selected="selected"' : ''?>><?=$category_name ?></option> 
                                <? endforeach; ?>
                            </select>
                        </div>
                    </div><!--./select-category-->

                    <div class="row-fluid">
                        <div class="span6">
                            <div class="control-group">
                                <div class="controls">
                                    <div class="input-prepend span9">
                                        <span class="add-on">$</span>
                                        <input id="price" name="price" value="<?=$price?>" class="span2" type="text" placeholder="Current value" required="">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="span6">
                            <div class="control-group">
                                <div class="controls">
                                    <div class="input">
                                        <input id="zipCode" style="margin-top:0" name="zipCode" value="<?=$zipCode?>" class="span2" type="text" maxlength="5" placeholder="Your Zip code">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="row-fluid">
                        <div class="span12">
                            <div class="control-group">
                                <div class="controls">
                                    <label for="pickUp" class="checkbox inline">
                                        <input name="pickUp" id="pickUp" value="1" <?=$pickUp == '1' ? 'checked="checked"' : ''?> type="checkbox" data-toggle="checkbox">
                                        Local Pick up
                                    </label>
                                    <label for="freeShipping" class="checkbox inline">
                                        <input name="freeShipping" id="freeShipping" value="1" <?=$freeShipping == '1' ? 'checked="checked"' : ''?> type="checkbox" data-toggle="checkbox">
                                        Free Shipping
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div><!--./shipping-->

                    <div class="ge-modal_footer">
                        <div class="row-fluid">
                            <div class="span12">
                                <div class="control-group control-form">
                                    <div class="controls">
                                        <? if( $is_modal ): ?>
                                            <button type="button" data-dismiss="modal" class="span2 mobile-one btn btn-huge"><i class="fui-cross"></i></button>
                                        <? endif; ?>
                                        <button type="button" onclick="submit_form('member_post_form');" class="span10 mobile-three btn btn-huge btn-ge pull-right">NEXT <i class="fui-arrow-right pull-right"></i></button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div><!--./ge well-->
            </div><!--./post-gift_2-->
            
            
        <? elseif ($step == Post_member::STEP_MAP): ?>
            
            
            <?
            $message = isset($this->post_form) ? $this->post_form->error_string() : '';
            if ( $message == '' ) $message = 'Doesn\'t need to be perfect, just close enough';
            
            if ( empty($longitude) ) $longitude = hasElement($_POST, 'longitude') ? $_POST['longitude'] : $marker_longitude;
            if ( empty($latitude) ) $latitude = hasElement($_POST, 'latitude') ? $_POST['latitude'] : $marker_latitude;
            ?>
            
            <input id="post_marker_longitude" type="hidden" value="<?=$marker_longitude?>">
            <input id="post_marker_latitude" type="hidden" value="<?=$marker_latitude?>">
            
            <input id="post_longitude" name="longitude" type="hidden" value="<?=$longitude?>">
            <input id="post_latitude" name="latitude" type="hidden" value="<?=$latitude?>">
            
            <div class="span6">
                <div class="well ge-well ge-form">
                    <div class="ge-modal_header">
                        <label class="control-label">
                            <h3><?=$message?></h3>
                        </label>
                    </div>
                    
                    <div class="row-fluid ge-map">
                        <div class="span12">
                            <div id="post_map" class="map"></div>
                        </div>
                    </div><!--./ge-item-image-->

                    <div class="ge-modal_footer">
                        <div class="row-fluid">
                            <div class="span12">
                                <div class="control-group control-form">
                                    <div class="controls">
                                        <? if( $is_modal ): ?>
                                            <button type="button" data-dismiss="modal" class="span2 mobile-one btn btn-huge"><i class="fui-cross"></i></button>
                                        <? endif; ?>
                                        <button type="button" onclick="submit_form('member_post_form');" class="span10 mobile-three btn btn-huge btn-ge pull-right">PREVIEW <i class="fui-arrow-right pull-right"></i></button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div><!--./ge well-->
            </div><!--./post-gift_3-->
            
            
        <? elseif ($step == Post_member::STEP_PREVIEW): ?>
            
            
            <?
            $message = isset($this->post_form) ? $this->post_form->error_string() : '';
            if ( $message == '' ) $message = 'One last step! Make sure everything is correct.<br /><small>With every gift you give, your Generosity Score will increase!</small>';
            
            if ( $is_new ) {
                $submit_text = 'POST MY GIFT';
            } else {
                $submit_text = 'UPDATE MY GIFT';
            }
            ?>
            
            <div class="span6">
                <div class="well ge-well ge-form">
                    <div class="ge-modal_header">
                        <label class="control-label">
                            <h3><?=$message?></h3>
                        </label>
                    </div>
                    
                    <? $this->load->view('element/ad_preview', array('ad' => $ad, 'standalone' => false)); ?>
                    
                    <div class="ge-modal_footer">
                        <div class="row-fluid">
                            <div class="span12">
                                <div class="control-group control-form">
                                    <div class="controls">
                                        <button type="button" onclick="edit_post();" class="span3 btn btn-huge mobile-one">EDIT</button>
                                        <button type="button" onclick="submit_form('member_post_form');" id="member_post_submit" class="span9 btn btn-huge btn-ge pull-right mobile-three"><?=$submit_text?> <i class="fui-arrow-right hidden-phone pull-right"></i></button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div><!--./ge well-->
            </div><!--./post-gift_4-->
            
            
        <? elseif ($step == Post_member::STEP_POST): ?>
            
            
            <? if( !empty($error) ): ?>
                
                <div class="span6">
                    <div class="well ge-well ge-form">
                        <div class="ge-modal_header">
                            <label class="control-label">
                                <h3><?=$error?></h3>
                            </label>
                        </div>
                        
                        <div class="ge-modal_footer">
                            <div class="row-fluid">
                                <div class="span12">
                                    <div class="control-group control-form">
                                        <div class="controls">
                                            <? if( $is_modal ): ?>
                                                <button type="button" data-dismiss="modal" class="span4 btn btn-huge"><i class="fui-cross pull-left"></i>OK</button>
                                            <? endif; ?>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!--./ge well-->
                </div><!--./post-gift_5-->
                
            <? else: ?>
                
                <div class="span6">
                    <div class="well ge-well ge-form">
                        <div class="ge-modal_header">
                            <label class="control-label">
                                <h3>You're Awesome!</h3>
                            </label>
                        </div>
                        
                        <div class="row-fluid">
                            Give us few hours to review your gift. (But most likely it will be available for request tomorrow at 12:00 pm ET)
                        </div>
                        
                        <div class="ge-modal_footer">
                            <div class="row-fluid">
                                <div class="span12">
                                    <div class="control-group control-form">
                                        <div class="controls">
                                            
                                            <? if( $is_modal ): ?>
                                                
                                                <button type="button" data-dismiss="modal" class="span3 btn btn-huge mobile-one">OK</button>
                                                <? if( $is_new ): ?>
                                                    <button type="button" onclick="another_post();" class="span9 btn btn-huge btn-ge pull-right mobile-three"><i class="fui-arrow-right hidden-phone pull-right"></i>POST ANOTHER GIFT</button>
                                                <? endif; ?>
                                                
                                            <? else: ?>
                                                
                                                <? if( $is_new ): ?>
                                                    <a href="<?=base_url()?>post" class="span9 btn btn-huge btn-ge pull-right"><i class="fui-arrow-right pull-right hidden-phone"></i>POST ANOTHER GIFT</a>
                                                <? endif; ?>
                                                
                                            <? endif; ?>
                                            
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!--./ge well-->
                </div><!--./post-gift_5-->
                
            <? endif; ?>
            
            
        <? endif; ?>
        
    </form>

<? if( !$is_modal ): ?>

    </div>
</div>

<? endif; ?>


<? if( $is_modal ): ?>
<script language="javascript">
    initPostModal();
</script>
<? endif; ?>
