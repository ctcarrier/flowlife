'use strict';

/* Controllers */

function TrickCategoryController($scope, $resource) {
    var Trick = $resource('/api/tricks/:_id');
    var TrickCategory = $resource('/api/trickCategories/:_id');
	$scope.allTricks = Trick.query({});
	$scope.allTrickCategories = TrickCategory.query({});
	$scope.trick = new Trick({});
}
TrickCategoryController.$inject = ['$scope', '$resource'];

function TrickController($scope, $resource, $routeParams) {
    var Trick = $resource('/api/tricks/:_id');
	$scope.allTricks = Trick.query({category: $routeParams.category});
}

TrickController.$inject = ['$scope', '$resource', '$routeParams'];

function TrickDetailsController($scope, $resource, $routeParams) {
    var Trick = $resource('/api/tricks/:_id');
	$scope.trick = Trick.get({_id: $routeParams.trickId});
}

TrickDetailsController.$inject = ['$scope', '$resource', '$routeParams'];

function AdminRootController($scope, $resource, $routeParams, $cookieStore, $http, User) {
    $http.defaults.headers.common['Authorization'] = 'Basic ' + $cookieStore.get('user');
    var user = User.query();
    console.log("Got " + user);
	$scope.user = user;
}

AdminRootController.$inject = ['$scope', '$resource', '$routeParams', '$cookieStore', '$http', User];

function AdminLoginController($scope, $resource, $routeParams, $cookieStore, $location, User) {

	$scope.user = User.query();

	$scope.login = function(user) {
        var authToken = user.email + ":" + user.password;
        var encodedToken = Base64.encode(authToken);

        $cookieStore.put('user', encodedToken);

        $location.path('/home');
	}
}

AdminLoginController.$inject = ['$scope', '$resource', '$routeParams', '$cookieStore', '$location', User];