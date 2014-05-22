define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('SearchController', ["$scope", "Filters", "Categories", function($scope, Filters, Categories) {
		var search = Filters.getSearch();

		$scope.$watch(function() {
			return Categories.get();
		}, function(cat) {
			$scope.categories=cat;
		}, true);

		$scope.search={};
		angular.copy(search,$scope.search);
		$scope.submit = function() {
			Filters.setSearch($scope.search);
			$scope.close();
		}
	}])

});