define(['angular','services'], function (angular, services) {
	'use strict';
	
	angular.module('gifteng.filters', ['gifteng.services'])
	.filter('replace', function() {
		return function(input, replacements) {
		    if (!angular.isObject(replacements)) {
		      return input;
		    }
		    
		    angular.forEach(replacements, function (to, from) {
		        var regex = new RegExp(from, "g");
		        if(typeof(input)!=='undefined')
		        input = input.replace(from, to);
		    });
		
		    return input;
		};
	});
});
