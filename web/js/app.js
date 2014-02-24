'use strict';


// Declare app level module which depends on filters, and services
angular.module('tclone', [
        'ngRoute',
        'tclone.filters',
        'tclone.services',
        'tclone.directives',
        'tclone.controllers',
        'ui.router'
    ])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/about', {templateUrl: 'partials/about.html', controller: 'AboutController'});
        $routeProvider.when('/login', {templateUrl: 'partials/login.html', controller: 'LoginController'});
        $routeProvider.when('/app', {templateUrl: 'partials/app.html', controller: 'AppController'});
        $routeProvider.otherwise({redirectTo: '/login'});
    }])
/**
 * Code based on code by http://arthur.gonigberg.com/2013/06/29/angularjs-role-based-auth/
 */
    .run(function ($rootScope, $location, AuthenticationService) {
        var routesThatDontRequireAuth = ['/login'];

        var routeClean = function (route) {
            return _.find(routesThatDontRequireAuth,
                function (noAuthRoute) {
                    return _.str.startsWith(route, noAuthRoute);
                });
        };

        $rootScope.$on('$routeChangeStart', function (event, next, current) {
            // if route requires auth and user is not logged in
            if (!routeClean($location.url()) && !AuthenticationService.isLoggedIn()) {
                // then redirect back to the login page
                $location.path('login');
            }
        });
    });
