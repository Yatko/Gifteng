<?

/**
 * Input params:
 * 
 * is_new: boolean
 * is_modal: boolean
 * adId: long
 * step: string
 * unique_id: string
 * image: string
 * title: string
 * description: string
 * category: long
 * price: float
 * zipCode: string
 * pickUp: 1 or 0
 * freeShipping: 1 or 0
 * categories: array of Category_model
 * longitude: float
 * latitude: float
 * marker_longitude: float
 * marker_latitude: float
 * error: string
 */

?>

<script langauge="javascript">
    $(function() {
        init_map('post_map', 'post_longitude', 'post_latitude', 'post_marker_longitude', 'post_marker_latitude', true);
        
        $('.ge-post-image-btn').on('file_selected', function() {
            var $this = $(this);
            
            if ( get_file_size($('#image').get(0)) > <?=UPLOAD_FILE_MAX_SIZE?> ) {
                $(".post_ajax_error").html("<div class='error'>Please limit photo size to 2MB !</div>");
                $this.html($this.attr('original_text'));
                return;
            }
            $('input[name=next_step]').val('<?=Post_member::STEP_START?>');
            submit_form('member_post_form');
        });
    });
</script>

<?
if ( $is_new ) {
    $form_action = '/post/member' . ($is_modal ? '?modal' : '');
} else {
    $form_action = '/edit_post/member/' . $adId . ($is_modal ? '?modal' : '');
}
?>

<? if( !$is_modal ): ?>

<div class="row-fluid">
    <div class="span12 offset3">

<? endif; ?>
    
    <?=form_open_multipart($form_action, array('id' => 'member_post_form'), array('step' => $step, 'next_step' => '', 'unique_id' => $unique_id)) ?>
        
        <? if ($step == Post_member::STEP_START): ?>
            
            
            <?
            $message = isset($this->post_form) ? $this->post_form->error_string() : '';
            if ( $message == '' ) $message = 'You should give as you would receive, cheerfully, quickly, and without hesitation';
            
            if ( $is_new ) {
                $image_text = 'Add photo';
            } else {
                $image_text = 'Change photo';
            }
            ?>
            
            <div class="span6">
                <div class="well ge-well ge-form">
                    <div class="ge-post-member-header">
                        <label class="control-label">
                            <blockquote>
                                <div class="post_ajax_error"><?=$message?></div>
                            </blockquote>
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

                            <? if( !is_empty($image->getDetectedImageUrl()) ): ?>
                                <img src="<?=$image->getDetectedImageUrl()?>" class="ge-post-image-img img img-rounded file" for="image" />
                            <? else: ?>
                                <img src="<?=BASE_PATH?>temp-sample/ge-upload.png" class="ge-post-image-img img img-rounded file" for="image" />
                            <? endif; ?>

                            </div>
                        </div>
                    </div><!--./ge-item-image-->
		    
                    <div class="ge-post-member-footer">
                        <div class="form_control">
	                    <div class="row-fluid">
	                        <div class="span12">
	                            <div class="control-group control-form">
	                                <div class="controls">
	                                    <? if( $is_modal ): ?>
	                                        <button type="button" data-dismiss="modal" class="span2 btn btn-huge"><i class="fui-cross"></i></button>
	                                    <? endif; ?>
	                                    <button type="submit" class="span10 btn btn-huge btn-ge pull-right">NEXT <i class="fui-arrow-right pull-right"></i></button>
	                                </div>
	                            </div>
	                        </div>
	                    </div><!--./submit-->
	                </div>
                    </div>
                </div><!--./ge well-->
            </div><!--./post-gift_1-->
            
            
        <? elseif ($step == Post_member::STEP_DETAILS): ?>
            
            
            <?
            $message = isset($this->post_form) ? $this->post_form->error_string() : '';
            if ( $message == '' ) $message = 'The More You Give, The More You Get. Promise! :)';
            
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
                    <div class="ge-post-member-header">
                        <label class="control-label">
                            <blockquote>
                                <div class="post_ajax_error"><?=$message?></div>
                            </blockquote>
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
                        <div class="span12">
                            <div class="control-group">
                                <div class="controls">
                                    <div class="input-prepend">
                                        <span class="add-on">$</span>
                                        <input id="price" name="price" value="<?=$price?>" class="span2" type="text" placeholder="Current value" required="">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!--./value-->

                    <div class="row-fluid">
                        <div class="span12">
                            <div class="control-group">
                                <div class="controls">
                                    <div class="input">
                                        <input id="zipCode" name="zipCode" value="<?=$zipCode?>" class="span2" type="text" maxlength="5" placeholder="Your Zip code">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!--./zip-->

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

                    <div class="ge-post-member-footer">
                        <div class="row-fluid">
                            <div class="span12">
                                <div class="control-group control-form">
                                    <div class="controls">
                                        <? if( $is_modal ): ?>
                                            <button type="button" data-dismiss="modal" class="span2 btn btn-huge"><i class="fui-cross"></i></button>
                                        <? endif; ?>
                                        <button type="button" onclick="submit_form('member_post_form');" class="span10 btn btn-huge btn-ge pull-right">NEXT <i class="fui-arrow-right pull-right"></i></button>
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
                    <div class="ge-post-member-header">
                        <label class="control-label">
                            <blockquote>
                                <div class="post_ajax_error"><?=$message?></div>
                            </blockquote>
                        </label>
                    </div>
                    
                    <div class="row-fluid ge-map">
                        <div class="span12">
                            <div id="post_map" class="map"></div>
                        </div>
                    </div><!--./ge-item-image-->

                    <div class="ge-post-member-footer">
                        <div class="row-fluid">
                            <div class="span12">
                                <div class="control-group control-form">
                                    <div class="controls">
                                        <? if( $is_modal ): ?>
                                            <button type="button" data-dismiss="modal" class="span2 btn btn-huge"><i class="fui-cross"></i></button>
                                        <? endif; ?>
                                        <button type="button" onclick="submit_form('member_post_form');" class="span10 btn btn-huge btn-block btn-ge pull-right">PREVIEW <i class="fui-arrow-right pull-right"></i></button>
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
            if ( $message == '' ) $message = 'One last step! Make sure everything is correct.<br /><small>(Note: with every gift you give your Generosity Score will increase.)</small>';
            
            if ( $is_new ) {
                $submit_text = 'POST MY GIFT';
            } else {
                $submit_text = 'UPDATE MY GIFT';
            }
            ?>
            
            <div class="span6">
                <div class="well ge-well ge-form">
                    <div class="ge-post-member-header">
                        <label class="control-label">
                            <blockquote>
                                <div class="post_ajax_error"><?=$message?></div>
                            </blockquote>
                        </label>
                    </div>
                    
                    <div class="row-fluid">
                    	<div class="ge-item-image">
                        <img src="<?=$image->getDetectedImageUrl()?>" class="img img-rounded" />
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
	                                <li><em>Current value: $</em><?=$price?></li>
	                            </ul>
	                            <div class="row-fluid">
	                                <div class="span6">
	                                    <label class="checkbox">
	                                        <input <?=(isset($pickUp) && $pickUp == '1') ? 'checked="checked"' : ''?> type="checkbox" data-toggle="checkbox" disabled="disabled">
	                                        Pick up
	                                    </label>
	                                </div>
	                                <div class="span6">
	                                    <label class="checkbox">
	                                        <input <?=(isset($freeShipping) && $freeShipping == '1') ? 'checked="checked"' : ''?> type="checkbox" data-toggle="checkbox" disabled="disabled">
	                                        Free shipping
	                                    </label>	
	                                </div>	
	                            </div>
	                        </div>
                        </div>
                    </div><!--./ge-text-->

                    <div class="ge-post-member-footer">
                        <div class="row-fluid">
                            <div class="span12">
                                <div class="control-group control-form">
                                    <div class="controls">
                                        <button onclick="edit_post();" type="button" class="span4 btn btn-huge"><i class="fui-arrow-left pull-left"></i>EDIT</button>
                                        <button type="button" onclick="submit_form('member_post_form');" class="span8 btn btn-huge btn-ge"><?=$submit_text?><i class="fui-arrow-right pull-right"></i></button>
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
                        <label class="control-label">
                            <blockquote>
                                <div class="post_ajax_error"><?=$error?></div>
                            </blockquote>
                        </label>
                        
                        <div class="ge-post-member-footer">
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
                        <label class="control-label">
                            <blockquote>
                                <p>You're Awesome!</p>
                                <small>Give us few hours to review your gift. (But most likely it will be available for request tomorrow at 12:00 pm ET)</small>
                            </blockquote>
                        </label>
                        
                        <div class="ge-post-member-footer">
                            <div class="row-fluid">
                                <div class="span12">
                                    <div class="control-group control-form">
                                        <div class="controls">
                                            <? if( $is_modal ): ?>
                                                <button type="button" data-dismiss="modal" class="span4 btn btn-huge"><i class="fui-cross pull-left"></i>OK</button>

                                                <? if( $is_new ): ?>
                                                    <button onclick="another_post();" type="button" class="span8 btn btn-huge btn-ge"><i class="pull-right"></i>POST ANOTHER GIFT</button>
                                                <? endif; ?>
                                            <? else: ?>
                                                <? if( $is_new ): ?>
                                                    <a href="<?=base_url()?>post" class="span8 btn btn-huge btn-ge">POST ANOTHER GIFT</a>
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
        
    <?=form_close() ?>

<? if( !$is_modal ): ?>

    </div>
</div>

<? endif; ?>


<? if( $is_modal ): ?>
<script language="javascript">
    initPostModal();
</script>
<? endif; ?>
