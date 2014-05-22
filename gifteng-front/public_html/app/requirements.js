require.config({
	paths : {
		"angular" : "lib/angular",
		"jquery" : "lib/jquery",
		"angular-route": "lib/angular-route",
		"angular-resource" : "lib/angular-resource",
		"angular-cookies" : "lib/angular-cookies",
		"angular-touch" : "lib/angular-touch",
		"angular-sanitize" : "lib/angular-sanitize",
		"bootstrap" : "lib/bootstrap",
		"jquery.form" : "lib/jquery.form",
		"leaflet" : "lib/leaflet",
		"leaflet-providers" : "lib/leaflet-providers",
		"leaflet-plugins" : "lib/leaflet-plugins",
		"ui-bootstrap-modal" : "lib/ui-bootstrap-modal",
		"ui-bootstrap-tooltip" : "lib/ui-bootstrap-tooltip",
        "jcrop": "lib/jquery.Jcrop.min",
        "wistia": "http://fast.wistia.com/assets/external/E-v1",
        "tooltip": "lib/tooltip",
        "add2home": "lib/add2home",
        "geoip": "http://js.maxmind.com/js/apis/geoip2/v2.0/geoip2"
	},
	shim : {
		angular : {
			exports : "angular"
		},
		leaflet : {
			exports : "leaflet"
		}
	},
	baseUrl : 'app/',
	urlArgs: "bust=v4"
});

require(["angular", "angular-route", "angular-resource", "angular-cookies", "angular-touch", "angular-sanitize", "jquery", "bootstrap", "leaflet", "leaflet-providers", "leaflet-plugins", "app", "routes", "ui-bootstrap-modal", "ui-bootstrap-tooltip", "add2home", "geoip"], function(angular) {
	angular.bootstrap(document, ["gifteng"]);
}); 