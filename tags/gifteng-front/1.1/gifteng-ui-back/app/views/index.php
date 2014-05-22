<!DOCTYPE html>
<html lang="en">
	<head profile="http://dublincore.org">
		<meta charset="utf-8">
		<title>Gifteng | Give. Receive. Inspire.</title>
		<meta name="viewport" content="width=device-width, height=device-height, initial-scale=1.0, maximum-scale=1.0, target-densityDpi=device-dpi, minimal-ui">
		<meta name="description" content="A unique social community where you can give and receive things you love, for free."> 
		<meta name="keywords" content="free stuff, freebies, gifts, free stuff online"> 
		<meta name="author" content="www.gifteng.com"> 
		<meta name="copyright" content="(c) 2014, gifteng.com"> 
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">		
		<meta name="google-site-verification" content="zW7qDZZCQYPFLciEL7ySIjCfccgznWjY5MHr1vkKiN8">
		<meta name="p:domain_verify" content="592c562546fda58a9ba3de536deafeca">
		<meta property="fb:admins" content="100006621787064">
		<meta property="fb:page_id" content="240475762731626">		
		<meta property="og:site_name" content="Gifteng"/>
		<meta property="og:url" content="http://www.gifteng.com/"/>
		<meta property="og:title" content="Give. Receive. Inspire."/>
		<meta property="og:description" content="A unique social community where you can give and receive things you love, for free."/>
		<meta property="og:image" content="http://www.gifteng.com/assets/images/apple-icon-144x144.png"/>
		<meta property="og:type" content="website"/>
		<!-- For iOS web apps -->
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta name="apple-mobile-web-app-title" content="Gifteng">
		<link rel="apple-touch-icon" sizes="57x57" href="assets/images/apple-icon-57x57.png" />
		<link rel="apple-touch-icon" sizes="72x72" href="assets/images/apple-icon-72x72.png" />
		<link rel="apple-touch-icon" sizes="114x114" href="assets/images/apple-icon-114x114.png" />
		<link rel="apple-touch-icon" sizes="144x144" href="assets/images/apple-icon-144x144.png" />
		<link rel="shortcut icon" href="assets/images/favicon.ico">   	
		
		<link href="assets/less/app.css" type="text/css" rel="stylesheet" />
		<link href="assets/css/gifteng.css" type="text/css" rel="stylesheet" />
		<link href="assets/css/gifteng-dev.css" type="text/css" rel="stylesheet" />
		<link href="assets/css/jquery.Jcrop.min.css" type="text/css" rel="stylesheet" />
		<link href='http://fonts.googleapis.com/css?family=Lato:300,400,700' rel='stylesheet' type='text/css'>

		<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.5.1/leaflet.css" />
		<!--[if lte IE 8]>
		<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.5.1/leaflet.ie.css" />
		<![endif]-->

		<!-- dublincore.org -->
		<link rel="schema.DC" href="http://purl.org/dc/elements/1.1/" /> 
		<link rel="schema.DCTERMS" href="http://purl.org/dc/terms/" /> 
		<meta name="DC.title" xml:lang="EN" content="Gifteng" />
		<meta name="DC.title" lang="en" content="Gifteng" /> 
		<meta name="DC.creator" content="www.gifteng.com" /> 
		<meta name="DC.subject" lang="en" content="Give. Receive. Inspire." /> 
		<meta name="DC.description" lang="en" content="A unique social community where you can give and receive things you love, for free." /> 
		<meta name="DC.publisher" content="Gifteng" /> 
		<meta name="DC.type" scheme="DCTERMS.DCMIType" content="Dataset" /> 
		<meta name="DC.format" content="text/html" />  
		<meta name="DC.identifier" scheme="DCTERMS.URI" content="http://www.gifteng.com" /> 
		<meta name="DC.language" scheme="DCTERMS.URI" content="en" /> 
		<meta name="DC.relation" scheme="DCTERMS.URI" content="free stuff online" /> 
		<meta name="DC.coverage" scheme="DCTERMS.URI" content="New York, USA, Worldwide" />
	
		<script data-main="app/requirements.js" src="app/lib/require.js"></script>
		<script language="javascript">
				(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
				(i[r].q=i[r].q||[]).push(arguments);},i[r].l=1*new Date();a=s.createElement(o),
				m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m);
				})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
				ga('create', 'UA-40348949-1', 'gifteng.com');
				ga('send', 'pageview');
		</script>	
	</head>
	<body>
		<fb app-id='285994388213208'></fb>
		<div ng-include="'app/partials/navbar.html'" ng-controller="NavController"></div>
		<div class="snap-content snap-slide">
			<div class="ng-view"></div>
		</div>
		<div id="loader" class="loader-back">
			<div>
				<loader></loader>
			</div>
		</div>
	</body>
</html>
