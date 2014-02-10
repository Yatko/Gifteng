<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<title>Gifteng | Give Receive Inspire</title>
		<!-- For iOS web apps -->
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta name="apple-mobile-web-app-title" content="Gifteng">
		
		<link rel="apple-touch-icon" sizes="57x57" href="assets/images/apple-icon-57x57.png" />
		<link rel="apple-touch-icon" sizes="72x72" href="assets/images/apple-icon-72x72.png" />
		<link rel="apple-touch-icon" sizes="114x114" href="assets/images/apple-icon-114x114.png" />
		<link rel="apple-touch-icon" sizes="144x144" href="assets/images/apple-icon-144x144.png" />
		    	
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, minimal-ui">

		<link href="assets/less/app.css" type="text/css" rel="stylesheet" />
		<link href="assets/css/gifteng.css" type="text/css" rel="stylesheet" />
		<link href="assets/css/gifteng-dev.css" type="text/css" rel="stylesheet" />
		<link href="assets/css/jquery.Jcrop.min.css" type="text/css" rel="stylesheet" />
		<link href='http://fonts.googleapis.com/css?family=Lato:300,400,700' rel='stylesheet' type='text/css'>

		<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.5.1/leaflet.css" />
		<!--[if lte IE 8]>
		<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.5.1/leaflet.ie.css" />
		<![endif]-->

		<link rel="shortcut icon" href="assets/images/favicon.ico">
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