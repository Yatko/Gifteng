<? if( isset($registration_success) && $registration_success ): ?>

<div class="container ge-container">
    <div class="row ge-business">
        <div class="span12">
            <div class="well ge-well">
                <div class="row-fluid ge-headline ge-topspace ge-bottomspace">
                    <h4>THANK YOU!</h4>
                    <p>We're doing our best to review all incoming inquiries and promise to get in touch soon.</p>
                </div><!--./ge-headline-->
            </div>
        </div>
    </div>
</div>

<? else: ?>

<div class="container ge-container">
<div class="row ge-business">
    <div class="span12">
        <div class="well ge-well ge-topspace ge-bottomspace">

            <div class="row-fluid ge-headline">
                <h4>Gifteng for your business</h4>
                <p>Beautifully simple way to reach your customer’s heart</p>
            </div><!--./ge-headline-->
            
            <div class="row-fluid">
                <div class="span5 offset1 ge-welcome">
                    <!--welcome--->
                    <div class="row-fluid">
                        <div class="span6 text-right">
                            <p style="height:1px ;"></p>
                            The quickest and easiest way to attain loyal customers is by having them fall in love with your product or service.
                        </div>
                        <div class="span6">
                            <img src="<?=BASE_PATH?>temp-sample/ge-img_1business.jpg" class="img-rounded">
                        </div>
                    </div>

                    <!--space-->
                    <div class="row-fluid ge-spacer"></div>

                    <div class="row-fluid">
                        <div class="span6">
                            <img src="<?=BASE_PATH?>temp-sample/ge-img_2business.jpg" class="img-rounded">
                        </div>
                        <div class="span6">
                            <p style="height:10px ;"></p>
                            No need to waste any more money on deal sites; we have found you a simple method for engagement.
                        </div>
                    </div>

                    <!--space-->
                    <div class="row-fluid ge-spacer"></div>

                    <div class="row-fluid">
                        <div class="span6 text-right">
                            <p style="height:30px ;"></p>
                            All you have to do is set up your account on Gifteng.com by following two easy steps.
                        </div>
                        <div class="span6">
                            <img src="<?=BASE_PATH?>temp-sample/ge-img_3business.jpg" class="img-rounded">
                        </div>
                    </div>

                    <!--space-->
                    <div class="row-fluid ge-spacer"></div>

                    <div class="row-fluid">
                        <div class="span6">
                            <img src="<?=BASE_PATH?>temp-sample/ge-img_4business.jpg" class="img-rounded">
                        </div>
                        <div class="span6">
                            <p style="height:10px ;"></p>
                            Your business will be seen by thousands of users who love to support local businesses.
                        </div>
                    </div>
                    <!--end welcome-->
                </div><!--./ge-welcome-->
                
                <div class="span4 offset1 ge-form">
                    <label class="control-label" for="fieldset">Create your FREE account now and discover how Gifteng can help your business grow:</label>
                    
                    <?=form_open('/registration/business', array('class' => "form"))?>
                    <?=isset($this->registration_form) ? $this->registration_form->error_string() : ""?>
                    <fieldset>
                        
                        <div class="control-group">
                            <div class="controls">
                                <input name="businessName" value="<?=set_value('businessName')?>" type="text" placeholder="Business name" class="input-block-level" required="">
                            </div>
                        </div>

                        <div class="control-group">
                            <div class="controls">
                                <input name="contactName" value="<?=set_value('contactName')?>" type="text" placeholder="Contact name" class="input-block-level" required="">
                            </div>
                        </div>

                        <div class="control-group">
                            <div class="controls">
                                <input name="phoneNumber" value="<?=set_value('phoneNumber')?>" type="text" placeholder="Phone number" class="input-block-level" required="">
                            </div>
                        </div>

                        <div class="control-group">
                            <div class="controls">
                                <input name="email" value="<?=set_value('email')?>" type="text" placeholder="Email address" class="input-block-level" required="">
                            </div>
                        </div>

                        <div class="control-group">
                            <div class="controls">
                                <input name="password_1" type="password" placeholder="Password" class="input-block-level" required="">
                            </div>
                        </div>

                        <div class="control-group">
                            <div class="controls">
                                <input name="password_2" type="password" placeholder="Confirm password" class="input-block-level" required="">
                            </div>
                        </div>

                        <div class="control-group">
                            <div class="controls">
                                <div class="input">
                                    <input name="zipCode" value="<?=set_value('zipCode')?>" type="text" maxlength="5" placeholder="Zip code" class="span2" required="">
                                </div>
                            </div>
                        </div>

                        <div class="control-group">
                            <div class="controls">
                                <select name="businessCategory" class="select-block mbl select-info">
                                    <option>Select category ...</option>
                                    
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

                        <div class="control-group">
                            <div class="controls">
                                <button type="submit" class="btn btn-large btn-ge btn-block ge-submit">CREATE YOUR FREE<br />BUSINESS ACCOUNT</button>
                            </div>
                        </div>

                    </fieldset>
                    <?=form_close()?>
                </div><!--./ge-form-->
            </div>
            
        </div>
    </div><!--./ge-form-->
</div>
</div>

<? endif; ?>