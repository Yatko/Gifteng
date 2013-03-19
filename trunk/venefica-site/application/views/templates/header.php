<!DOCTYPE html>
<html>
<head>
    <title><?=lang('main_title')?></title>
    
    <meta charset="utf-8"/>
    <meta content='text/html; charset=UTF-8' http-equiv='Content-Type'/>
    
    <link rel="shortcut icon" href="assets/img/favicon.ico" >
    
    <link rel='stylesheet' type='text/css' media='all' href="assets/css/style.css" />
    <link rel='stylesheet' type='text/css' media='all' href="assets/css/tabs.css" />
    
    <script src="assets/js/jquery-1.9.1.min.js"></script>
    <script src="assets/js/jquery-easing.1.2.js"></script>
    <script src="assets/js/jquery-easing-compatibility.1.2.js"></script>
    <script src="assets/js/coda-slider.1.1.1.js"></script>
    <script src="assets/js/textbox-hint.js"></script>
    
    <script language="javascript">
        $(function() {
            // find all the input elements with title attributes
            $('input[title != ""]').hint();
        });
    </script>
</head>
<body>
    <div id="header">
        <div>
            <div class="logo"></div>
            <div class="slogan"></div>
        </div>
    </div> <!-- end header -->
    
    <div id="container">
