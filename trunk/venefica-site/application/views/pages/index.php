<? /** ?>
<script langauge="javascript">
    $(function() {
        //the form wrapper (includes all forms)
        var $form_wrapper = $('#form_wrapper');
        //the current form is the one with class active
        var $currentForm = $form_wrapper.children('form.active');
        //the change form links
        var $linkform = $form_wrapper.find('.linkform');
        
        //get width and height of each form and store them for later						
        $form_wrapper.children('form').each(function(i) {
            var $theForm = $(this);
            //solve the inline display none problem when using fadeIn fadeOut
            if ( !$theForm.hasClass('active') ) {
                $theForm.hide();
            }
            $theForm.data({
                width: $theForm.width(),
                height: $theForm.height()
            });
        });
        
        //set width and height of wrapper (same of current form)
        $form_wrapper.css({
            width: $currentForm.data('width') + 'px',
            height: $currentForm.data('height') + 'px'
        });
        
        //clicking a link (change form event) in the form
        //makes the current form hide.
        //The wrapper animates its width and height to the 
        //width and height of the new current form.
        //After the animation, the new form is shown
        $linkform.bind('click', function(e){
            var $link = $(this);
            var target = $link.attr('rel');
            $currentForm.fadeOut(400, function() {
                //remove class active from current form
                $currentForm.removeClass('active');
                //new current form
                $currentForm= $form_wrapper.children('form.' + target);
                //animate the wrapper
                $form_wrapper.stop().animate({
                    width: $currentForm.data('width') + 'px',
                    height: $currentForm.data('height') + 'px'
                }, 150, function() {
                    //new form gets class active
                    $currentForm.addClass('active');
                    //show the new form
                    $currentForm.fadeIn(250);
                });
            });
            e.preventDefault();
        });
    });
</script>
<? /**/ ?>

<div class="container"><h2>
    <?=lang('index_welcome')?>
</h2></div>

<div class="form">
    <div class="container">
        <div id="wistia_vhti7jwll8" class="wistia_embed" style="width:400px;height:227px;" data-video-width="400" data-video-height="227">&nbsp;</div>
        <script charset="ISO-8859-1" src="http://fast.wistia.com/static/concat/E-v1.js"></script>
        <script>
        wistiaEmbed = Wistia.embed("vhti7jwll8", {
            version: "v1",
            videoWidth: 400,
            videoHeight: 227,
            volumeControl: true,
        });
        </script>
    </div>
</div>

<div class="form">
    <div class="container" style="width: 300px; min-height: 229px;">
        <div class="container" id="login">
            
            <?=form_open('/invitation/request/1')?>
            
            <div class="formBox">
                <div><input name="invitation_email" type="text" class="textbox" title="<?=lang('index_invitation_email_hint')?>" placeholder="<?=lang('index_invitation_email_hint')?>"></div>
                <div><input type="submit" value="<?=lang('index_invitation_request_button')?>" class="green"></div>
            </div>

            <?=form_close()?>
            
            <? /** ?>
            <div id="form_wrapper" class="form_wrapper">
                <?=form_open('#', 'id="requestInvitation" class="request active"')?>
                <div class="formBox">
                    <div><input name="invitation_email" value="" type="text" class="textbox" title="Email adress" placeholder="Email adress"></div>
                    <div><input type="submit" value="REQUEST AN INVITATION" class="green"></div>
                </div>
                <div class="contentBox">
                    <div><a rel="verify" class="red linkform">I have an invitation</a></div>
                </div>
                <?=form_close()?>
                
                <?=form_open('#', 'id="verifyInvitation" class="verify"')?>
                <div class="formBox">
                    <div><input name="invitation_code" value="" type="text" class="textbox" title="Enter your invitation code" placeholder="Enter your invitation code"></div>
                    <div><input type="submit" value="VERIFY INVITATION" class="red"></div>
                </div>
                <div class="contentBox">
                    <div><a rel="request" class="red linkform">Request an invitation</a></div>
                </div>
                <?=form_close()?>
            </div>
            <? /**/ ?>
            
        </div>
    </div>
</div>

<div class="container"><h1 class="green">
    <?=lang('index_slogan')?>
</h1></div>

<div class="container">
    <img src="assets/<?=TEMPLATES?>/img/facebook.png"/>
    <img src="assets/<?=TEMPLATES?>/img/twitter.png"/>
    <img src="assets/<?=TEMPLATES?>/img/email.png"/>
</div>
