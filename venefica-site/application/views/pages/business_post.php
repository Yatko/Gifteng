<script langauge="javascript">
    $(function() {
        $("#expires").change(function() {
            $("#expiresAt").prop("disabled", $(this).is(':checked'));
        });
        
        
        $("#allWeek").change(function() {
            if ( $(this).is(':checked') ) {
                $('#availableDays option').prop('selected', true);
                $("#availableDays").selectpicker('render');
            }
        });
        $("#availableDays").change(function() {
            var allSelected = $("#availableDays option:not(:selected)").length === 0;
            if ( allSelected ) {
                $("#allWeek").prop('checked', true);
                $("#allWeek").checkbox('check');
            } else {
                $("#allWeek").prop('checked', false);
                $("#allWeek").checkbox('uncheck');
            }
        });
        
        
        $("#availableFromTime").change(function() {
            if ( $("#availableFromTime option:selected").val() === "*" ) {
                $("#availableToTime").prop("disabled", true);
                $("#availableToTime").selectpicker('render');
            } else {
                $("#availableToTime").prop("disabled", false);
                $("#availableToTime").selectpicker('render');
            }
        });
        
        
        $("#new_address").click(function() {
            $.ajax({
                type: 'POST',
                url: '<?=base_url()?>post/business/ajax/new_address',
                dataType: 'json',
                data: {
                    new_address_name: $("#new_address_name").val(),
                    new_address_address: $("#new_address_address").val(),
                    new_address_address_2: $("#new_address_address_2").val(),
                    new_address_city: $("#new_address_city").val(),
                    new_address_zipCode: $("#new_address_zipCode").val()
                }
            }).done(function(address) {
                var addressId = address.id;
                var addressName = address.name;

                var $parent = $("#business_post_location_addresses");
                var source = $parent.children(":first")[0];
                var cloned = $(source).clone(true, true)[0];

                $(cloned).find("*").each(function(index, element) {
                    var oldId = element.id;
                    var oldName = element.name;
                    var oldFor = $(element).attr('for');

                    if (oldId) {
                        var matches = oldId.match(/(.+)_\d+/);
                        if (matches && matches.length >= 2) {
                            var newId = matches[1] + "_" + addressId;
                            element.id = newId;
                        }
                    }
                    if (oldName) {
                        var matches = oldName.match(/(.+)_\d+/);
                        if (matches && matches.length >= 2) {
                            var newName = matches[1] + "_" + addressId;
                            element.name = newName;
                        }
                    }
                    if (oldFor) {
                        var matches = oldFor.match(/(.+)_\d+/);
                        if (matches && matches.length >= 2) {
                            var newFor = matches[1] + "_" + addressId;
                            $(element).attr('for', newFor);
                        }
                    }
                });
                $(cloned).find("#address_" + addressId).text(addressName);
                //alert(cloned.outerHTML);
                $(cloned).appendTo($parent);

                $("#new_address_name").val('');
                $("#new_address_address").val('');
                $("#new_address_address_2").val('');
                $("#new_address_city").val('');
                $("#new_address_zipCode").val('');
                $("#locationContainer").modal('hide');
            }).fail(function(data) {
                //TODO
            });
        });
    });
</script>

<?
//print_r($this->session->all_userdata());
?>


<div class="container ge-topspace">
<div class="row ge-business ge-post">

    <div class="span12 ge-headline">
        <blockquote>
            <p>Give your customers a reason to come in, give them something they appreciate</p>
            <small>Post a Gift</small>
        </blockquote>
    </div><!--./ge-headline-->
    
    
    <?=form_open_multipart('/post/business', '', array('step' => $step)) ?>
    <?=isset($this->post_form) ? $this->post_form->error_string() : "" ?>
        
        
        <? if ($step == Business_post::STEP_START): ?>
            
            
            <div id="business_post_start" class="span6 ge-form">
            <div class="well ge-well">
            
            <div class="row-fluid">
                <div class="span12">
                    <div class="control-group large">
                        <label class="control-label" for="">The Gift</label>
                        <div class="controls">
                            <input id="title" name="title" value="<?=set_value('title') ?>" type="text" placeholder="Name your Gift" class="span3" required="">
                        </div>
                    </div>
                </div>
            </div><!--./gift-->

            <div class="row-fluid">
                <div class="span12">
                    <div class="control-group">
                        <label class="control-label" for="price">MSRP value</label>
                        <div class="controls">
                            <div class="input-prepend">
                                <span class="add-on">$</span>
                                <input id="price" name="price" value="<?=set_value('price') ?>" type="text" class="span2" placeholder="0.00" required="">
                            </div>
                        </div>
                    </div>
                </div>
            </div><!--./value-->
            
            <div class="row-fluid">
                <div class="span12">
                    <div class="control-group">
                        <label class="control-label" for="subtitle">Additionally you may offer a certain percentage off the receipt in case of purchase.</label>
                        <div class="controls">
                            <div class="input-append">
                                <input id="subtitle" name="subtitle" value="<?=set_value('subtitle') ?>" type="text" class="span2" id="" placeholder="00">
                                <span class="add-on">% off</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!--./offer-->
            
            <div class="row-fluid">
                <div class="span12">
                    <label class="control-label" for="category">Gift category</label>
                    <select id="category" name="category" placeholder="Gift category" class="select-block mbl select-info" data-size="5">
                        <option>Select category</option>
                        <? foreach ($categories as $category): ?>
                            <?
                            $category_id = $category->id;
                            $category_name = $category->name;
                            ?>

                            <option value="<?=$category_id ?>" <?=set_select('category', $category_id) ?>><?=$category_name ?></option> 
                        <? endforeach; ?>
                    </select>
                </div>
            </div><!--./select-category-->
            
            <div class="row-fluid"><!--TODO remove style="..."-->
                <div class="span6">
                    <div class="control-group">
                        <label class="control-label">
                            <div class="span3">
                                <label for="availableAt">Starting</label>
                            </div>
                            <div class="span6">
                                <div class="control-group"><div class="controls"></div></div>
                            </div>
                        </label>
                        <div class="controls">
                            <div class="input-prepend input-datepicker">
                                <button type="button" class="btn"><span class="fui-calendar"></span></button>
                                <input id="availableAt" name="availableAt" value="<?=set_value('availableAt') ?>" type="text" class="span2 ge-datepicker" style="width:160px;">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="span6">
                    <div class="control-group">
                        <label class="control-label">
                            <div class="span3">
                                <label for="expiresAt">Expires</label>
                            </div>
                            <div class="span6 ge-check">
                                <div class="control-group">
                                    <div class="controls">
                                        <label class="checkbox" for="neverExpires">
                                            <input name="neverExpires" id="expires" value="1" <?=set_checkbox('neverExpires', '1', TRUE) ?> type="checkbox" data-toggle="checkbox">
                                            Never expires
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </label>
                        <div class="controls">
                            <div class="input-prepend input-datepicker">
                                <button type="button" class="btn"><span class="fui-calendar"></span></button>
                                <input id="expiresAt" name="expiresAt" value="<?=set_value('expiresAt') ?>" type="text" class="span2 ge-datepicker" style="width:160px;" disabled="disabled">
                            </div>
                        </div>
                    </div>
                </div>
            </div><!--./available date-->
            
            <div class="row-fluid">
                <div class="span12">
                    <div class="control-group">
                        <label class="control-label" for="checkboxes">This Gift available</label>
                        <div class="controls">
                            <label class="radio inline" for="place_ONLINE">
                                <input name="place" id="place_ONLINE" value="<?=Ad_model::PLACE_ONLINE ?>" <?=set_radio('place', Ad_model::PLACE_ONLINE, TRUE) ?> type="radio" data-toggle="radio">
                                Online
                            </label>
                            <label class="radio inline" for="place_LOCATION">
                                <input name="place" id="place_LOCATION" value="<?=Ad_model::PLACE_LOCATION ?>" <?=set_radio('place', Ad_model::PLACE_LOCATION) ?> type="radio" data-toggle="radio">
                                At location
                            </label>
                        </div>
                    </div>
                </div>
            </div><!--./available location-->
            
            <div class="row-fluid ge-submit">
                <div class="span12">
                    <div class="control-group">
                        <div class="controls">
                            <button type="submit" class="btn btn-huge btn-block btn-primary">NEXT <i class="fui-arrow-right pull-right"></i></button>
                        </div>
                    </div>
                </div>
            </div><!--./submit-->
            
            </div><!--./ge well-->
            </div><!--./ge-post step1-->
            
            
        <? elseif ($step == Business_post::STEP_ONLINE): ?>


            <div id="business_post_online" class="span6 ge-form">
            <div class="well ge-well">
            
            <div class="row-fluid ge-terms">
                <div class="span12">
                    <div class="control-group">
                        <label class="control-label" for="description">Terms & conditions</label>
                        <div class="controls">
                            <textarea id="description" name="description" rows="4"><?=set_value('description') ?></textarea>
                        </div>
                    </div>
                </div>
            </div><!--/terms-->
            
            <div class="row-fluid">
                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="quantity">Gifts available</label>
                        <div class="controls">
                            <div class="input">
                            <input id="quantity" name="quantity" value="<?=set_value('quantity') ?>" type="text" class="span2" placeholder="0" required="">
                        </div>
                        </div>
                    </div>
                </div>
            </div><!--./gifts -->
            
            <div class="row-fluid">
                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="promocode">Promo code</label>
                        <div class="controls">
                            <div class="input">
                                <input id="promocode" name="promocode" value="<?=set_value('promocode') ?>" type="text" class="span2" id="" placeholder="XXXXX" required="">
                            </div>
                        </div>
                    </div>
                </div>
            </div><!--./promocode -->
            
            <div class="row-fluid">
                <div class="span12">
                    <div class="control-group">
                        <label class="control-label" for="website">Website</label>
                        <div class="controls">
                            <div class="input-prepend">
                                <span class="add-on">http://</span>
                                <input id="website" name="website" value="<?=set_value('website') ?>" type="text" class="span2" placeholder="www.your-website.com" required="">
                            </div>
                        </div>
                    </div>
                </div>
            </div><!--./website -->
            
            <div class="row-fluid">
                <div class="span12">
                    <div class="control-group">
                        <label class="control-label">Photo</label>
                        <div class="controls">
                            <button class="btn file" for="image" type="button">
                                <? if( !is_empty($image) ): ?>
                                <img src="<?=base_url()?>get_photo/<?=$image?>/30/30" />
                                <? endif; ?>
                                Upload from your device
                            </button>
                            <input type="file" name="image" id="image" />
                            <input type="hidden" name="image_posted" value="<?=$image?>" />
                        </div>
                    </div>
                </div>
            </div><!--./photo -->
            
            <div class="row-fluid ge-submit">
                <div class="span12">
                    <div class="control-group">
                        <div class="controls">
                            <button type="submit" class="btn btn-huge btn-block btn-primary">PREVIEW <i class="fui-arrow-right pull-right"></i></button>
                        </div>
                    </div>
                </div>
            </div><!--./submit-->

            </div><!--./ge well-->
            </div><!--./ge-post online-->


        <? elseif ($step == Business_post::STEP_LOCATION): ?>
            
            
            <?
            $default_availableFromTime = 7;
            $availableFromTime = hasElement($_POST, 'availableFromTime') ? $_POST['availableFromTime'] : $default_availableFromTime;
            
            $availableFromTime_AM = array('7' => '07:00 AM', '8' => '08:00 AM', '9' => '09:00 AM', '10' => '10:00 AM', '11' => '11:00 AM');
            $availableFromTime_PM = array('12' => '12:00 PM', '13' => '01:00 PM', '14' => '02:00 PM', '15' => '03:00 PM', '16' => '04:00 PM', '17' => '05:00 PM', '18' => '06:00 PM', '19' => '07:00 PM', '20' => '08:00 PM', '21' => '09:00 PM', '22' => '10:00 PM', '23' => '11:00 PM');
            
            $availableToTime_AM = array('9' => '09:00 AM', '10' => '10:00 AM', '11' => '11:00 AM');
            $availableToTime_PM = array('12' => '12:00 PM', '13' => '01:00 PM', '14' => '02:00 PM', '15' => '03:00 PM', '16' => '04:00 PM', '17' => '05:00 PM', '18' => '06:00 PM', '19' => '07:00 PM', '20' => '08:00 PM', '21' => '09:00 PM', '22' => '10:00 PM', '23' => '11:00 PM');
            $availableToTime_AM_NEXTDAY = array('24' => '12:00 AM');
            ?>

            <div id="business_post_location" class="span6 ge-form">
            <div class="well ge-well">
            
            <div class="row-fluid ge-terms">
                <div class="span12">
                    <div class="control-group">
                        <label class="control-label" for="description">Terms & conditions</label>
                        <div class="controls">
                            <textarea id="description" name="description" rows="4"><?=set_value('description') ?></textarea>
                        </div>
                    </div>
                </div>
            </div><!--/terms-->
            
            <div class="row-fluid ge-select">
                <div class="span12">
                    <div class="control-group">
                        <label class="control-label">
                            <div class="span3">
                                <label for="availableDays">Available</label>
                            </div>
                            <div class="span6 ge-check">
                                <div class="control-group">
                                    <div class="controls">
                                        <label class="checkbox" for="allWeek">
                                            <input id="allWeek" type="checkbox" data-toggle="checkbox">
                                            All week
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </label>
                        <div class="controls">
                            <div class="span12">
                                <select id="availableDays" name="availableDays" multiple="multiple" class="select-block mbl select-info" title=' '>
                                    <option value="<?=Ad_model::WEEKDAY_MONDAY ?>" <?=set_select('availableDays', Ad_model::WEEKDAY_MONDAY) ?>>Monday</option>
                                    <option value="<?=Ad_model::WEEKDAY_TUESDAY ?>" <?=set_select('availableDays', Ad_model::WEEKDAY_TUESDAY) ?>>Tuesday</option>
                                    <option value="<?=Ad_model::WEEKDAY_WEDNESDAY ?>" <?=set_select('availableDays', Ad_model::WEEKDAY_WEDNESDAY) ?>>Wednesday</option>
                                    <option value="<?=Ad_model::WEEKDAY_THURSDAY ?>" <?=set_select('availableDays', Ad_model::WEEKDAY_THURSDAY) ?>>Thursday</option>
                                    <option value="<?=Ad_model::WEEKDAY_FRIDAY ?>" <?=set_select('availableDays', Ad_model::WEEKDAY_FRIDAY) ?>>Friday</option>
                                    <option value="<?=Ad_model::WEEKDAY_SATURDAY ?>" <?=set_select('availableDays', Ad_model::WEEKDAY_SATURDAY) ?>>Saturday</option>
                                    <option value="<?=Ad_model::WEEKDAY_SUNDAY ?>" <?=set_select('availableDays', Ad_model::WEEKDAY_SUNDAY) ?>>Sunday</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
            </div><!--./select-day -->
            
            <div class="row-fluid ge-select">
                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="availableFromTime">From</label>
                        <div class="controls">
                            <select id="availableFromTime" name="availableFromTime" class="select-block mbl select-info" data-size="5">
                                <option value="*" <?=set_select('availableFromTime', '*')?> class="highlighted fui-lock">All day</option>
                                <? foreach ( $availableFromTime_AM as $key => $value ): ?>
                                    <option value="<?=$key?>" <?=$availableFromTime == $key ? 'selected="selected"' : ''?>><?=$value?></option>
                                <? endforeach; ?>
                                <option data-divider="true"></option>
                                <? foreach ( $availableFromTime_PM as $key => $value ): ?>
                                    <option value="<?=$key?>" <?=$availableFromTime == $key ? 'selected="selected"' : ''?>><?=$value?></option>
                                <? endforeach; ?>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="availableToTime">Until</label>
                        <div class="controls">
                            <select id="availableToTime" name="availableToTime" class="select-block mbl select-info" data-size="5">
                                <? foreach ( $availableToTime_AM as $key => $value ): ?>
                                    <option value="<?=$key?>" <?=set_select('availableToTime', $key)?>><?=$value?></option>
                                <? endforeach; ?>
                                <option data-divider="true"></option>
                                <? foreach ( $availableToTime_PM as $key => $value ): ?>
                                    <option value="<?=$key?>" <?=set_select('availableToTime', $key)?>><?=$value?></option>
                                <? endforeach; ?>
                                <option data-divider="true"></option>
                                <? foreach ( $availableToTime_AM_NEXTDAY as $key => $value ): ?>
                                    <option value="<?=$key?>" <?=set_select('availableToTime', $key)?>><?=$value?></option>
                                <? endforeach; ?>
                            </select>
                        </div>
                    </div>
                </div>
            </div><!--./select-time -->
            
            <div class="row-fluid ge-check">
                <div class="span12">
                    <div class="control-group">
                        <div class="controls">
                            <label class="checkbox" for="needReservation">
                                <input id="needReservation" name="needReservation" value="1" <?=set_checkbox('needReservation', '1', FALSE) ?> type="checkbox" data-toggle="checkbox">
                                Appointment/Reservation needed
                            </label>
                        </div>
                    </div>
                </div>
            </div><!--./appointment -->
            
            <hr>

            <div class="row-fluid ge-multiple-location">
                <div class="span12">
                    
                    <div class="row-fluid">
                        <div class="span6">
                            Location <a data-toggle="modal" href="#locationContainer">+ Add location</a>
                        </div>
                        <div class="span2">
                            Gifts
                        </div>
                        <div class="span2">
                            Photo
                        </div>
                        <div class="span2">
                            QR/Bar
                        </div>
                    </div><!--./add location-->
                    
                    
                    <div id="business_post_location_addresses">

                    <? if( !is_empty($user->addresses) ): ?>
                    <? foreach( $user->addresses as $address ): ?>
                        <?
                        $address_id = $address->id;
                        $address_detail = $address->name;

                        $image = ${'image_'.$address_id};
                        $bar_code = ${'bar_code_'.$address_id};
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
                                    <? if( !is_empty($bar_code) ): ?>
                                    <img src="<?=base_url()?>get_photo/<?=$bar_code?>/30/30" />
                                    <? endif; ?>
                                    Add
                                </button>
                                <input type="file" name="bar_code_<?=$address_id?>" id="bar_code_<?=$address_id?>" />
                                <input type="hidden" name="bar_code_posted_<?=$address_id?>" value="<?=$bar_code?>" />
                            </div>
                            
                        </div><!--/location-->

                    <? endforeach; ?>
                    <? endif; ?>

                    </div>

                    
                    <div class="row-fluid">
                        <div class="span6">
                            Location
                            <a data-toggle="modal" href="#locationContainer">+ Add location</a>
                        </div>
                        <div class="span2">
                            Gifts
                        </div>
                        <div class="span2">
                            Photo
                        </div>
                        <div class="span2">
                            QR/Bar
                        </div>
                    </div><!--./add location-->
                    
                    
                    <div id="locationContainer" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            Enter new business location details:
                        </div>
                        <div class="modal-body">
                            <div id="business_post_location" class="ge-form">
                                <div class="row-fluid">
                                    <div class="control-group">
                                        <div class="controls">
                                            <input id="new_address_name" type="text" placeholder="Location name" class="input-block-level" required="">
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <div class="controls">
                                            <input id="new_address_address" type="text" placeholder="Address" class="input-block-level" required="">
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <div class="controls">
                                            <input id="new_address_address_2" type="text" placeholder="Address 2" class="input-block-level" required="">
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <div class="controls">
                                            <input id="new_address_city" type="text" placeholder="City" class="input-block-level" required="">
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <div class="controls">
                                            <input id="new_address_zipCode" type="text" maxlength="5" placeholder="Zip code" class="input-block-level" required="">
                                        </div>
                                    </div>
                                </div>
                            </div><!--./ge-lightbox add location-->
                        </div>
                        <div class="modal-footer">
                            <div class="control-group">
                                <div class="controls">
                                    <button id="new_address" type="button" class="btn btn-large btn-inverse btn-block ge-submit">Add new location</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                </div>
            </div><!--/multiple locations-->

            <div class="row-fluid ge-submit">
                <div class="span12">
                    <div class="control-group">
                        <div class="controls">
                            <button type="submit" class="btn btn-huge btn-block btn-primary">PREVIEW <i class="fui-arrow-right pull-right"></i></button>
                        </div>
                    </div>
                </div>
            </div><!--./submit-->
            
            </div><!--./ge well-->
            </div><!--./ge-post location-->


        <? elseif ($step == Business_post::STEP_PREVIEW): ?>


            preview


        <? elseif ($step == Business_post::STEP_POST): ?>


            Error: <?=$error ?>


        <? endif; ?>

    <?=form_close() ?>
    
</div>
</div>
