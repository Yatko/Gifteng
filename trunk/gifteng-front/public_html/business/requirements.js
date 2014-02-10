require.config({
	paths : {
		"angular" : "../app/lib/angular",
		"jquery" : "../app/lib/jquery",
		"angular-resource" : "../app/lib/angular-resource",
		"bootstrap" : "../app/lib/bootstrap",
		"jquery.form" : "../app/lib/jquery.form",
		"leaflet" : "../app/lib/leaflet",
		"leaflet-providers" : "../app/lib/leaflet-providers",
		"leaflet-plugins" : "../app/lib/leaflet-plugins",
		"ui-bootstrap-modal" : "../app/lib/ui-bootstrap-modal",
		"bootstrap-datepicker" : "../app/lib/bootstrap-datepicker"
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

require(["angular", "angular-resource", "jquery", "jquery.form", "bootstrap", "leaflet", "leaflet-providers", "leaflet-plugins", "app", "routes", "ui-bootstrap-modal", "bootstrap-datepicker"], function(angular) {
	angular.bootstrap(document, ["giftengbiz"]);
}); 