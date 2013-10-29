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
			var keywords = replacements.keywords;
			var category = replacements.category;
			var filtered;
			
	        if(typeof(keywords)!=='undefined') {
	        	var ads=[];
	        	angular.forEach(input, function(value, key) {
	        		if(value.creatorname.indexOf(keywords) !== -1 || value.title.indexOf(keywords) !== -1) {
	        			ads.push(value);
	        		}
	        	});
	        	filtered=ads;
	        }
	        else {
				filtered=input;
	        }
	        
	        if(typeof(category)!=='undefined' && category!=='') {
	        	var ads=[];
	        	angular.forEach(filtered, function(value,key) {
	        		if(value.categoryId==category) {
	        			ads.push(value);
	        		}
	        	});
	        	filtered=ads;
	        }
	        
	        return filtered;
		}
	});
});
