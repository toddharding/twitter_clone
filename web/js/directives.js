'use strict';

/* Directives */


angular.module('tclone.directives', []).
  directive('appVersion', ['version', function(version) {
    return function(scope, elm, attrs) {
      elm.text(version);
    };
  }])
    .directive('navLinks', function(){
        return{
            restrict: 'E',
            transclude: true,
            templateUrl: 'templates/nav_links.html'
        };
    })
