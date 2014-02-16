'use strict';
var tclone = angular.module('tclone', []);

tclone.controller('NavbarCtrl', ['$scope', function ($scope) {
    $scope.navs = [
        {
            'name': 'about',
            'href': '#/about'
        },
        {
            'name': 'test',
            'href': '#/test'
        }
    ];
}]);

tclone.controller('AboutCtrl', ['$scope', function ($scope) {
    $scope.about_message = "this is the test about message";
}]);

tclone.controller('TestCtrl', ['$scope', function ($scope) {

}]);


