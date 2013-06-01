<!--content (form)-->
<div class="span12">
    <div class="well geWell">
        <?= form_open_multipart('/post', '', array('step' => $step)) ?>
        <?= isset($this->post_form) ? $this->post_form->error_string() : "" ?>

        <? if ($step == Post::STEP_START): ?>


            <!--tagline-->
            <div class="row-fluid geFormTagline">
                <h4>Give your customers a reason to come in, give them something they appreciate</h4>
            </div><!--/tagline-->

            <!--part 1 gift-->
            <div class="row-fluid">
                <div class="control-group">
                    <label class="control-label" for="title">Part 1: </label>
                    <div class="controls">
                        <input id="title" name="title" value="<?= set_value('title') ?>" type="text" placeholder="Complimentary gift" class="input-xxlarge">
                    </div>
                </div>
            </div><!--/part 1 gift-->

            <!--part 2-->
            <div class="row-fluid">
                <!--discount-->
                <div class="span4">
                    <div class="control-group">
                        <label class="control-label" for="subtitle">Part 2: </label>
                        <div class="controls">
                            <div class="input-append">
                                <input id="subtitle" name="subtitle" value="<?= set_value('subtitle') ?>" class="input-mini" placeholder="00" type="text">
                                <span class="add-on">% off the receipt (optional)</span>
                            </div>
                        </div>
                    </div>
                </div><!--/discount-->

                <!--value-->
                <div class="span4">
                    <div class="control-group">
                        <label class="control-label" for="price">&nbsp; </label>
                        <div class="controls">
                            <div class="input-prepend">
                                <span class="add-on">Value:</span>
                                <input id="price" name="price" value="<?= set_value('price') ?>" class="input-mini" placeholder="$" type="text">
                            </div>
                        </div>
                    </div>
                </div><!--/value-->
            </div><!--/part 2-->

            <!--category-->
            <div class="row-fluid">
                <div class="control-group">
                    <label class="control-label" for="category">Category: </label>
                    <div class="controls">
                        <select id="category" name="category" placeholder="Gift category">
                            <option value=""></option>
                            <? foreach ($categories as $category): ?>
                                <?
                                $category_id = $category->id;
                                $category_name = $category->name;
                                ?>

                                <option value="<?= $category_id ?>" <?= set_select('category', $category_id) ?>><?= $category_name ?></option> 
                            <? endforeach; ?>
                        </select>
                    </div>
                </div>
            </div><!--/category-->

            <!--available place-->
            <div class="row-fluid">
                <div class="control-group">
                    <label class="control-label">This Gift available: </label>
                    <div class="controls">
                        <label class="radio inline" for="place_ONLINE">
                            <input type="radio" name="place" id="place_ONLINE" value="<?= Ad_model::PLACE_ONLINE ?>" <?= set_radio('place', Ad_model::PLACE_ONLINE, TRUE) ?>>
                            Online
                        </label>
                        <label class="radio inline" for="place_LOCATION">
                            <input type="radio" name="place" id="place_LOCATION" value="<?= Ad_model::PLACE_LOCATION ?>" <?= set_radio('place', Ad_model::PLACE_LOCATION) ?>>
                            At location
                        </label>
                    </div>
                </div>	
            </div><!--/available place-->

            <!--available time-->
            <div class="row-fluid">
                <!--starting-->
                <div class="span4">
                    <div class="control-group">
                        <label class="control-label" for="availableAt">Starting: </label>
                        <div class="controls">
                            <div class="input-append">
                                <div class="input-append date" data-date="12-02-2012" data-date-format="dd-mm-yyyy">
                                    <input class="span2" size="106" id="availableAt" name="availableAt" value="<?= set_value('availableAt') ?>" type="text">
                                    <span class="add-on"><i class="icon-th"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div><!--/starting-->
                <!--expires-->
                <div class="span2">
                    <div class="control-group">
                        <label class="control-label" for="expiresAt">Expires: </label>
                        <div class="controls">
                            <div class="input-append">
                                <div class="input-append date" data-date="12-02-2012" data-date-format="dd-mm-yyyy">
                                    <input class="span2" size="106" id="expiresAt" name="expiresAt" value="<?= set_value('expiresAt') ?>" type="text">
                                    <span class="add-on"><i class="icon-th"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>	
                </div><!--/expires-->
                <!--never-->
                <div class="span2">
                    <div class="control-group">
                        <label class="control-label" for="expires">&nbsp; </label>
                        <div class="controls">
                            <label class="checkbox" for="expires">
                                <input type="checkbox" name="expires" id="expires" value="1" <?= set_checkbox('expires', '1', FALSE) ?>>
                                Never
                            </label>
                        </div>
                    </div>	
                </div><!--/never-->
            </div><!--/available time-->

            <!--submit-->
            <div class="row-fluid geFormSubmit">
                <div class="control-group">
                    <div class="controls">
                        <button class="btn btn-large btn-ge" type="submit">Next &raquo;</button>
                    </div>
                </div>
            </div><!--/submit-->


        <? elseif ($step == Post::STEP_ONLINE): ?>


            <!--tagline-->
            <div class="row-fluid geFormTagline">
                <h4>Give your customers a reason to come in, give them something they appreciate</h4>
            </div><!--/tagline-->

            <!--terms-->
            <div class="row-fluid geFormTerms">
                <div class="control-group">
                    <label class="control-label" for="description">Terms & conditions</label>
                    <div class="controls">
                        <textarea id="description" name="description" rows="4"><?= set_value('description') ?></textarea>
                    </div>
                </div>
            </div><!--/terms-->

            <div class="row-fluid">
                <!--number og-->
                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="quantity">Gifts </label>
                        <div class="controls">
                            <div class="input-prepend">
                                <span class="add-on">Available (number):</span>
                                <input id="quantity" name="quantity" value="<?= set_value('quantity') ?>" class="input-mini" placeholder="0" type="text">
                            </div>
                        </div>
                    </div>
                </div><!--/number og-->
                <!--promo code-->
                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="promocode">Promo code </label>
                        <div class="controls">
                            <div class="input-prepend">
                                <span class="add-on">GE -</span>
                                <input id="promocode" name="promocode" value="<?= set_value('promocode') ?>" class="input-small" placeholder="XXX-XXX" type="text">
                            </div>
                        </div>
                    </div>
                </div><!--/promo code-->
            </div>

            <div class="row-fluid">
                <!--website-->
                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="website">Website </label>
                        <div class="controls">
                            <div class="input-prepend">
                                <span class="add-on">http://</span>
                                <input id="website" name="website" value="<?= set_value('website') ?>" class="input-xlarge" placeholder="www.your-website.com" type="text">
                            </div>
                        </div>
                    </div>
                </div><!--/website-->
            </div>

            <!--upload image-->
            <div class="row-fluid">
                <div class="span6">
                    <div class="control-group">
                        <label class="control-label" for="photo">Gift photo </label>
                        <div class="controls">
                            <button id="" name="photo" class="btn" type="button">Select from your images</button>
                        </div>
                    </div>
                </div>
            </div><!--/upload image-->

            <!--submit-->
            <div class="row-fluid geFormSubmit">
                <div class="control-group">
                    <div class="controls">
                        <button class="btn btn-large btn-ge" type="submit">Post</button>
                    </div>
                </div>
            </div><!--/submit-->


        <? elseif ($step == Post::STEP_LOCATION): ?>


            <!--tagline-->
            <div class="row-fluid geFormTagline">
                <h4>Give your customers a reason to come in, give them something they appreciate</h4>
            </div><!--/tagline-->

            <!--terms-->
            <div class="row-fluid geFormTerms">
                <div class="control-group">
                    <label class="control-label" for="description">Terms & conditions</label>
                    <div class="controls">
                        <textarea id="description" name="description" rows="4"><?= set_value('description') ?></textarea>
                    </div>
                </div>
            </div><!--/terms-->

            <!--available day-->
            <div class="row-fluid">
                <div class="control-group">
                    <label class="control-label">Available</label>
                    <div class="controls">

                        <label class="checkbox inline" for="monday">
                            <input type="checkbox" name="availableDays[]" id="monday" value="<?= Ad_model::WEEKDAY_MONDAY ?>" <?= set_checkbox('availableDays', Ad_model::WEEKDAY_MONDAY) ?>>
                            Monday
                        </label>
                        <label class="checkbox inline" for="tuesday">
                            <input type="checkbox" name="availableDays[]" id="tuesday" value="<?= Ad_model::WEEKDAY_TUESDAY ?>" <?= set_checkbox('availableDays', Ad_model::WEEKDAY_TUESDAY) ?>>
                            Tuesday
                        </label>
                        <label class="checkbox inline" for="wednesday">
                            <input type="checkbox" name="availableDays[]" id="wednesday" value="<?= Ad_model::WEEKDAY_WEDNESDAY ?>" <?= set_checkbox('availableDays', Ad_model::WEEKDAY_WEDNESDAY) ?>>
                            Wednesday
                        </label>
                        <label class="checkbox inline" for="thursday">
                            <input type="checkbox" name="availableDays[]" id="thursday" value="<?= Ad_model::WEEKDAY_THURSDAY ?>" <?= set_checkbox('availableDays', Ad_model::WEEKDAY_THURSDAY) ?>>
                            Thursday
                        </label>
                        <label class="checkbox inline" for="friday">
                            <input type="checkbox" name="availableDays[]" id="friday" value="<?= Ad_model::WEEKDAY_FRIDAY ?>" <?= set_checkbox('availableDays', Ad_model::WEEKDAY_FRIDAY) ?>>
                            Friday
                        </label>
                        <label class="checkbox inline" for="saturday">
                            <input type="checkbox" name="availableDays[]" id="saturday" value="<?= Ad_model::WEEKDAY_SATURDAY ?>" <?= set_checkbox('availableDays', Ad_model::WEEKDAY_SATURDAY) ?>>
                            Saturday
                        </label>
                        <label class="checkbox inline" for="sunday">
                            <input type="checkbox" name="availableDays[]" id="sunday" value="<?= Ad_model::WEEKDAY_SUNDAY ?>" <?= set_checkbox('availableDays', Ad_model::WEEKDAY_SUNDAY) ?>>
                            Sunday
                        </label>

                        <label class="checkbox inline geColor0" for="all_week">
                            <input type="checkbox" id="all_week">
                            All week
                        </label>

                    </div><!--/controls-->
                </div>			
            </div><!--/available day-->

            <!--available time-->
            <div class="row-fluid">

                <!--time from-->
                <div class="span3">
                    <div class="control-group">
                        <label class="control-label" for="availableFromTime">&nbsp;</label>
                        <div class="controls">
                            <div class="input-append input-prepend">
                                <div class="input-append time" id="" data-time="12:02 AM" data-date-format="hh:mm tt">
                                    <span class="add-on">From:</span>
                                    <input class="span2" id="availableFromTime" name="availableFromTime" value="<?= set_value('availableFromTime') ?>" size="106" type="text">
                                    <span class="add-on"><i class="icon-time"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div><!--/time from-->

                <!--time until-->
                <div class="span3">
                    <div class="control-group">
                        <label class="control-label" for="availableToTime">&nbsp;</label>
                        <div class="controls">
                            <div class="input-append input-prepend">
                                <div class="input-append time" id="" data-time="12:02  AM" data-date-format="hh:mm tt">
                                    <span class="add-on">Until:</span>
                                    <input class="span2" id="availableToTime" name="availableToTime" value="<?= set_value('availableToTime') ?>" size="106" type="text">
                                    <span class="add-on"><i class="icon-time"></i></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div><!--/time until-->

                <!--all day-->
                <div class="span2">
                    <div class="control-group">
                        <label class="control-label">&nbsp;</label>
                        <div class="controls">
                            <label class="checkbox geColor0" for="availableAllDay">
                                <input type="checkbox" id="availableAllDay" name="availableAllDay" value="1" <?= set_checkbox('availableAllDay', '1', FALSE) ?>>
                                All day
                            </label>
                        </div>
                    </div>
                </div><!--/all day-->

                <!--appointment-->
                <div class="span4">
                    <div class="control-group">
                        <label class="control-label">&nbsp;</label>
                        <div class="controls">
                            <label class="checkbox" for="needReservation">
                                <input type="checkbox" name="needReservation" id="needReservation" value="1" <?= set_checkbox('needReservation', '1', FALSE) ?>>
                                Appointment/Reservation needed
                            </label>
                        </div>
                    </div>
                </div><!--/appointment-->

            </div><!--/available time-->

            <!--multiple locations-->
            <div class="row-fluid">
                <div class="span12">
                    <div class="control-group geBPUploadBlock">
                        <fieldset>
                            <label class="control-label geColor0">Location</label>
                            
                            <? if( !empty($user->addresses) ): ?>
                            <? $index = -1; ?>
                            <? foreach( $user->addresses as $address ): ?>
                                <?
                                $address_detail = $address->zipCode;
                                $index++;
                                ?>
                                
                                <!--location-->
                                <div class="row-fluid">

                                    <div class="span3">
                                        <div class="control-group">
                                            <div class="controls">
                                                <div class="input-prepend">
                                                    <span class="add-on">
                                                        <label class="checkbox" for="address_<?=$index?>">
                                                            <input type="checkbox" name="address[]" id="address_<?=$index?>" value="<?=$index?>" <?= set_checkbox('address', ''.$index, FALSE) ?>>
                                                            <?=$address_detail?>
                                                        </label>
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div><!--/location name-->

                                    <div class="span3">
                                        <div class="control-group">
                                            <div class="controls">
                                                <div class="input-prepend input-append">
                                                    <span class="add-on">Gifts: </span>
                                                    <input id="prependedtext" name="quantity[]" class="input-mini" placeholder="0" type="text" value="<?= set_value('quantity') ?>">
                                                    <span class="add-on">items</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div><!--/gifts nr.-->

                                    <div class="span2">
                                        <div class="control-group">
                                            <div class="controls">
                                                <button class="btn" type="button">Upload photo</button>
                                            </div>
                                        </div>
                                    </div><!--/photo-->

                                    <div class="span2">
                                        <div class="control-group">
                                            <div class="controls">
                                                <button class="btn" type="button">QR/Bar Code</button>
                                            </div>
                                        </div>
                                    </div><!--/qrbar code-->

                                </div><!--/location-->
                                
                            <? endforeach; ?>
                            <? endif; ?>
                            
                            <!--add location-->
                            <div class="row-fluid">
                                <div class="span12 geColor0">
                                    + Add new location
                                </div>
                            </div><!--/add location-->
                            
                            <!--add-->
                            <div class="row-fluid">

                                <div class="span2">
                                    <div class="control-group">
                                        <div class="controls">
                                            <input id="" name="add_address_name" class="input-medium" placeholder="Name" value="" type="text">
                                        </div>
                                    </div>
                                </div>
                                <div class="span2">
                                    <div class="control-group">
                                        <div class="controls">
                                            <input id="" name="add_address_city" class="input-medium" placeholder="City" value="" type="text">
                                        </div>
                                    </div>
                                </div>

                                <div class="span2">
                                    <div class="control-group">
                                        <div class="controls">
                                            <input id="" name="add_address_address" class="input-medium" placeholder="Address" value="" type="text">
                                        </div>
                                    </div>
                                </div>

                                <div class="span1">
                                    <div class="control-group">
                                        <div class="controls">
                                            <input id="" name="add_address_state" class="input-mini" placeholder="State" value="" type="text">
                                        </div>
                                    </div>
                                </div>
                                <div class="span1">
                                    <div class="control-group">
                                        <div class="controls">
                                            <input id="" name="add_address_zipCode" class="input-mini" placeholder="Zipcode" value="" type="text">
                                        </div>
                                    </div>
                                </div>

                                <div class="span2">
                                    <div class="control-group">
                                        <div class="controls">
                                            <button class="btn btn-ge" type="button">Add Location</button>
                                        </div>
                                    </div>
                                </div>

                            </div><!--/add-->

                        </fieldset>
                    </div>
                </div>
            </div><!--/multiple locations-->

            <!--submit-->
            <div class="row-fluid geFormSubmit">
                <div class="control-group">
                    <div class="controls">
                        <button class="btn btn-large btn-ge" type="submit">Post</button>
                    </div>
                </div>
            </div><!--/submit-->


        <? elseif ($step == Post::STEP_PREVIEW): ?>


            preview


        <? elseif ($step == Post::STEP_POST): ?>


            Error: <?= $error ?>


        <? endif; ?>

        <?= form_close() ?>
    </div>
</div><!--/content (form)-->
