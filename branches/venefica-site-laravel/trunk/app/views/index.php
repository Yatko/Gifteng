<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<title>Gifteng | Give. Receive. Inspire.</title>
		
		<link rel="stylesheet/less" type="text/css" href="assets/less/app.less" />
		
		<script src="assets/js/less.min.js" type="text/javascript"></script>
		
		<script data-main="app/requirements.js" src="app/lib/require.js"></script>
		<script type="text/javascript">var less=less||{};less.env='development';</script>
	</head>
	<body>
		<div ng-include="'app/partials/navbar.html'"></div>
		<div class="snap-content snap-slide">
			<div class="container ge-container">
				<div class="ng-view"></div>
			</div>
		</div>
	</body>
</html>