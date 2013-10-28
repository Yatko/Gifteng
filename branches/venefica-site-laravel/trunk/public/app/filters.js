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
	})
	.filter('ads', function() {
		return function(input, replacements) {
			
	        if(typeof(replacements)!=='undefined') {
	        	var ads=[];
	        	angular.forEach(input, function(value, key) {
					console.log(value);
	        		if(value.creatorname.indexOf(replacements) !== -1 || value.title.indexOf(replacements) !== -1) {
	        			ads.push(value);
	        		}
	        	});
	        	return ads;
	        }
	        else {
				return input;
	        }
		}
	});
});
