<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Gifteng | Give. Receive. Inspire.</title>
		
		<link href="assets/less/app.css" type="text/css" rel="stylesheet" />
		<link href="assets/css/gifteng.css" type="text/css" rel="stylesheet" />
		<link href="assets/css/gifteng-dev.css" type="text/css" rel="stylesheet" />
		<link href="assets/css/jquery.Jcrop.min.css" type="text/css" rel="stylesheet" />
		<link href='http://fonts.googleapis.com/css?family=Lato:300,400,700' rel='stylesheet' type='text/css'>

		<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.5.1/leaflet.css" />
		<!--[if lte IE 8]>
		<link rel="stylesheet" href="http://cdn.leafletjs.com/leaflet-0.5.1/leaflet.ie.css" />
		<![endif]-->
		
		<script data-main="business/requirements.js" src="app/lib/require.js"></script>
		<script> /* Provisory for dev environment: */ localStorage.clear(); </script>
	</head>
	<body>
		<div ng-include="'business/partials/navbar.html'" ng-controller="NavController"></div>
		
		<div class="ng-view"></div>
			
		<div id="loader" class="loader-back">
			<div>
				<loader></loader>
			</div>
		</div>
	</body>
</html>