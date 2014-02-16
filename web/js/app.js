'use strict';


// Declare app level module which depends on filters, and services
var tclone = angular.module('tclone', [
    'ngRoute',
    'ui.bootstrap',
    'tclone.filters',
    'tclone.services',
    'tclone.directives',
    'tclone.controllers'
]);

tclone.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.
        when('/about', {
            templateUrl: 'partials/about.html',
            controller: 'AboutCtrl'
        }).
        when('/test', {
            templateUrl: 'partials/test.html',
            controller: 'TestCtrl'
        }).
        otherwise({
            redirectTo: '/about'
        })
}]);
