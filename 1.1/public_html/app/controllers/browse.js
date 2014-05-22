define(['controllers'], function(controllers) {
	'use strict';
	
	controllers.controller('BrowseController', ["$scope", "$route", "$modal", "$window", "AdDialog", "AdMore", "Ad", "Filters", "$routeParams", "Categories", "$location", function($scope, $route, $modal, $window, AdDialog, AdMore, Ad, Filters, $routeParams, Categories, $location) {
			
		Filters.setSearch({order:'newest',keywords:"",category:"",delivery:""});
		
		var search = Filters.getSearch();

		$scope.$watch(function() {
			return Categories.get();
		}, function(cat) {
			$scope.categories=cat;
		}, true);

		var type=null;
		$scope.filter={members:false,sponsored:false};
		$scope.search={};
		$scope.numCols = Filters.getColumns();

		var search=Filters.getSearch();
		angular.copy(search,$scope.search);
		
		$scope.$watch(function() {
			return $scope.filter;
		}, function(filter) {
			if(filter.members && !filter.sponsored) {
				type="MEMBER";
			} else if(!filter.members && filter.sponsored) {
				type="BUSINESS";
			} else {
				type=null;
			}
			$scope.doSearch();
		}, true);

		
		$scope.doPost = function() {
    		AdDialog.close();
    		$location.search('view',null);
			var modalInstance = $modal.open({
				templateUrl: 'app/partials/post.html',
				controller: function($scope, UserEx, $modalInstance) {
					$scope.isModal=true;
					$scope.close = function () {
						$modalInstance.close();
					};
				}
			});
		}

		$scope.submitSearch = function() {
			var src={};
			angular.copy($scope.search, src);
			if(src.pickup && !src.shipping) {
				src.delivery="pickup"
			} else if(!src.pickup && src.shipping) {
				src.delivery="shipping";
			}
			$scope.order=src.order;
			$scope.keywords=src.keywords;
			$scope.category=src.category;
			$scope.delivery=src.delivery;
			$scope.doSearch();
		}

		$scope.setColumns = function(num) {
			Filters.setColumns(num);
		}
		$scope.toggleWells = function() {
			Filters.toggleSimple();
		}

		var toCols = function() {
			$scope.cols=[];
			for(var i=0;i<$scope.numberColumns;i++) {
				$scope.cols[i]=[];
			}
			
			if(typeof(ads)!=='undefined') {
				var c=0;
				for(var i=0;i<ads.length;i++) {
					$scope.cols[c].push(ads[i]);
					c++;
					if(c==$scope.numberColumns) {
						c=0;
					}
				}
			}
		}
		
		$scope.$watch(
			function() {
				return Filters.getColumns();
			},
			function(columns) {
				$scope.numCols=columns;
				$scope.distributeColumns(columns);
			},
			true
		);
		
		$scope.$watch(
			function() {
				return $scope.colsClass;
			},
			function(colsclass) {
				toCols();
			},
			true
		);
		
		$scope.columnCheck = function() {
			if(window.innerWidth<480) {
				return 1;
			} else if(window.innerWidth<768) {
				return 2;
			} else if(window.innerWidth<992) {
				return 3;
			} else {
				return 4;
			}
		}
		
		$scope.distributeColumns = function(columns) {
			var size = $scope.columnCheck();
			if(size==1) {
				$scope.numberColumns=1;
			} else if(size==2 && columns>2) {
				$scope.numberColumns=2;
			} else if(size==3 && (columns==4 || columns==1)) {
				$scope.numberColumns=3;
			} else if(size==4 && columns<3) {
				$scope.numberColumns=4;
			} else {
				$scope.numberColumns=columns;
			}
			if($scope.colsClass!="col-xs-"+(12/$scope.numberColumns)) {
				$scope.colsClass = "col-xs-"+(12/$scope.numberColumns);
			}
		}

		angular.element($window).bind('resize',function(){
        	$scope.$apply(function() {
				$scope.distributeColumns(Filters.getColumns());
            });
		});
		
		
		$scope.openSearch = function() {
			var modalInstance = $modal.open({
				templateUrl: 'app/partials/directives/modal/search.html',
				controller: function($scope, $modalInstance) {
					$scope.close = function(){
						$modalInstance.close();
					};
				}
			});
		};
		
		$scope.$watch(
			function() {
				return Filters.getSimple();
			},
			function(simple) {
				$scope.wellSimple = simple;
			},
			true
		);
		
		$scope.$watch(
			function() {
				return Filters.getSearch();
			},
			function(search) {
				$scope.order=search.order;
				$scope.keywords=search.keywords;
				$scope.category=search.category;
				$scope.delivery=search.delivery;
				$scope.doSearch();
			},
			true
		);
		
		if(typeof($routeParams.id)!=='undefined'){
			$scope.adsearch = $routeParams.id;
		}
		if(typeof($routeParams.view)!=='undefined') {
			AdDialog.open($routeParams.view, $scope);
		}
		$scope.$on('$locationChangeSuccess', function(event) {
			if(typeof($routeParams.view)!=='undefined') {
				AdDialog.open($routeParams.view, $scope);
			}
		});
		$scope.loaded=0;
		var more=0;
		var filterAds=[];
		var ads=[];
		
		$scope.doSearch = function() {
			more=0;
			filterAds=[];
			$scope.loadAds();
			$scope.creators=[];
			ads=[];
		}

		$scope.loadAds = function () {
			var loaded_ads = Ad.query({order:$scope.order,keywords:$scope.keywords,category:$scope.category,delivery:$scope.delivery,types:type}, function() {
				$scope.searchTerm = $scope.keywords;
				if(typeof(loaded_ads['ads'])!=='undefined') {
					$scope.hasAds = loaded_ads['ads'].length;
				}
				else {
					$scope.hasAds = -1;
				}
				$scope.loaded=1;
				$scope.creators = $.extend({},$scope.creators,loaded_ads['creators']);
				
				if(typeof(loaded_ads['ads'])!=='undefined') {
					for(var i=0;i<loaded_ads['ads'].length;i++) {
						if(filterAds.indexOf(loaded_ads['ads'][i]['id']) == -1) {
							filterAds.push(loaded_ads['ads'][i]['id']);
							ads.push(loaded_ads['ads'][i]);
						}
						if(i==loaded_ads['ads'].length-1) {
							$scope.last=loaded_ads['ads'][i].lastIndex;
						}
					}
				}
				toCols();
				$scope.filterAds = filterAds;
			});
		}
		
		var loadMore = function() {
			for(var i=0;i<more;i++) {
				var loaded_ads = AdMore.query({'last':$scope.last,order:$scope.order,keywords:$scope.keywords,category:$scope.category,delivery:$scope.delivery,types:type}, function() {
					$scope.creators = $.extend({},$scope.creators,loaded_ads['creators']);
					if(typeof(loaded_ads['ads'])!=='undefined') {
						for(var i=0;i<loaded_ads['ads'].length;i++) {
							if(filterAds.indexOf(loaded_ads['ads'][i]['id']) == -1) {
								filterAds.push(loaded_ads['ads'][i]['id']);
								ads.push(loaded_ads['ads'][i]);
							}
							if(i==loaded_ads['ads'].length-1) {
								$scope.last=loaded_ads['ads'][i].lastIndex;
							}
						}
					}
					toCols();
					$scope.filterAds = filterAds;
				});
			}
		}
		
		$scope.loadMore = function() {
			more++;
			loadMore();
		}
		
		$scope.callback = function() {
			$scope.loaded=0;
			filterAds=[];
			$scope.loadAds();
			ads=[];
		};
	}])

});