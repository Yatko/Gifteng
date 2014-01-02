<?

/**
 * Input params:
 * 
 * 
 */

?>

<div class="snap-content ge-topspace">

<div class="container">
    <div class="row">
    	<div class="ge-welcome">
	        <div class="span12">
	            <div class="ge-stage" id="welcome_0">
	                <div class="row-fluid ge-headline">
	                    <p><b>Gifteng</b> is an invitation-only social community<br />where you can give and receive things you love for free</p>
	                </div><!--./ge-headline-->
	                <div id="topContent" class="row-fluid">
	                    <div class="span4">
	                        <div class="well ge-well" style="min-height: 330px;">
	                            <div class="row-fluid">
	                            	<div class="ge-item-image">
	                                	<img src="<?=BASE_PATH?>temp-sample/gift.jpg" class="img">
	                                </div>
	                            </div><!--./ge-item-image-->
	                            <div class="row-fluid">
	                            	<div class="ge-text">
		                                <h1 class="ge-title" style="font-size:160%;text-align:center;">Free Stuff</h1>
		                                <p>On Gifteng you can give away things you no longer need. Pick a friend or a neighbor and put a smile on their face.<br/><br/></p>
	                            	</div>
	                            </div>
	                        </div>
	                    </div><!--./-->
	                    <div class="span4">
	                        <div class="well ge-well" style="min-height: 330px;">
	                            <div class="row-fluid">
	                            	<div class="ge-item-image">
	                                	<img src="<?=BASE_PATH?>temp-sample/treasure.jpg" class="img">
	                                </div>
	                            </div><!--./ge-item-image-->
	                            <div class="row-fluid">
	                            	<div class="ge-text">
		                                <h1 class="ge-title" style="font-size:160%;text-align:center;">Freebies</h1>
		                                <p>Need a good book? Or a coffeemaker? How about a new pair of shoes? Find what you want by browsing through free stuff. They are all gifts!</p>
	                            	</div>
	                            </div>
	                        </div>
	                    </div><!--./-->
	                    <div class="span4">
	                        <div class="well ge-well">
	                            <div class="row-fluid">
	                            	<div class="ge-item-image ge-ribbon2">
		                                <div class="row-fluid" id="login_">
		                                    <div class="span10 offset1">
		                                        
		                                        <div id="form_wrapper" class="form_wrapper">
		                                            <?=form_open('/invitation/request/1', array('id' => 'requestInvitation', 'class' => 'request active'))?>
		                                            <div class="control-group">
		                                                <div class="controls">
		                                                    <div class="input">
		                                                        <input class="span2" name="invitation_email" type="text" placeholder="<?=lang('invitation_email_hint')?>" required="">
		                                                    </div>
		                                                </div>
		                                            </div>
		                                            <div class="control-group">
		                                                <div class="controls">
		                                                    <button type="submit" class="btn btn-block btn-primary btn-ge"><?=lang('invitation_request_button')?></button>
		                                                </div>
		                                            </div>
		                                            <div class="control-group">
		                                                <div class="controls">
		                                                    <label class="control-label text-center"><a rel="verify" class="linkform"><?=lang('invitation_invitation_invitation')?></a></label>
		                                                </div>
		                                            </div>
		                                            <?=form_close()?>
		                                            
		                                            <?=form_open('/invitation/verify/1', array('id' => 'verifyInvitation', 'class' => 'verify'))?>
		                                            <div class="control-group">
		                                                <div class="controls">
		                                                    <div class="input">
		                                                        <input class="span2" name="invitation_code" type="text" placeholder="<?=lang('invitation_code_hint')?>" required="">
		                                                    </div>
		                                                </div>
		                                            </div>
		                                            <div class="control-group">
		                                                <div class="controls">
		                                                    <button type="submit" class="btn btn-block btn-primary btn-ge"><?=lang('invitation_verify_button')?></button>
		                                                </div>
		                                            </div>
		                                            <div class="control-group">
		                                                <div class="controls">
		                                                    <label class="control-label text-center"><a rel="request" class="linkform"><?=lang('invitation_invitation_request')?></a></label>
		                                                </div>
		                                            </div>
		                                            <?=form_close()?>
		                                        </div>
		                                    </div>
		                                </div>
	                                </div><!--./ge-item-image-->
	                            </div>
	                            <div class="row-fluid">
	                            	<div class="ge-text">
		                                <h1 class="ge-title" style="font-size:160%;text-align:center;">Gifts</h1>
		                                <p>Giving helps you live longer. Generosity makes our world a better place. Sharing is caring... and it all starts with you.<br/><br/></p>
	                            	</div>
	                            </div>
	                        </div>
	                    </div><!--./-->
	                </div>
	            </div><!--./welcome_0-->
	
	            <div class="ge-stage" id="welcome_1">
	                <div class="row-fluid ge-headline">
	                    <p>&middot; make the world a giving place &middot;</p>
	                    <small>Watch our video to learn more</small>
	                </div><!--./ge-headline-->
	                <div class="row-fluid">
	                    <div class="span8 offset2">
	                        <div class="well ge-well" style="padding:19px;">
	                            <div id="wistia_4b9homs4ka" class="wistia_embed" style="width:580px;height:326px;" data-video-width="580" data-video-height="326">&nbsp;</div>
	                            <script charset="ISO-8859-1" src="http://fast.wistia.com/static/concat/E-v1.js"></script>
	                            <script>
	                                wistiaEmbed = Wistia.embed("4b9homs4ka", {
	                                    version: "v1",
	                                    videoWidth: 580,
	                                    videoHeight: 326,
	                                    volumeControl: true
	                                });
	                            </script>
	                        </div><!--./video embed-->
	                    </div>
	                </div>
	            </div><!--./welcome_1-->
	
	        </div>
    	</div>
    </div>
</div>

<hr class="ge-hr">

<div class="container">
    <div class="row">
    	<div class="ge-welcome">
	        <div class="span12">
	            <div class="ge-stage" id="welcome_2">
	                <div class="row-fluid">
	                	<div class="ge-headline">
	                    	<p>How does it work?</p>
	                    </div>
	                </div><!--./ge-headline-->
	                <div class="row-fluid">
	                    <div class="span2 offset2">
	                        <i class="icon ge-icon-giftbox"></i>
	                    </div>
	                    <div class="span6">
	                        <p class="lead">Freebies you see on Gifteng are individually posted by a member of Gifteng’s growing community or by a company who loves rewarding their customers with awesome gifts.</p>
	                    </div>
	                </div>
	                <div class="row-fluid"><div class="span8 offset2"><hr class="ge-hr-dotted"></div></div>
	                <div class="row-fluid">
	                    <div class="span2 offset2">
	                        <i class="icon ge-icon-check"></i>
	                    </div>
	                    <div class="span6">
	                        <p class="lead">As a Giftenger you have access to new Gifts - FREE stuff - on a daily basis, beginning at 12:00pm Eastern Time. (The preciousness of the gift is checked by our team before publishing.)</p>
	                    </div>
	                </div>
	                <div class="row-fluid"><div class="span8 offset2"><hr class="ge-hr-dotted"></div></div>
	                <div class="row-fluid">
	                    <div class="span2 offset2">
	                        <i class="icon ge-icon-heart"></i>
	                    </div>
	                    <div class="span6">
	                        <p class="lead">Currently, there is no need for points to request a Gift. But the more you give the higher your Generosity Score becomes. What does this mean? It means that the higher your score is, the more free stuff will be waiting for you.</p>
	                    </div>
	                </div>
	                <div class="row-fluid">
	                    <div class="span4 offset4"><br><br>
	                        <div class="control-group">
	                            <div class="controls">
	                            <a href="#topContent" class="btn btn-block btn-ge">REQUEST AN INVITATION</a>
	                            </div>
	                        </div>
	                    </div>	
	                </div>
	            </div><!--./welcome_2-->			
	        </div>
    	</div>
    </div>
</div>

<hr class="ge-hr">

<div class="container">
    <div class="row">
    	<div class="ge-welcome">
	        <div class="span12">
	            <div class="ge-stage" id="welcome_3">
	                <div class="row-fluid">
	                	<div class="ge-headline">
		                    <p>Give. Receive. Inspire.</p>
		                    <small>Gifts you can give (and receive)</small>
	                    </div>
	                </div><!--./ge-headline-->
	                <div class="row-fluid">
	                    <div class="span4">
	                        <div class="well ge-well ge-text" style="min-height: 330px;">
	                            <div class="row-fluid">
	                            	<div class="ge-item-image">
	                                	<img src="<?=BASE_PATH?>temp-sample/ge-img_1.jpg" class="img img-rounded">
	                                </div>
	                            </div><!--./ge-item-image-->
	                            <div class="row-fluid">
	                                <p class="lead">Help out other students with your textbooks 
	                                or put a smile on someone’s face with a good 
	                                novel that’s collecting dust on your shelf. </p>
	                            </div>
	                        </div>	
	                    </div><!--./-->
	                    <div class="span4">
	                        <div class="well ge-well ge-text" style="min-height: 330px;">
	                            <div class="row-fluid">
	                            	<div class="ge-item-image">
	                                	<img src="<?=BASE_PATH?>temp-sample/ge-img_2.jpg" class="img img-rounded">
	                               	</div>
	                            </div><!--./ge-item-image-->
	                            <div class="row-fluid">
	                                <p class="lead">Easily give away your new or used furniture 
	                                without the hassle of answering emails.</p>
	                            </div>
	                        </div>
	                    </div><!--./-->
	                    <div class="span4">
	                        <div class="well ge-well ge-text" style="min-height: 330px;">
	                            <div class="row-fluid">
	                            	<div class="ge-item-image">
	                                	<img src="<?=BASE_PATH?>temp-sample/ge-img_3.jpg" class="img img-rounded">
	                               </div>
	                            </div><!--./ge-item-image-->
	                            <div class="row-fluid">
	                                <p class="lead">Celebrate the arrival of your new phone, 
	                                camera, laptop, or television by gifting your 
	                                old one to someone in need.</p>
	                            </div>
	                        </div>
	                    </div><!--./-->	
	                </div>
	                <div class="row-fluid">
	                    <div class="span4">
	                        <div class="well ge-well ge-text" style="min-height: 330px;">
	                            <div class="row-fluid">
	                            	<div class="ge-item-image">
	                                	<img src="<?=BASE_PATH?>temp-sample/ge-img_4.jpg" class="img img-rounded">
	                               </div>
	                            </div><!--./ge-item-image-->
	                            <div class="row-fluid">
	                                <p class="lead">They say a girl can never have enough 
	                                shoes. It's time to get rid of the 
	                                ones you don’t use. Let's find some 
	                                new ones!</p>
	                            </div>
	                        </div>	
	                    </div><!--./-->
	                    <div class="span4">
	                        <div class="well ge-well ge-text" style="min-height: 330px;">
	                            <div class="row-fluid">
	                            	<div class="ge-item-image">
	                                	<img src="<?=BASE_PATH?>temp-sample/ge-img_5.jpg" class="img img-rounded">
	                               </div>
	                            </div><!--./ge-item-image-->
	                            <div class="row-fluid">
	                                <p class="lead">Instead of selling your clothing, why not 
	                                give it away and request something 
	                                you need in return?</p>
	                            </div>
	                        </div>
	                    </div><!--./-->
	                    <div class="span4">
	                        <div class="well ge-well ge-text" style="min-height: 330px;">
	                            <div class="row-fluid">
	                            	<div class="ge-item-image">
	                                	<img src="<?=BASE_PATH?>temp-sample/ge-img_6.jpg" class="img img-rounded">
	                               </div>
	                            </div><!--./ge-item-image-->
	                            <div class="row-fluid">
	                                <p class="lead">Make someone happy with the things 
	                                you got bored of. (And discover some great 
	                                freebies from others around you.)</p>
	                            </div>
	                        </div>
	                    </div><!--./-->	
	                </div>
	                <div class="row-fluid">
	                	<div class="ge-headline">
	                    	<small><a href="#topContent">Give something that has value that someone might need.</a></small>
	                    </div>
	                </div><!--./ge-headline-->
	            </div>
	        </div>
    	</div>
    </div>
</div>

<div class="ge-highlight">
    <div class="container">
        <div class="row">
        	<div class="ge-welcome">
	            <div class="span12">
	                <div class="ge-stage" id="welcome_4">
	                    <div class="row-fluid">
	                        <div class="span6">
	                            <p>We welcome Businesses</p>
	                            A beautifully simple way to reach a customer's heart is with a Gift.
	                        </div>
	                        <div class="span5 offset1">
	                            <div class="control-group">
	                                <div class="controls">
	                                    <a href="<?=base_url()?>registration" class="btn btn-block btn-ge">Create Your Free Business Account</a>
	                                </div>
	                            </div>
	                        </div>
	                    </div>
	                </div><!--./welcome_4-->
	            </div>
        	</div>
        </div>
    </div>
</div>

<div class="container">
    <div class="row">
    	<div class="ge-welcome">
	        <div class="span12">
	            <div class="ge-stage" id="welcome_5">
	                <div class="row-fluid">
	                	<div class="ge-headline">
	                    	<small>Our mission is to make the world a giving place. Our biggest inspiration are people who appreciate what they have and are willing to give to others, especially those in need. The ability to provide a place where they can get together is delightful.</small>
	                	</div><!--./ge-headline-->
	                </div>
	            </div>
	        </div>
        </div>
    </div>
</div>



<? /** ?>
<div class="container"><h2>
    <?=lang('index_welcome')?>
</h2></div>

<div class="form">
    <div class="container">
        <div id="wistia_4b9homs4ka" class="wistia_embed" style="width:400px;height:225px;" data-video-width="400" data-video-height="225">&nbsp;</div>
        <script charset="ISO-8859-1" src="http://fast.wistia.com/static/concat/E-v1.js"></script>
        <script>
            wistiaEmbed = Wistia.embed("4b9homs4ka", {
                version: "v1",
                videoWidth: 400,
                videoHeight: 225,
                volumeControl: true
            });
        </script>
    </div>
</div>

<div class="form"><div class="container containerDefaultSize">
    <div id="form_wrapper" class="form_wrapper">
        <?=form_open('/invitation/request/1', 'id="requestInvitation" class="request active"')?>
        <div>
            <div class="loginBox"><input name="invitation_email" type="text" class="textbox" title="<?=lang('invitation_email_hint')?>" placeholder="<?=lang('invitation_email_hint')?>"></div>
            <div class="loginBox"><input type="submit" value="<?=lang('invitation_request_button')?>" class="green"></div>
        </div>
        <div class="contentBox">
            <div><a rel="verify" class="red linkform"><?=lang('invitation_invitation_invitation')?></a></div>
        </div>
        <?=form_close()?>

        <?=form_open('/invitation/verify/1', 'id="verifyInvitation" class="verify"')?>
        <div>
            <div class="loginBox"><input name="invitation_code" value="" type="text" class="textbox" title="<?=lang('invitation_code_hint')?>" placeholder="<?=lang('invitation_code_hint')?>"></div>
            <div class="loginBox"><input type="submit" value="<?=lang('invitation_verify_button')?>" class="red"></div>
        </div>
        <div class="contentBox">
            <div><a rel="request" class="red linkform"><?=lang('invitation_invitation_request')?></a></div>
        </div>
        <?=form_close()?>
    </div>
</div></div>

<div class="container">
    <h1 class="green"><?=lang('index_slogan')?></h1>
</div>

<div class="container">
    <a href="http://facebook.com/gifteng" target="_blank" title="Gifteng on facebook"><div class="share facebook"></div></a>
    <a href="http://twitter.com/gifteng" target="_blank" title="Gifteng on twitter"><div class="share twitter"></div></a>
    <!-- <a href="#" target="_blank" title=""><div class="share email"></div></a> -->
    <a href="http://linkedin.com/in/gifteng" target="_blank" title="Gifteng on LinkedIn"><div class="share linkedin"></div></a>
</div>
<? /**/ ?>
