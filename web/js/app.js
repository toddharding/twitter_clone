'use strict';



/**
 * Object -> String
 * Similar to [url]http://api.jquery.com/jQuery.param/[/url]
 * Source from [url]http://stackoverflow.com/questions/1714786/querystring-encoding-of-a-javascript-object/1714899#1714899[/url]
 *
 * @param object
 * @param [prefix]
 * @returns {string}
 */
angular.extend( angular, {
    toParam: toParam
});

function toParam( object, prefix ) {
    var stack = [];
    var value;
    var key;

    for( key in object ) {
        value = object[ key ];
        key = prefix ? prefix + '[' + key + ']' : key;

        if ( value === null ) {
            value = encodeURIComponent( key ) + '=';
        } else if ( typeof( value ) !== 'object' ) {
            value = encodeURIComponent( key ) + '=' + encodeURIComponent( value );
        } else {
            value = toParam( value, key );
        }

        stack.push( value );
    }

    return stack.join( '&' );
}
/** END **/

// Declare app level module which depends on filters, and services
angular.module('tclone', [
        'ngRoute',
        'ngCookies',
        'tclone.filters',
        'tclone.services',
        'tclone.directives',
        'tclone.controllers',
        'ui.router'
    ])
    /*
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/about', {templateUrl: 'partials/about.html', controller: 'AboutController'});
        $routeProvider.when('/login', {templateUrl: 'partials/login.html', controller: 'LoginController'});
        $routeProvider.when('/app', {templateUrl: 'partials/app.html', controller: 'AppController'});
        $routeProvider.otherwise({redirectTo: '/login'});
    }])
    */
    .config(function ($httpProvider) {

        var logsOutUserOn401 = ['$q', '$location', function ($q, $location) {
            var success = function (response) {
                return response;
            };

            var error = function (response) {
                if (response.status === 401) {
                    //redirect them back to login page
                    $location.path('/login');

                    return $q.reject(response);
                }
                else {
                    return $q.reject(response);
                }
            };

            return function (promise) {
                return promise.then(success, error);
            };
        }];
        $httpProvider.defaults.headers.post  = {'Content-Type': 'application/x-www-form-urlencoded'};
        $httpProvider.defaults.transformRequest = function( data ) {
            return angular.isObject( data ) && String( data ) !== '[object File]' ? angular.toParam( data ) : data;
        };
        $httpProvider.responseInterceptors.push(logsOutUserOn401);
    })
/**
 * Code based  on code by http://arthur.gonigberg.com/2013/06/29/angularjs-role-based-auth/
 */
    .run(function ($rootScope, $location, AuthenticationService) {
        var routesThatDontRequireAuth = ['/login', '/signup'];

        var routeClean = function (route) {
            return _.find(routesThatDontRequireAuth,
                function (noAuthRoute) {
                    return _.str.startsWith(route, noAuthRoute);
                });
        };

        $rootScope.$on('$stateChangeStart', function (ev, to, toParams, from, fromParams) {
            // if route requires auth and user is not logged in
            if (!routeClean($location.url()) && !AuthenticationService.isLoggedIn()) {
                // redirect back to login
                ev.preventDefault();
                $location.path('/login');
            }
        });
    });

