require.config({
	paths : {
		"angular" : "lib/angular",
		"jquery" : "lib/jquery",
		"angular-route": "lib/angular-route",
		"angular-resource" : "lib/angular-resource",
		"angular-cookies" : "lib/angular-cookies",
		"angular-touch" : "lib/angular-touch",
		"bootstrap" : "lib/bootstrap",
		"jquery.form" : "lib/jquery.form",
		"leaflet" : "lib/leaflet",
		"leaflet-providers" : "lib/leaflet-providers",
		"leaflet-plugins" : "lib/leaflet-plugins",
		"ui-bootstrap-modal" : "lib/ui-bootstrap-modal",
        "jcrop": "lib/jquery.Jcrop.min",
        "wistia": "http://fast.wistia.com/assets/external/E-v1",
        "tooltip": "lib/tooltip"
	},
	shim : {
		angular : {
			exports : "angular"
		},
		leaflet : {
			exports : "leaflet"
		}
	},
	baseUrl : 'app/'
});

require(["angular", "angular-route", "angular-resource", "angular-cookies", "angular-touch", "jquery", "jquery.form", "bootstrap", "leaflet", "leaflet-providers", "leaflet-plugins", "app", "routes", "ui-bootstrap-modal", "tooltip"], function(angular) {
	angular.bootstrap(document, ["gifteng"]);
}); 