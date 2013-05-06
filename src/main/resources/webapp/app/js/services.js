'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('myApp.services', []).
  value('version', '0.1');

angular.module('meshServices', ['ngResource']).
    factory('Mesh', function($resource){
  		return $resource('/api/meshes/:_id');
	}).
  factory('Trick', function($resource){
                return $resource('/tricks/:_id');
        });
