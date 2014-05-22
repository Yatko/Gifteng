define(["angular", "services"], function(angular, services) {
	angular.module("giftengbiz.filters", ["giftengbiz.services"]).filter("replace", function() {
		return function(input, replacements) {
			return angular.isObject(replacements) ? (angular.forEach(replacements, function(to, from) {
				var regex = new RegExp(from, "g");
				typeof input != "undefined" && ( input = input.replace(from, to))
			}), input) : input
		}
	}).filter("ads", function() {
		return function(input, replacements) {
			var keywords = replacements.keywords, category = replacements.category, filtered;
			if ( typeof keywords != "undefined") {
				var ads = [];
				angular.forEach(input, function(value, key) {
					(value.creatorname.indexOf(keywords) !== -1 || value.title.indexOf(keywords) !== -1) && ads.push(value)
				}), filtered = ads
			} else
				filtered = input;
			if ( typeof category != "undefined" && category !== "") {
				var ads = [];
				angular.forEach(filtered, function(value, key) {
					value.categoryId == category && ads.push(value)
				}), filtered = ads
			}
			return filtered
		}
	}).filter('htmlLinky', function($sanitize, linkyFilter) {
	  var ELEMENT_NODE = 1;
	  var TEXT_NODE = 3;
	  var linkifiedDOM = document.createElement('div');
	  var inputDOM = document.createElement('div');
	  
	  var linkify = function linkify(startNode) {
	    var i, ii, currentNode;
	    
	    for (i = 0, ii = startNode.childNodes.length; i < ii; i++) {
	      currentNode = startNode.childNodes[i];
	      
	      switch (currentNode.nodeType) {
	        case ELEMENT_NODE:
	          linkify(currentNode);
	          break;
	        case TEXT_NODE:
	          linkifiedDOM.innerHTML = linkyFilter(currentNode.textContent);
	          i += linkifiedDOM.childNodes.length - 1
	          while(linkifiedDOM.childNodes.length) {
	            startNode.insertBefore(linkifiedDOM.childNodes[0], currentNode);
	          }
	          startNode.removeChild(currentNode);
	      }
	    }
	    
	    return startNode;
	  };
	  
	  return function(input) {
	    inputDOM.innerHTML = input;
	    return linkify(inputDOM).innerHTML;
	  };
	});
}); 