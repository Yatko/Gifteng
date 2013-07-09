<script language="javascript">
    $(function() {
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
                var cloned = $(source).clone(true, true)[0]; //TODO: what if there is no address to clone

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
