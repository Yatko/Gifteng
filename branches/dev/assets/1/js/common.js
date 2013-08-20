$(document).ready(function() {
    $('input[type=checkbox]').prettyCheckboxes();
    $('input[type=radio][class=radionInline]').prettyCheckboxes({
        'display': 'inline'
    });
    $('input[type=radio][class=radioList]').prettyCheckboxes();
    $(".chzn-select").chosen({
        no_results_text: "No results matched"
    });
});

$(function() {
    // find all the input elements with title attributes
    $('input[title != ""]').hint();
});
