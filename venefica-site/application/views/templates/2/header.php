<?php

$base_path  = base_url().'assets/'.TEMPLATES.'/';
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
    
    <link href='http://fonts.googleapis.com/css?family=PT+Sans:400,700,400italic,700italic' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=PT+Sans+Narrow:400,700' rel='stylesheet' type='text/css'>
    
    <link rel='stylesheet' type='text/css' media='all' href="<?=$css_path?>style.css" />
    <link rel='stylesheet' type='text/css' media='all' href="<?=$css_path?>masonry.css" />
    
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
    
    <!-- infinite scroll -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!--[if lt IE 9]><script src="<?=$js_path?>html5.js"></script><![endif]-->
    <script src="<?=$js_path?>jquery.masonry.min.js"></script>
    <script src="<?=$js_path?>jquery.infinitescroll.min.js"></script>
    <script src="<?=$js_path?>modernizr-transitions.js"></script>
    
    
    <script src="<?=$js_path?>common.js"></script>
    
    
    <!--
    <meta property="og:site_name" content="MANteresting"/>
    <meta property="og:url" content="http://manteresting.com/"/>
    <meta property="og:title" content="Interesting. Man. Things. "/>
    <meta property="og:description" content="A social image bookmarking community specifically catered to the male population. "/>
    <meta property="og:image" content="http://manteresting.com/uploads/data/manheader.png"/>
    <meta property="og:type" content="manterestingtwo:&lt;meta property=&quot;og:type&quot; content=&quot;manterestingtwo:nail&quot; /&gt;"/>
    -->
</head>
<body>
    <div id="header">
        <div id="top"><h1 class="gifteng" title="Gifteng">Gifteng</h1></div>
        <div id="navigator">
            <div class="left">About</div>
            <div class="left"><a href="<?=base_url()?>browse"><strong>&Xi;</strong></a></div>
            <div class="left"><strong>&hearts;</strong></div>
            <div class="right"><a href="<?=base_url()?>authentication">Sign in</a></div>
            <div class="right"><strong>&clubs;</strong></div>
        </div>
        <div class="tagline">- make the world a giving place -</div>
    </div>
    
    <div id="stage">
