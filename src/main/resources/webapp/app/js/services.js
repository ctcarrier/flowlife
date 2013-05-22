'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('myApp.services', ['ngResource']).
  factory('Trick', function($resource){
            return $resource('/tricks/:_id');
    }).factory('TrickCategory', function($resource){
              return $resource('/api/trickCategories/:_id');
  });
