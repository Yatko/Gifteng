$(document).ready(function() {
    //pretty check and radio boxes
    $('input[type=checkbox]').prettyCheckboxes();
    $('input[type=radio][class=radionInline]').prettyCheckboxes({
        'display': 'inline'
    });
    $('input[type=radio][class=radioList]').prettyCheckboxes();
    
    //chosen selectbox
    $(".chzn-select").chosen({
        no_results_text: "No results matched"
    });
    
    //textbox hint
    $('input[title != ""]').hint();
});
