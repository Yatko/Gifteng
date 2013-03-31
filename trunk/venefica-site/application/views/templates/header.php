<?php

$base_path  = base_url().'assets/';
$js_path    = $base_path.'js/';
$css_path   = $base_path.'css/';
$img_path   = $base_path.'img/';
$font_path  = $base_path.'font/';

?>

<!DOCTYPE html>
<html>
<head>
    <title><?=lang('main_title')?></title>
    
    <meta charset="utf-8"/>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>
    <meta name="description" content="Gifteng - make the world a giving place" />
    
    <link rel="shortcut icon" href="<?=$img_path?>favicon.ico" >
    
    <link rel='stylesheet' type='text/css' media='all' href="<?=$css_path?>fonts.css" />
    <link rel='stylesheet' type='text/css' media='all' href="<?=$css_path?>style.css" />
    <link rel='stylesheet' type='text/css' media='all' href="<?=$css_path?>coda-slider.css" />
    <link rel='stylesheet' type='text/css' media='all' href="<?=$css_path?>prettyCheckboxes.css" />
    <link rel='stylesheet' type='text/css' media='all' href="<?=$css_path?>chosen.css" />
    
    <script src="<?=$js_path?>jquery-1.9.1.min.js"></script>
    <script src="<?=$js_path?>jquery-migrate-1.1.1.min.js"></script>
    <script src="<?=$js_path?>jquery-easing.1.2.js"></script>
    <script src="<?=$js_path?>jquery-easing-compatibility.1.2.js"></script>
    
    <!-- sliding tabs -->
    <script src="<?=$js_path?>jquery.coda-slider-3.0.min.js"></script>
    
    <!-- placeholder for textboxes -->
    <script src="<?=$js_path?>textbox-hint.js"></script>
    
    <!-- radio and checkbox beautifier -->
    <script src="<?=$js_path?>prettyCheckboxes.js"></script>
    
    <!-- multi selection/chooser -->
    <script src="<?=$js_path?>chosen.jquery.min.js"></script>
    
    <!-- infinite scroll -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!--[if lt IE 9]><script src="assets/js/html5.js"></script><![endif]-->
    <script src="<?=$js_path?>jquery.masonry.min.js"></script>
    <script src="<?=$js_path?>jquery.infinitescroll.min.js"></script>
    <script src="<?=$js_path?>modernizr-transitions.js"></script>
    
    
    <script src="<?=$js_path?>common.js"></script>
</head>
<body>
    <div id="header">
        <div>
            <div class="logo"></div>
            <div class="slogan"></div>
        </div>
    </div> <!-- end header -->
    
    <div id="container">
