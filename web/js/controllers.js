'use strict';

/* Controllers */

angular.module('tclone.controllers', []).
    controller('MyCtrl1', [function () {

    }])
    .controller('MyCtrl2', [function () {

    }])
    .controller('NavBarController', ['$scope', function ($scope) {
        $scope.nav_links = [
            {
                'name': 'about',
                'url': '#/about'
            },
            {
                'name': 'login',
                'url': '#/login'
            }
        ];
    }])
    .controller('AboutController', ['$scope', function ($scope) {
        $scope.about_message = "Twitter Clone";
    }])
    .controller('LoginController', ['$scope', function ($scope) {
        $scope.test_message = "Test page";
    }])
    .controller('TestController', ['$scope', function ($scope) {
        $scope.test_message = "Test page";
    }])
    .controller('LoginFormController', ['$scope', '$http', function($scope, $http){
        $scope.user = {'username': '', 'password':''};
        $scope.test_message = "on start"
        $scope.login = function()
        {
            $scope.test_message = "wat up";
            $http.post('auth').
                success(function(){
                    $scope.test_message = "win";
                }).
                error(function(){
                    $scope.test_message = "fuck";
                });
        };
    }]);