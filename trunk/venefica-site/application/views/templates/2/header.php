<?php

$base_path  = base_url().'assets/'.TEMPLATES.'/';
$js_path    = $base_path.'js/';
$css_path   = $base_path.'css/';
$img_path   = $base_path.'img/';
//$font_path  = $base_path.'font/';

?>

<!DOCTYPE html>
<html>
<head>
    <title><?=lang('main_title')?></title>
    
    <meta charset="utf-8"/>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>
    <meta name="description" content="Gifteng - make the world a giving place" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    
    <link rel="shortcut icon" href="<?=$img_path?>favicon.ico" >
    
    <link rel='stylesheet' type='text/css' href='http://fonts.googleapis.com/css?family=PT+Sans:400,700,400italic,700italic'>
    <link rel='stylesheet' type='text/css' href='http://fonts.googleapis.com/css?family=PT+Sans+Narrow:400,700'>
    <link rel='stylesheet' type='text/css' href='http://fonts.googleapis.com/css?family=Lato:100,300,400,700,900,100italic,300italic,400italic,700italic,900italic'>
    
    <link rel='stylesheet' type='text/css' media='all' href="<?=$css_path?>style.css" />
    <link rel='stylesheet' type='text/css' media='all' href="<?=$css_path?>extra.css" />
    <link rel='stylesheet' type='text/css' media='all' href="<?=$css_path?>masonry.css" />
    <link rel='stylesheet' type='text/css' media='all' href="<?=$css_path?>prettyCheckboxes.css" />
    <link rel='stylesheet' type='text/css' media='all' href="<?=$css_path?>chosen.css" />
    
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="<?=$js_path?>jquery-migrate-1.1.1.min.js"></script>
    <script type="text/javascript" src="<?=$js_path?>jquery-easing.1.2.js"></script>
    <script type="text/javascript" src="<?=$js_path?>jquery-easing-compatibility.1.2.js"></script>
    <!-- placeholder for textboxes -->
    <script type="text/javascript" src="<?=$js_path?>textbox-hint.js"></script>
    <!-- radio and checkbox beautifier -->
    <script type="text/javascript" src="<?=$js_path?>prettyCheckboxes.js"></script>
    <!-- multi selection/chooser -->
    <script type="text/javascript" src="<?=$js_path?>chosen.jquery.min.js"></script>
    <!-- infinite scroll -->
    <!--[if lt IE 9]><script type="text/javascript" src="<?=$js_path?>html5.js"></script><![endif]-->
    <script type="text/javascript" src="<?=$js_path?>jquery.masonry.min.js"></script>
    <script type="text/javascript" src="<?=$js_path?>jquery.infinitescroll.min.js"></script>
    <script type="text/javascript" src="<?=$js_path?>modernizr-transitions.js"></script>
    
    <script type="text/javascript" src="<?=$js_path?>common.js"></script>
    
    <? /** ?>
    <meta property="og:site_name" content="gifteng"/>
    <meta property="og:url" content="http://gifteng.com/"/>
    <meta property="og:title" content="Interesting. Man. Things. "/>
    <meta property="og:description" content="A social image bookmarking community specifically catered to the male population. "/>
    <meta property="og:image" content="http://manteresting.com/uploads/data/manheader.png"/>
    <meta property="og:type" content="manterestingtwo:&lt;meta property=&quot;og:type&quot; content=&quot;manterestingtwo:nail&quot; /&gt;"/>
    <? /**/ ?>
</head>
<body>
    <div id="header">
        <div id="top"><h1 class="gifteng" title="Gifteng">Gifteng</h1></div>
        <div id="navigator">
            <div class="left"><a id="about"><?=lang('main_menu_about')?></a></div>
            <? /** ?>
            <div class="left"><a href="<?=base_url()?>browse"><span class="categories"></span></a></div>
            <? /**/ ?>
            <div class="left"><span class="categories"></span></div>
            <div class="left"><span class="search"></span></div>
            <? /** ?>
            <div class="right"><a href="<?=base_url()?>invitation">Invitation</a></div>
            <div class="right"><a href="<?=base_url()?>authentication">Sign in</a></div>
            <? /**/ ?>
            <div class="right"><span class="location"></span></div>
            <div class="right"><span class="give"></span></div>
            <div class="logo"><img src="<?=$img_path?>logo.png" /></div>
        </div>
        
        <div id="navigator2" style="display: none;"> 
            <ul id="nav2">
                <li><a href="<?=base_url()?>index"><?=lang('main_submenu_home')?></a></li>
                <li><a href="#"><?=lang('main_submenu_help')?></a></li>
                <li><a href="#"><?=lang('main_submenu_jobs')?></a></li>
                <li><a href="#"><?=lang('main_submenu_business')?></a></li>
                <li><a href="#"><?=lang('main_submenu_privacy')?></a></li>
                <li><a href="#"><?=lang('main_submenu_policy')?></a></li>
                <li><a href="#"><?=lang('main_submenu_contact')?></a></li>
            </ul>	
        </div>
        
        <div class="tagline"><?=lang('main_slogan')?></div>
    </div>
    
    <div id="stage">
