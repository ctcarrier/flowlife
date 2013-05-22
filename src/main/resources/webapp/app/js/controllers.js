'use strict';

/* Controllers */

function TrickCategoryController($scope, $resource) {
    var Trick = $resource('/api/tricks/:_id');
    var TrickCategory = $resource('/api/trickCategories/:_id');
	$scope.allTricks = Trick.query({});
	$scope.allTrickCategories = TrickCategory.query({});
	$scope.trick = new Trick({});
 
  $scope.update = function(user) {
    $scope.trick.$save();
  };
 
  $scope.reset = function() {
    //$scope.loc = {};
  };
 
  $scope.isUnchanged = function(user) {
    return angular.equals(user, {});
  };
 
  $scope.reset();
}
TrickCategoryController.$inject = ['$scope', '$resource'];

function TrickController($scope, $resource, $routeParams, Trick) {
	$scope.allTricks = Trick.query({category: $routeParams.category});
}

TrickController.$inject = ['$scope', '$resource', '$routeParams', Trick];

function TrickDetailsController($scope, $resource, $routeParams, Trick) {
	$scope.trick = Trick.get({_id: $routeParams.trickId});
}

TrickDetailsController.$inject = ['$scope', '$resource', '$routeParams', Trick];

function TrickAdminController($scope, $resource, $routeParams, Trick) {
    $http.defaults.headers.common['Authorization'] = 'Basic ' + 'Y2hyaXNAY2hyaXMuY29tOjEyMzQ=';
	$scope.trick = Trick.get({_id: $routeParams.trickId});
}

TrickAdminController.$inject = ['$scope', '$resource', '$routeParams', Trick];
