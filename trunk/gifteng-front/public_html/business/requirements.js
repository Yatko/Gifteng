require.config({
	paths : {
		"angular" : "../app/lib/angular",
		"jquery" : "../app/lib/jquery",
		"angular-route": "../app/lib/angular-route",
		"angular-resource" : "../app/lib/angular-resource",
		"angular-touch" : "../app/lib/angular-touch",
		"angular-sanitize" : "../app/lib/angular-sanitize",
		"bootstrap" : "../app/lib/bootstrap",
		"jquery.form" : "../app/lib/jquery.form",
		"leaflet" : "../app/lib/leaflet",
		"leaflet-providers" : "../app/lib/leaflet-providers",
		"leaflet-plugins" : "../app/lib/leaflet-plugins",
		"ui-bootstrap-modal" : "../app/lib/ui-bootstrap-modal",
		"bootstrap-datepicker" : "../app/lib/bootstrap-datepicker",
        "jcrop": "../app/lib/jquery.Jcrop.min"
	},
	shim : {
		angular : {
			exports : "angular"
		},
		leaflet : {
			exports : "leaflet"
		}
	},
	baseUrl : 'business/'
});

require(["angular", "angular-route", "angular-touch", "angular-resource", "angular-sanitize", "jquery", "jquery.form", "bootstrap", "leaflet", "leaflet-providers", "leaflet-plugins", "app", "routes", "ui-bootstrap-modal", "bootstrap-datepicker"], function(angular) {
	angular.bootstrap(document, ["giftengbiz"]);
}); 