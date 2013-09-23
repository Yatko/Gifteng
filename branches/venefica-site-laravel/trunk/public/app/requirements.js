require.config({
	paths : {
		"angular" : "lib/angular",
		"jquery" : "lib/jquery",
		"angular-resource" : "lib/angular-resource",
		"bootstrap" : "lib/bootstrap",
		"snap": "lib/snap"
	},
	shim : {
		angular  :{
			exports : "angular"
		}
	},
    baseUrl: 'app/'
});


require(["angular","jquery","snap","bootstrap","angular-resource","app","routes"],
	function(angular) {
		angular.bootstrap(document,["gifteng"]);
	}
);