require.config({
	paths : {
		"angular" : "lib/angular",
		"jquery" : "lib/jquery",
		"angular-resource" : "lib/angular-resource",
		"bootstrap" : "lib/bootstrap",
		"snap" : "lib/snap",
		"jquery.form" : "lib/jquery.form",
		"leaflet" : "lib/leaflet",
		"leaflet-providers" : "lib/leaflet-providers",
		"leaflet-plugins" : "lib/leaflet-plugins",
		"ui-bootstrap-modal" : "lib/ui-bootstrap-modal"
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

require(["angular", "angular-resource", "jquery", "jquery.form", "snap", "bootstrap", "leaflet", "leaflet-providers", "leaflet-plugins", "app", "routes", "ui-bootstrap-modal"], function(angular) {
	angular.bootstrap(document, ["gifteng"]);
}); 