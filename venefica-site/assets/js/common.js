$(document).ready(function() {
    $('input[type=checkbox],input[type=radio]').prettyCheckboxes();
});

$(function() {
    // find all the input elements with title attributes
    $('input[title != ""]').hint();
});
