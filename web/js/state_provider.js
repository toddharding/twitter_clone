/**
 * Created by Todd on 24/02/14.
 */
angular.module("tclone").config(function ($stateProvider, $urlRouterProvider){
    'use strict';

    $urlRouterProvider.otherwise('/login');

    $stateProvider
        .state('login', {
            url: '/login',
            templateUrl: 'partials/login.html',
            controller: 'LoginController'
        })

        .state('app', {
            url: '/app',
            templateUrl: 'partials/app.html',
            controller: 'AppController'
        })

        .state('about', {
            url: '/about',
            templateUrl: 'partials/about.html',
            controller: 'AppController'
        })

        .state('logout', {
            url:'/logout',
            templateUrl: 'partials/logout.html',
            controller: 'LogoutController'
        })

});