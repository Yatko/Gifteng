<?php

define('BASE_PATH', base_url().'assets/'.TEMPLATES.'/');
define('JS_PATH', BASE_PATH.'js/');
define('CSS_PATH', BASE_PATH.'css/');
define('IMG_PATH', BASE_PATH.'img/');

$page = $this->uri->segment(1, null);

?>

<!DOCTYPE html>
<html>
<head>
    <title><?=lang('main_title')?></title>
    
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>
    <meta name="description" content="Gifteng - make the world a giving place" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    
    
    <link rel='stylesheet' type='text/css' media='all' href="<?=CSS_PATH?>bootstrap.css" />
    <link rel='stylesheet' type='text/css' media='all' href="<?=CSS_PATH?>bootstrap-responsive.css" />
    <link rel='stylesheet' type='text/css' media='all' href="<?=BASE_PATH?>temp-gifteng.css" />
    <link rel='stylesheet' type='text/css' media='all' href="<?=BASE_PATH?>temp-gifteng-addon.css" />
    
    <link rel='stylesheet' type='text/css' href='http://fonts.googleapis.com/css?family=Lato:100,300,400,700,900,100italic,300italic,400italic,700italic,900italic'>
    
    <style>
        body {
            padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */
        }
    </style>
    
    
    <link rel='stylesheet' type='text/css' media='all' href="<?=CSS_PATH?>masonry.css" />
    
    
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <!-- bootstrap -->
    <script type="text/javascript" src="<?=JS_PATH?>bootstrap.js"></script>
    <!-- infinite scroll -->
    <!--[if lt IE 9]><script type="text/javascript" src="<?=JS_PATH?>html5.js"></script><![endif]-->
    <script type="text/javascript" src="<?=JS_PATH?>jquery.masonry.min.js"></script>
    <script type="text/javascript" src="<?=JS_PATH?>jquery.infinitescroll.min.js"></script>
    <script type="text/javascript" src="<?=JS_PATH?>modernizr-transitions.js"></script>
    
    <script type="text/javascript" src="<?=JS_PATH?>common.js"></script>
    
    
    <meta property="og:site_name" content="Gifteng ♥"/>
    <meta property="og:url" content="http://gifteng.com/"/>
    <meta property="og:title" content="Gifteng ♥"/>
    <meta property="og:description" content="- make the world a giving place -"/>
    <meta property="og:image" content="<?=IMG_PATH?>gifteng.png"/>
    <meta property="og:type" content="website"/>
    
    <script>
        (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
        (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
        m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
        })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
        
        ga('create', 'UA-40348949-1', 'gifteng.com');
        ga('send', 'pageview');
    </script>
</head>

<body>
    
    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="<?=base_url()?>index">Gifteng</a>
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li<?=($page == "profile" ? ' class="active"' : '')?>><a href="<?=base_url()?>profile"><?=lang('main_submenu_profile')?></a></li>
              <li<?=($page == "browse" ? ' class="active"' : '')?>><a href="<?=base_url()?>browse">BROWSE</a></li>
              <li<?=($page == "post" ? ' class="active"' : '')?>><a href="<?=base_url()?>post">POST</a></li>
              <li<?=($page == "invitation" ? ' class="active"' : '')?>><a href="<?=base_url()?>invitation">INVITATION</a></li>
              <li<?=($page == "registration" ? ' class="active"' : '')?>><a href="<?=base_url()?>registration">REGISTRATION</a></li>
              <? if( isLogged() ): ?>
                <li<?=($page == "authentication" ? ' class="active"' : '')?>><a href="<?=base_url()?>authentication/logout">SIGN OUT</a></li>
              <? else: ?>
                <li<?=($page == "authentication" ? ' class="active"' : '')?>><a href="<?=base_url()?>authentication/login">SIGN IN</a></li>
              <? endif; ?>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>
    
<!-- ********** ********** ********** -->    
<!-- content starts here -->
<!-- ********** ********** ********** -->  

<!-- ge CONTENT -->
<div class="container geContainer">
    <div class="row">