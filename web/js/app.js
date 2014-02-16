'use strict';


// Declare app level module which depends on filters, and services
angular.module('tclone', [
  'ngRoute',
  'tclone.filters',
  'tclone.services',
  'tclone.directives',
  'tclone.controllers'
]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/view1', {templateUrl: 'partials/partial1.html', controller: 'MyCtrl1'});
  $routeProvider.when('/view2', {templateUrl: 'partials/partial2.html', controller: 'MyCtrl2'});
  $routeProvider.when('/about', {templateUrl: 'partials/about.html', controller: 'AboutController'});
  $routeProvider.when('/test', {templateUrl: 'partials/test.html', controller: 'TestController'});
  $routeProvider.otherwise({redirectTo: '/view1'});
}]);
