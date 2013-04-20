<? /**/ ?>
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

<script langauge="javascript">
    function fbs_click(width, height) {
        //Allow for borders.
        var leftPosition = (window.screen.width / 2) - ((width / 2) + 10);
        //Allow for title and status bars.
        var topPosition = (window.screen.height / 2) - ((height / 2) + 50);
        var windowFeatures = "status=no,height=" + height + ",width=" + width + ",resizable=yes,left=" + leftPosition + ",top=" + topPosition + ",screenX=" + leftPosition + ",screenY=" + topPosition + ",toolbar=no,menubar=no,scrollbars=no,location=no,directories=no";
        var u = location.href;
        var t = document.title;
        window.open('http://www.facebook.com/sharer.php?u=' + encodeURIComponent(u) + '&t=' + encodeURIComponent(t), 'sharer', windowFeatures);
        return false;
    }
</script>

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

<div class="form">
    <div class="container" style="width: 300px; min-height: 225px;">
        <div class="container" id="login" style="padding-top:30px;">
            
            <? /** ?>
            <?=form_open('/invitation/request/1')?>
            <div>
                <div class="loginBox"><input name="invitation_email" type="text" class="textbox" title="<?=lang('index_invitation_email_hint')?>" placeholder="<?=lang('index_invitation_email_hint')?>"></div>
                <div class="loginBox"><input type="submit" value="<?=lang('index_invitation_request_button')?>" class="green"></div>
            </div>
            <?=form_close()?>
            <? /**/ ?>
            
            <? /**/ ?>
            <div id="form_wrapper" class="form_wrapper">
                <?=form_open('/invitation/request/1', 'id="requestInvitation" class="request active"')?>
                <div>
                    <div class="loginBox"><input name="invitation_email" type="text" class="textbox" title="<?=lang('index_invitation_email_hint')?>" placeholder="<?=lang('index_invitation_email_hint')?>"></div>
                    <div class="loginBox"><input type="submit" value="<?=lang('index_invitation_request_button')?>" class="green"></div>
                </div>
                <div class="contentBox">
                    <div><a rel="verify" class="red linkform"><?=lang('invitation_invitation_invitation')?></a></div>
                </div>
                <?=form_close()?>
                
                <?=form_open('/invitation/verify/1', 'id="verifyInvitation" class="verify"')?>
                <div>
                    <div class="loginBox"><input name="invitation_code" value="" type="text" class="textbox" title="<?=lang('index_invitation_code_hint')?>" placeholder="<?=lang('index_invitation_code_hint')?>"></div>
                    <div class="loginBox"><input type="submit" value="<?=lang('index_invitation_verify_button')?>" class="red"></div>
                </div>
                <div class="contentBox">
                    <div><a rel="request" class="red linkform"><?=lang('invitation_invitation_request')?></a></div>
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
    <a href="http://www.facebook.com/share.php?u=http://gifteng.com" onClick="return fbs_click(400, 300);" target="_blank" title="Share on facebook"><div class="share facebook"></div></a>
    <a href="http://twitter.com/share?text=-%20make%20the%20world%20a%20giving%20place%20-&url=http://www.gifteng.com" target="_blank" title="Gifteng on twitter"><div class="share twitter"></div></a>
    <a href="#" target="_blank" title=""><div class="share email"></div></a>
    <!--<a href="http://linkedin.com/in/gifteng" target="_blank" title="Gifteng on LinkedIn"><div class="share linkedin"></div></a>-->
</div>