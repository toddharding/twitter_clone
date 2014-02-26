/**
 * Created by Todd on 24/02/14.
 */
angular.module("tclone").config(function ($stateProvider, $urlRouterProvider){
    'use strict';

    $urlRouterProvider.otherwise('/app');

    $stateProvider
        .state('login', {
            url: '/login',
            templateUrl: 'partials/login.html',
            controller: 'LoginController'
        })
        .state('signup', {
            url: '/signup',
            templateUrl: 'partials/signup.html',
            controller: 'SignupController'
        })

        .state('app', {
            url: '/app',
            templateUrl: 'partials/app.html',
            controller: 'AppController'
        })
        .state('user', {
            url: '/user/:username',
            templateUrl: 'partials/public_user_profile.html',
            controller: 'PublicUserProfileController'
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