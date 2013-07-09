<script langauge="javascript">
    $(function() {
        init_map(true);
        
        $('#postImageBtn').on('file_selected', function() {
            $('input[name=next_step]').val('<?=Post_member::STEP_START?>');
            $("#member_post_form").submit();
        });
        
        $("#editBtn").click(function() {
            $('input[name=next_step]').val('<?=Post_member::STEP_START?>');
            $('#member_post_form').submit();
        });
        $('#anotherBtn').click(function() {
            $('input[name=next_step]').val('<?=Post_member::STEP_START?>');
            $('#member_post_form').submit();
        });
        
        <? if( $is_modal ): ?>
        init_select();
        init_checkbox();
        
        $('#member_post_form').on('submit', function(e) {
            e.preventDefault();
            
            var formData = new FormData($("#member_post_form").get(0));
            
            $.ajax({
                type: "POST",
                url: '<?=base_url()?>post/member?modal',
                dataType: 'html',
                cache: false,
                data: formData,
                processData: false,
                contentType: false
            }).done(function(response) {
                $('#postContainer > .modal-body').html(response);
            }).fail(function(data) {
                //TODO
            });
        });
        <? endif; ?>
    });
</script>

<?
//print_r($this->session->all_userdata());
?>

<? if( !$is_modal ): ?>

<div class="row-fluid">
    <div class="span12 offset3">

<? endif; ?>
    
    <?=form_open_multipart('/post/member' . ($is_modal ? '?modal' : ''), array('id' => 'member_post_form'), array('step' => $step, 'next_step' => '', 'unique_id' => $unique_id)) ?>
        
        <? if ($step == Post_member::STEP_START): ?>
            
            
            <?
            $message = isset($this->post_form) ? $this->post_form->error_string() : '';
            if ( $message == '' ) $message = 'You should give as you would receive, cheerfully, quickly, and without hesitation';
            ?>
            
            <script language="javascript">
                <? if( $is_modal ): ?>
                $(function() {//needs manual event handling
                    hide_file($('#ad_image'));
                    open_file($('#postImageBtn'));
                    open_file($('#postImageImg'));
                    attach_file($('#postImageBtn'));
                });
                <? endif; ?>
            </script>

            <div class="span6">
                <div class="well ge-well ge-form">
                    <label class="control-label">
                        <blockquote>
                            <p id="ajax_error"><?=$message?></p>
                        </blockquote>
                    </label>
                    
                    <div class="row-fluid">
                        <div class="span12">
                            <div class="control-group">
                                <div class="controls">
                                    <button id="postImageBtn" for="ad_image" type="button" class="btn btn-huge btn-block file">
                                        Upload a great photo
                                        <i class="fui-photo pull-right"></i>
                                    </button>
                                    <input type="file" name="ad_image" id="ad_image" />
                                    <input type="hidden" name="ad_image_posted" value="<?= $ad_image ?>" />
                                </div>
                            </div>
                        </div>
                    </div><!--./upload-->

                    <div class="ge-item-image">
                        <div class="row-fluid">
                            <div class="span12">
                            
                            <? if( !is_empty($ad_image) ): ?>
                                <img id="postImageImg" src="<?=base_url()?>get_photo/<?= $ad_image ?>" class="img img-rounded file" for="ad_image" />
                            <? else: ?>
                                <img id="postImageImg" src="<?=BASE_PATH?>temp-sample/ge-upload.png" class="img img-rounded file" for="ad_image" />
                            <? endif; ?>
                            
                            </div>
                        </div>
                    </div><!--./ge-item-image-->

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
                    <label class="control-label">
                        <blockquote>
                            <p id="ajax_error"><?=$message?></p>
                        </blockquote>
                    </label>
                    
                    <div class="row-fluid">
                        <div class="span12">
                            <div class="control-group large">
                                <div class="controls">
                                    <input id="title" name="title" value="<?=$title?>" type="text" maxlength="50" placeholder="Name your gift" class="span3" required="">
                                </div>
                            </div>
                        </div>
                    </div><!--./gift-->

                    <div class="row-fluid ge-description">
                        <div class="span12">
                            <div class="control-group">
                                <div class="controls">
                                    <textarea id="description" name="description" rows="4" maxlength="1500" placeholder="Describe it ..."><?=$description?></textarea>
                                </div>
                            </div>
                        </div>
                    </div><!--/description-->

                    <div class="row-fluid">
                        <div class="span12">
                            <select id="category" name="category" placeholder="Gift category" class="select-block mbl select-info" data-size="5">
                                <option>Select category</option>
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
                </div><!--./ge well-->
            </div><!--./post-gift_2-->
            
            
        <? elseif ($step == Post_member::STEP_MAP): ?>
            
            
            <?
            $message = isset($this->post_form) ? $this->post_form->error_string() : '';
            if ( $message == '' ) $message = 'Doesn\'t need to be perfect, just close enough';
            
            if ( empty($longitude) ) $longitude = hasElement($_POST, 'longitude') ? $_POST['longitude'] : $marker_longitude;
            if ( empty($latitude) ) $latitude = hasElement($_POST, 'latitude') ? $_POST['latitude'] : $marker_latitude;
            ?>
            
            <input id="marker_longitude" type="hidden" value="<?=$marker_longitude?>">
            <input id="marker_latitude" type="hidden" value="<?=$marker_latitude?>">
            
            <input id="longitude" name="longitude" type="hidden" value="<?=$longitude?>">
            <input id="latitude" name="latitude" type="hidden" value="<?=$latitude?>">
            
            <div class="span6">
                <div class="well ge-well ge-form">
                    <label class="control-label">
                        <blockquote>
                            <p id="ajax_error"><?=$message?></p>
                        </blockquote>
                    </label>
                    
                    <div class="row-fluid ge-map">
                        <div class="span12">
                            <div id="map"></div>
                        </div>
                    </div><!--./ge-item-image-->

                    <div class="row-fluid">
                        <div class="span12">
                            <div class="control-group control-form">
                                <div class="controls">
                                    <button type="submit" class="btn btn-huge btn-block btn-ge pull-right">PREVIEW <i class="fui-arrow-right pull-right"></i></button>
                                </div>
                            </div>
                        </div>
                    </div><!--./submit-->
                </div><!--./ge well-->
            </div><!--./post-gift_3-->
            
            
        <? elseif ($step == Post_member::STEP_PREVIEW): ?>
            
            
            <?
            $message = isset($this->post_form) ? $this->post_form->error_string() : '';
            if ( $message == '' ) $message = 'One last step! Make sure everything is correct.<br /><small>(Note: with every gift you give your Generosity Score will increase.)</small>';
            ?>
            
            <div class="span6">
                <div class="well ge-well ge-form">
                    <label class="control-label">
                        <blockquote>
                            <p id="ajax_error"><?=$message?></p>
                        </blockquote>
                    </label>
                    
                    <div class="row-fluid ge-item-image">
                        <img src="<?=base_url()?>get_photo/<?=$ad_image?>" class="img img-rounded" />
                    </div><!--./ge-item-image-->

                    <div id="item_description" class="row-fluid ge-text">
                        <p class="ge-title" style="margin-top: 0px;padding-top: 0px;line-height: 16px;"><?=$title?></p>
                        <p class="ge-description">
                            <em>Description:</em> <?=$description?>
                        </p>
                        <p class="ge-details">
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
                        </p>
                    </div><!--./ge-text-->

                    <div class="row-fluid">
                        <div class="span12">
                            <div class="control-group control-form">
                                <div class="controls">
                                    <button id="editBtn" type="button" class="span4 btn btn-huge"><i class="fui-arrow-left pull-left"></i>EDIT</button>
                                    <button type="submit" class="span8 btn btn-huge btn-ge">POST MY GIFT<i class="fui-arrow-right pull-right"></i></button>
                                </div>
                            </div>
                        </div>
                    </div><!--./submit-->

                </div><!--./ge well-->
            </div><!--./post-gift_4-->
            
            
        <? elseif ($step == Post_member::STEP_POST): ?>
            
            
            <? if( !empty($error) ): ?>
                
                <div class="span6">
                    <div class="well ge-well ge-form">
                        <label class="control-label">
                            <blockquote>
                                <p id="ajax_error"><?=$error?></p>
                            </blockquote>
                        </label>
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
                        
                        <div class="row-fluid">
                            <div class="span12">
                                <div class="control-group control-form">
                                    <div class="controls">
                                        <? if( $is_modal ): ?>
                                            <button type="button" data-dismiss="modal" class="span4 btn btn-huge"><i class="fui-cross pull-left"></i>DONE</button>
                                            <button id="anotherBtn" type="button" class="span8 btn btn-huge btn-ge"><i class="pull-right"></i>POST ANOTHER GIFT</button>
                                        <? else: ?>
                                            <a href="<?=base_url()?>post" class="span8 btn btn-huge btn-ge">POST ANOTHER GIFT</a>
                                        <? endif; ?>
                                    </div>
                                </div>
                            </div>
                        </div><!--./submit-->
                    </div><!--./ge well-->
                </div><!--./post-gift_5-->
                
            <? endif; ?>
            
            
        <? endif; ?>
        
    <?=form_close() ?>

<? if( !$is_modal ): ?>

    </div>
</div>

<? endif; ?>
