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
                'name': 'test',
                'url': '#/test'
            }
        ];
    }])
    .controller('AboutController', ['$scope', function ($scope) {
        $scope.about_message = "Twitter Clone";
    }])
    .controller('TestController', ['$scope', function ($scope) {
        $scope.test_message = "Test page";
    }]);