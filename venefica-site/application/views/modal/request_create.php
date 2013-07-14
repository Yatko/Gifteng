<script langauge="javascript">
    var requestCallerElement;
    
    function startRequestModal(callerElement, type, adId) {
        requestCallerElement = callerElement;
        
        var $requestAdId = $("#" + type + "_request_post_form input[name=requestAdId]");
        $requestAdId.val(adId);
        
        $("#member_request_post_form").removeClass('hide').addClass('hide');
        $("#business_request_post_form").removeClass('hide').addClass('hide');
        $("#" + type + "_request_post_form").removeClass('hide');
        
        if ( $('#requestContainer').length > 0 ) {
            $('#requestContainer').modal('show');
        }
    }
    
    function requestModal(type) {
        if ( $("#" + type + "_request_post_form").length === 0 ) {
            return;
        }

        var $requestAdId = $("#" + type + "_request_post_form input[name=requestAdId]");
        var $requestText = $("#" + type + "_request_post_form textarea[name=requestText]");

        $.ajax({
            type: 'POST',
            url: '<?=base_url()?>ajax/request',
            dataType: 'json',
            cache: false,
            data: {
                requestAdId: $requestAdId.val(),
                requestText: $requestText.val()
            }
        }).done(function(response) {
            if ( !response || response === '' ) {
                //TODO: empty result
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_ERROR?>') ) {
                //TODO
            } else if ( response.hasOwnProperty('<?=AJAX_STATUS_RESULT?>') ) {
                var adId = $requestAdId.val();
                $requestAdId.val('');
                $requestText.val('');

                if ( requestCallerElement !== null ) {
                    var $element = $(requestCallerElement);
                    $element.trigger('request_created', [adId]);
                }

                if ( $('#requestContainer').length > 0 ) {
                    $('#requestContainer').modal('hide');
                }
            } else {
                //TODO: unknown response received
            }
        }).fail(function(data) {
            //TODO
        });
    }
    
    function showRequestStep1() {
        $('#request_step_1').removeClass('hide');
        $('#request_step_2').addClass('hide');
    }
    function showRequestStep2() {
        $('#request_step_1').addClass('hide');
        $('#request_step_2').removeClass('hide');
    }
    
    $(function() {
        $('#requestContainer').on('hidden', function() {
            showRequestStep1();
        });
    });
</script>



<div id="requestContainer" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-body">

    <?=form_open('/ajax/request', array('id' => 'business_request_post_form'))?>
        <input type="hidden" name="requestAdId"/>
        <input type="hidden" name="requestText"/>

        <label class="control-label" for="fieldset">
            <blockquote>
                <p>Yippy! You are about to request a gift! Because its a "Company Gift" your request is instantly accepted.</p>
            </blockquote>
        </label>
        <fieldset>
            <div class="row-fluid">
                <div class="span12">
                    <div class="control-group control-form">
                        <div class="controls">
                            <button type="button" class="span4 btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
                            <button onclick="requestModal('business');" type="button" class="span8 btn btn-ge">Send Request</button>
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>

    <?=form_close()?>

    <?=form_open('/ajax/request', array('id' => 'member_request_post_form'))?>
        <input type="hidden" name="requestAdId"/>

        <div id="request_step_1">
            <label class="control-label" for="fieldset">
                <blockquote>
                    <p>Yippy! You are about to request a gift! Because it’s a "Member’s Gift" your request will be pending until the giver accepts it.</p>
                </blockquote>
            </label>
            <fieldset>
                <div class="row-fluid">
                    <div class="span12">
                        <div class="control-group control-form">
                            <div class="controls">
                                <button onclick="showRequestStep2();" type="button" class="span8 pull-right btn btn-ge" >Got it!</button>
                            </div>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>

        <div id="request_step_2" class="hide">
            <fieldset>
                <div class="row-fluid ge-input">
                    <div class="span12">
                        <div class="row-fluid">
                            <div class="span12 ge-text">
                                <textarea name="requestText" placeholder="Your message ...">I would love to have your gift! Please accept my request!</textarea>
                            </div>
                        </div>
                    </div>
                </div><!--./ge-input-->
                <div class="row-fluid">
                    <div class="span12">
                        <div class="control-group control-form">
                            <div class="controls">
                                <button type="button" class="span4 btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
                                <button onclick="requestModal('member');" type="button" class="span8 btn btn-ge">Send Request</button>
                            </div>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>

    <?=form_close()?>

    </div>
</div>
