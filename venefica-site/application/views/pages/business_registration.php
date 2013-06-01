<div class="span12  geBox geLargeBox">
    <div class="row-fluid">
        <div class="span12 text-center">
            <!-- Form Name -->
            <legend>
                <h3>GIFTENG FOR YOUR BUSINESS</h3>
                <small>Beautifully simple way to reach your customer’s heart.</small>
            </legend>

        </div>
    </div>

    <div class="row-fluid">

        <div class="span5 offset1 geBWelcome">

            <!--space--><div class="row-fluid geSpacer"></div>

            <div class="row-fluid">
                <div class="span6 text-right">
                    <p style="height:1px ;"></p>
                    The quickest and easiest way to attain loyal customers is by making them fall in love with your product or service.
                </div>
                <div class="span6">
                    <img src="<?=BASE_PATH?>temp-sample/item.jpg" class="img-rounded">
                </div>
            </div>

            <!--space--><div class="row-fluid geSpacer"></div>

            <div class="row-fluid">
                <div class="span6">
                    <img src="<?=BASE_PATH?>temp-sample/item.jpg" class="img-rounded">
                </div>
                <div class="span6">
                    <p style="height:10px ;"></p>
                    No need to waste any more money on deal sites, we have a better way to bring you closer to them.
                </div>
            </div>

            <!--space--><div class="row-fluid geSpacer"></div>

            <div class="row-fluid">
                <div class="span6 text-right">
                    <p style="height:30px ;"></p>
                    All you have to do is set up an offer!
                </div>
                <div class="span6">
                    <img src="<?=BASE_PATH?>temp-sample/item.jpg" class="img-rounded">
                </div>
            </div>

            <!--space--><div class="row-fluid geSpacer"></div>

            <div class="row-fluid">
                <div class="span6">
                    <img src="<?=BASE_PATH?>temp-sample/item.jpg" class="img-rounded geProfileImage">
                </div>
                <div class="span6">
                    <p style="height:10px ;"></p>
                    Your business will be seen by thousands of connected users who love to support local businesses.
                </div>
            </div>

        </div>

        <!--content-->
        <div class="span4 offset1 gePostPox">
            <?=form_open('/registration/business', array('class' => "form"))?>
            <?=isset($this->registration_form) ? $this->registration_form->error_string() : ""?>
            
                <fieldset>

                    <p class="text-center">Create your FREE account now and discover how<br />Gifteng can help your business grow</p>

                    <!-- Text input-->
                    <div class="control-group">
                        <div class="controls">
                            <input name="businessName" value="<?=set_value('businessName')?>" type="text" placeholder="Business name" class="input-block-level" required="">
                        </div>
                    </div>

                    <!-- Text input-->
                    <div class="control-group">
                        <div class="controls">
                            <input name="contactName" value="<?=set_value('contactName')?>" type="text" placeholder="Contact name" class="input-block-level" required="">

                        </div>
                    </div>

                    <!-- Text input-->
                    <div class="control-group">
                        <div class="controls">
                            <input name="phoneNumber" value="<?=set_value('phoneNumber')?>" type="text" placeholder="Phone number" class="input-block-level" required="">

                        </div>
                    </div>

                    <!-- Text input-->
                    <div class="control-group">
                        <div class="controls">
                            <input name="email" value="<?=set_value('email')?>" type="text" placeholder="Email address" class="input-block-level" required="">

                        </div>
                    </div>

                    <!-- Text input-->
                    <div class="control-group">
                        <div class="controls">
                            <input name="password_1" type="password" placeholder="Password" class="input-block-level" required="">

                        </div>
                    </div>

                    <!-- Text input-->
                    <div class="control-group">
                        <div class="controls">
                            <input name="password_2" type="password" placeholder="Confirm password" class="input-block-level" required="">

                        </div>
                    </div>

                    <!-- Text input-->
                    <div class="control-group">
                        <div class="controls">
                            <div class="input-append">
                                <input name="zipCode" value="<?=set_value('zipCode')?>" type="text" placeholder="Zip code" class="input-medium" required="">
                                <span class="add-on"><i class="icon-ok icon-green"></i></span>
                            </div>
                        </div>
                    </div>

                    <!-- Select Basic -->
                    <div class="control-group">
                        <div class="controls">
                            <select name="businessCategory" class="input-block-level">
                                <option value="">Select category ...</option>
                                <? foreach ( $categories as $category ): ?>
                                <?
                                    $category_id = $category->id;
                                    $category_name = $category->name;
                                ?>
                                
                                <option value="<?=$category_id?>" <?=set_select('businessCategory', $category_id)?>><?=$category_name?></option> 
                                <? endforeach; ?>
                            </select>
                        </div>
                    </div>


                    <!--space--><div class="row-fluid geSpacer"></div>

                    <!-- Button -->
                    <div class="control-group">
                        <div class="controls">
                            <button type="submit" class="btn btn-large btn-ge btn-block">CREATE YOUR FREE<br />BUSINESS ACCOUNT</button>
                        </div>
                    </div>

                </fieldset>
            <?=form_close()?>

        </div><!--/geBox-->

    </div>
</div>
