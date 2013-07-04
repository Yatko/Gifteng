<script langauge="javascript">
    $(function() {
        $('#postImage').on('file_selected', function() {
            $('input[name=next_step]').val('<?=Post_member::STEP_START?>');
            $("#member_post_form").submit();
        });
        
        if ( $('#map').length > 0 ) {
            //#map element exists
            
            var locationIcon = L.icon({
                iconUrl: '<?=BASE_PATH?>temp-sample/ge-location-pin-teal.png',
                iconSize: [64, 64]
            });
            
            var marker_longitude = $("#marker_longitude").val();
            var marker_latitude = $("#marker_latitude").val();
            
            var locationMarker = L.marker([marker_latitude, marker_longitude], {
                icon: locationIcon,
                draggable: true,
                title: 'Drag me to the exact location',
            });
            locationMarker.on('dragend', function() {
                var latlng = locationMarker.getLatLng();
                var lng = latlng.lng;
                var lat = latlng.lat;
                
                $("#longitude").val(lng);
                $("#latitude").val(lat);
            });
            
            var tileLayer = L.tileLayer.provider('Esri.WorldStreetMap'); //very simple and useful
            //var tileLayer = L.tileLayer.provider('Esri.WorldImagery'); //similar with google terrain maps
            //var tileLayer = L.tileLayer.provider('Nokia.normalDay');//so-so
            //var tileLayer = L.tileLayer.provider('Nokia.satelliteYesLabelsDay'); //probably the best: very-very similar with google terrain also with labels
            
            /**
            var tileLayer = L.tileLayer('http://{s}.tile.cloudmade.com/c82af3890f2f47afb15da8e99832543b/997/256/{z}/{x}/{y}.png', {
                attribution: 'Gifteng',
                maxZoom: 18
            });
            /**/
            
            var map = L.map('map').setView([marker_latitude, marker_longitude], 16);
            tileLayer.addTo(map);
            locationMarker.addTo(map);
        }
        $("#editButton").click(function() {
            $('input[name=next_step]').val('<?=Post_member::STEP_START?>');
            $('#member_post_form').submit();
        });
        $('#closeButton').click(function() {
            //TODO: detect if is in popup and close it
        });
    });
</script>

<?
//print_r($this->session->all_userdata());
?>


<div class="container ge-topspace">

    <div class="span12">
        
    <?=form_open_multipart('/post/member', array('id' => 'member_post_form'), array('step' => $step, 'next_step' => '')) ?>
    
        
        <? if ($step == Post_member::STEP_START): ?>
            <?
            $message = isset($this->post_form) ? $this->post_form->error_string() : '';
            if ( $message == '' ) $message = 'You should give as you would receive, cheerfully, quickly, and without hesitation';
            ?>
        
            
            <div id="post-gift_1" class="span6 offset3">
                <div class="well ge-well ge-form">
                    <label class="control-label">
                        <blockquote>
                            <p><?=$message?></p>
                        </blockquote>
                    </label>
                    
                    <div class="row-fluid">
                        <div class="span12">
                            <div class="control-group">
                                <div class="controls">
                                    <button id="postImage" for="image" type="button" class="btn btn-huge btn-block file">Upload a great photo<i class="fui-photo pull-right"></i></button>
                                    <input type="file" name="image" id="image" />
                                    <input type="hidden" name="image_posted" value="<?=$image?>" />
                                </div>
                            </div>
                        </div>
                    </div><!--./upload-->

                    <div class="ge-item-image">
                        <div class="row-fluid">
                            <div class="span12">
                            
                            <? if( !is_empty($image) ): ?>
                                <img src="<?=base_url()?>get_photo/<?=$image?>" class="img img-rounded file" for="image" />
                            <? else: ?>
                                <img src="<?=BASE_PATH?>temp-sample/ge-upload.png" class="img img-rounded file" for="image" />
                            <? endif; ?>
                            
                            </div>
                        </div>
                    </div><!--./ge-item-image-->

                    <div class="row-fluid">
                        <div class="span12">
                            <div class="control-group control-form">
                                <div class="controls">
                                    <button id="closeButton" type="button" class="span2 btn btn-huge"><i class="fui-cross"></i></button>
                                    <button type="submit" class="span10 btn btn-huge btn-ge">NEXT <i class="fui-arrow-right pull-right"></i></button>
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
            
            <div id="post-gift_2" class="span6 offset3">
                <div class="well ge-well ge-form">
                    <label class="control-label">
                        <blockquote>
                            <p><?=$message?></p>
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
                                    <button id="closeButton" type="button" class="span2 btn btn-huge"><i class="fui-cross"></i></button>
                                    <button type="submit" class="span10 btn btn-huge btn-ge">NEXT <i class="fui-arrow-right pull-right"></i></button>
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
            
            <div id="post-gift_3" class="span6 offset3">
                <div class="well ge-well ge-form">
                    <label class="control-label">
                        <blockquote>
                            <p><?=$message?></p>
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
                                    <button type="submit" class="btn btn-huge btn-block btn-ge">PREVIEW <i class="fui-arrow-right pull-right"></i></button>
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
            
            <div id="post-gift_4" class="span6 offset3">
                <div class="well ge-well ge-form">
                    <label class="control-label">
                        <blockquote>
                            <p><?=$message?></p>
                        </blockquote>
                    </label>
                    
                    <div class="row-fluid ge-item-image">
                        <img src="<?=base_url()?>get_photo/<?=$image?>" class="img img-rounded" />
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
                                    <button id="editButton" type="button" class="span4 btn btn-huge"><i class="fui-arrow-left pull-left"></i>EDIT</button>
                                    <button type="submit" class="span8 btn btn-huge btn-ge">POST MY GIFT<i class="fui-arrow-right pull-right"></i></button>
                                </div>
                            </div>
                        </div>
                    </div><!--./submit-->

                </div><!--./ge well-->
            </div><!--./post-gift_4-->
            
            
        <? elseif ($step == Post_member::STEP_POST): ?>
            
            
            <? if( !empty($error) ): ?>
                
                <div id="post-gift_5" class="span6 offset3">
                    <div class="well ge-well ge-form">
                        <label class="control-label">
                            <blockquote>
                                <p><?=$error?></p>
                            </blockquote>
                        </label>
                    </div><!--./ge well-->
                </div><!--./post-gift_5-->
                
            <? else: ?>
                
                <div id="post-gift_5" class="span6 offset3">
                    <div class="well ge-well ge-form">
                        <label class="control-label">
                            <blockquote>
                                <p>You're Awesome!</p>
                                <small>Give us few hours to review your gift. (But most likely it will be available for request tomorrow at 12:00 pm ET)</small>
                            </blockquote>
                        </label>
                        
                        <div class="row-fluid ge-headline  ge-topspace">
                            <blockquote>
                                <p>You're Awesome!</p>
                                <small>We'll notify you when your Gift is posted.</small>
                            </blockquote>
                        </div><!--./ge-headline-->

                        <div class="row-fluid">
                            <div class="span12">
                                <div class="control-group control-form">
                                    <div class="controls">
                                        <button id="closeButton" type="button" class="span4 btn btn-huge"><i class="fui-cross pull-left"></i>DONE</button>
                                        <a href="<?=base_url()?>post" class="span8 btn btn-huge btn-ge">POST ANOTHER GIFT</a>
                                    </div>
                                </div>
                            </div>
                        </div><!--./submit-->
                    </div><!--./ge well-->
                </div><!--./post-gift_5-->
                
            <? endif; ?>
            
            
        <? endif; ?>
        
    <?=form_close() ?>
    </div>
</div>