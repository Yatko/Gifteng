$(document).ready(function() {
    //pretty check and radio boxes
    $('input[type=checkbox]').prettyCheckboxes();
    $('input[type=radio][class=radionInline]').prettyCheckboxes({
        'display': 'inline'
    });
    $('input[type=radio][class=radioList]').prettyCheckboxes();
    
    //chosen selectbox
    $(".chzn-select").chosen({
        no_results_text: "No results matched",
        disable_search: true,
        allow_single_deselect: false
    });
    
    //textbox hint
    $('input[title != ""]').hint();
    
    $('#about').click(function() {
        $("#navigator2").slideToggle(350, "backinout");
    });
});
