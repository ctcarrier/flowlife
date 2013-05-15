'use strict';

// Declare app level module which depends on filters, and services
angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives', 'ngResource']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/new_mesh', {templateUrl: 'partials/mesh_form.html', controller: MeshController});
    $routeProvider.when('/view1', {templateUrl: 'partials/partial2.html', controller: TrickCategoryController});
    $routeProvider.when('/tricks/:category', {templateUrl: 'partials/trick_root.html', controller: TrickController});
//    $routeProvider.when('/hoop', {templateUrl: 'partials/hoop.html', controller: TrickController});
//    $routeProvider.when('/staff', {templateUrl: 'partials/staff.html', controller: TrickController});
//    $routeProvider.when('/view2', {templateUrl: 'partials/partial2.html', controller: MyCtrl2});
    $routeProvider.otherwise({redirectTo: '/view1'});
  }]);

