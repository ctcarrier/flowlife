'use strict';

/* Controllers */

function TrickCategoryController($scope, $resource, Trick, TrickCategory) {
	$scope.allTricks = Trick.query({});
	$scope.allTrickCategories = TrickCategory.query({});
	$scope.trick = new Trick({});
}
TrickCategoryController.$inject = ['$scope', '$resource', 'Trick', 'TrickCategory'];

function TrickController($scope, $resource, $routeParams, Trick) {
	$scope.allTricks = Trick.query({category: $routeParams.category});
}

TrickController.$inject = ['$scope', '$resource', '$routeParams', 'Trick'];

function TrickDetailsController($scope, $resource, $routeParams, Trick) {
	$scope.trick = Trick.get({_id: $routeParams.trickId});
}

TrickDetailsController.$inject = ['$scope', '$resource', '$routeParams', 'Trick'];

function AdminRootController($scope, $resource, $routeParams, $cookieStore, $http, Trick, TrickCategory, User) {
    $http.defaults.headers.common['Authorization'] = 'Basic ' + $cookieStore.get('user');
    var user = User.get({});
	$scope.flowUser = user;

    $scope.allTrickCategories = TrickCategory.query({});

    $scope.getTricksByCategory = function(category) {
        $scope.allTricks = Trick.query({category: category.name});
    }

    $scope.saveCategory = function(category) {
        category.href = "/categories/" + category.name.toLowerCase();
        new TrickCategory(category).$save(function(u, headers) {
            $scope.refreshCategories();
        });
    }

    $scope.refreshCategories = function() {
        $scope.allTrickCategories = TrickCategory.query({});
    }
}

AdminRootController.$inject = ['$scope', '$resource', '$routeParams', '$cookieStore', '$http', 'Trick', 'TrickCategory', 'User'];

function AdminLoginController($scope, $resource, $routeParams, $cookieStore, $location, $User) {
    var User = $resource('/api/users/:email');
	$scope.user = User.query();

	$scope.login = function(user) {
        var authToken = user.email + ":" + user.password;
        var encodedToken = Base64.encode(authToken);

        $cookieStore.put('user', encodedToken);

        $location.path('/home');
	}
}

AdminLoginController.$inject = ['$scope', '$resource', '$routeParams', '$cookieStore', '$location', 'User'];

function AdminTrickCategoryController($scope, $resource, $routeParams, $cookieStore, $http, Trick, TrickCategory, User) {
    $http.defaults.headers.common['Authorization'] = 'Basic ' + $cookieStore.get('user');
    var user = User.get({});
    $scope.flowUser = user;
    $scope.currentCategory = $routeParams.category;

    $scope.allTricks = Trick.query({category: $scope.currentCategory});

    $scope.saveTrick = function(trick) {
        trick.category = $scope.currentCategory;
        new Trick(trick).$save(function(u, headers) {
            $scope.refreshTricks();
        });
    }

    $scope.refreshTricks = function() {
        $scope.allTricks = Trick.query({category: $scope.currentCategory});
    }
}

AdminTrickCategoryController.$inject = ['$scope', '$resource', '$routeParams', '$cookieStore', '$http', 'Trick', 'TrickCategory', 'User'];

function AdminTrickController($scope, $resource, $routeParams, $cookieStore, $http, Trick, TrickCategory, User) {
    $http.defaults.headers.common['Authorization'] = 'Basic ' + $cookieStore.get('user');
    var user = User.get({});
    $scope.flowUser = user;
    $scope.currentId = $routeParams.trickId;

    $scope.trick = Trick.get({_id: $scope.currentId});

    $scope.saveTrick = function(trick) {
        trick._id = $scope.currentId;
        new Trick(trick).$save(function(u, headers) {
            $scope.refreshTricks();
        });
    }

    $scope.deleteTrick = function(trick) {
        trick._id = $scope.currentId;
        new Trick(trick).$delete(function(u, headers) {
            $scope.refreshTricks();
        });
    }
}

AdminTrickController.$inject = ['$scope', '$resource', '$routeParams', '$cookieStore', '$http', 'Trick', 'TrickCategory', 'User'];