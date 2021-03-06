'use strict';

// Declare app level module which depends on filters, and services
angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives', 'ngResource', 'ngCookies']).
  config(['$routeProvider', '$locationProvider', '$httpProvider', function($routeProvider, $locationProvider, $httpProvider) {
    $routeProvider.when('/', {
        templateUrl: 'partials/root.html',
        controller: TrickCategoryController
    });
    $routeProvider.when('/guide', {
        templateUrl: 'partials/trick_guide.html',
        controller: TrickCategoryController
    });
    $routeProvider.when('/gallery', {
        templateUrl: 'partials/gallery.html',
        controller: TrickCategoryController
    });
    $routeProvider.when('/categories/:category', {
        templateUrl: 'partials/trick_root.html',
        controller: TrickController
    });
    $routeProvider.when('/tricks/:trickId', {
        templateUrl: 'partials/trick_details.html',
        controller: TrickDetailsController
    });
    $routeProvider.otherwise({redirectTo: '/'});

    var interceptor = ['$location', '$q', function($location, $q) {
        function success(response) {
            return response;
        }

        function error(response) {

            if(response.status === 403) {
                $location.path('/login');
                return $q.reject(response);
            }
            else {
                return $q.reject(response);
            }
        }

        return function(promise) {
            return promise.then(success, error);
        }
    }];

    $httpProvider.responseInterceptors.push(interceptor);
  }]);


angular.module('adminApp', ['adminApp.filters', 'adminApp.services', 'adminApp.directives', 'ngResource', 'ngCookies']).
  config(['$routeProvider', '$locationProvider', '$httpProvider', function($routeProvider, $locationProvider, $httpProvider) {
    $routeProvider.when('/home', {
        templateUrl: 'partials/admin/root.html',
        controller: AdminRootController
    });
    $routeProvider.when('/login', {
        templateUrl: 'partials/admin/login.html',
        controller: AdminLoginController
    });
    $routeProvider.when('/categories/:category', {
        templateUrl: 'partials/admin/trickCategories.html',
        controller: AdminTrickCategoryController
    });
    $routeProvider.when('/tricks/:trickId', {
        templateUrl: 'partials/admin/trick_details.html',
        controller: AdminTrickController
    });
    $routeProvider.otherwise({redirectTo: '/home'});

    var interceptor = ['$location', '$q', function($location, $q) {
        function success(response) {
            return response;
        }

        function error(response) {

            if(response.status === 403) {
                $location.path('/login');
                return $q.reject(response);
            }
            else {
                return $q.reject(response);
            }
        }

        return function(promise) {
            return promise.then(success, error);
        }
    }];

    $httpProvider.responseInterceptors.push(interceptor);
  }]);