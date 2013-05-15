'use strict';

/* Controllers */


function MyCtrl1() {}
MyCtrl1.$inject = [];


function MyCtrl2() {
}
MyCtrl2.$inject = [];

function TrickCategoryController($scope, $resource, Trick) {
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

function TrickController($scope, $resource, $routeParams) {
	var Trick = $resource('/api/tricks/:_id');
	$scope.allTricks = Trick.query({category: $routeParams.category});
}

TrickController.$inject = ['$scope', '$resource', '$routeParams'];

function MeshController($scope, $resource) {
        var Mesh = $resource('/api/meshes/:_id');
        $scope.mesh = new Mesh({});
	//$http.defaults.headers.common['Authorization'] = 'Basic ' + 'Y2hyaXNAY2hyaXMuY29tOjEyMzQ=';

  $scope.update = function(user) {
    $scope.mesh.$save();
  };

  $scope.reset = function() {
    //$scope.loc = {};
  };

  $scope.isUnchanged = function(mseh) {
    return angular.equals($scope.mesh, {});
  };

  $scope.reset();
}
MeshController.$inject = ['$scope', '$resource'];
