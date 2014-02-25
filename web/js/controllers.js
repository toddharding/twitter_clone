'use strict';

/* Controllers */

angular.module('tclone.controllers', [])
    .controller('NavBarController', ['$scope', 'AuthenticationService', 'Globals', function ($scope, AuthenticationService, Globals) {
        $scope.authService = AuthenticationService;
        $scope.globVars = Globals;
        $scope._user = null;
        $scope.isUserLoggedIn = false;
        $scope.$watch('globVars.getUser()', function (user) {
            console.log("Watch User: ", user);
            $scope._user = user;
            if ($scope._user != null) {
                $scope.isUserLoggedIn = true
            }
            else {
                $scope.isUserLoggedIn = false;
            }
        })

        $scope.nav_links = [
            {
                'name': 'home',
                'url': '#/app',
                'conditional': $scope.isUserLoggedIn


            },
            {
                'name': 'login',
                'url': '#/login',
                'conditional': !$scope.isUserLoggedIn
            },
            {
                'name': 'logout',
                'url': '#/logout',
                'conditional': $scope.isUserLoggedIn
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
    .controller('LoginFormController', ['$scope', '$http', 'AuthenticationService', 'Auth', 'Globals', '$location', function ($scope, $http, AuthenticationService, Auth, Globals, $location) {
        $scope.user = {'username': '', 'password': ''};
        $scope.test_message = ""
        $scope.login = function () {
            Auth.setCredentials($scope.user.username, $scope.user.password);
            $scope.test_message = "logging in";
            $http.post('auth').
                success(function (data) {
                    $scope.test_message = "logged in";
                    $scope.user.id = data.id;
                    AuthenticationService.login({username: $scope.user.username, id: $scope.user.id});
                    Globals.setUser({name: $scope.user.username, id: $scope.user.id});
                    $location.path("/app");
                }).
                error(function (data) {
                    $scope.test_message = "Incorrect Username or Password";
                    $scope.user = null;
                    Auth.clearCredentials();
                    Globals.setUser(null);
                });
        };
    }])
    .controller('LogoutController', ['$scope', '$http', '$location', 'AuthenticationService', 'Auth', 'Globals', function ($scope, $http, $location, AuthenticationService, Auth, Globals) {
        $scope.logout = function () {
            AuthenticationService.logout();
            Auth.clearCredentials();
            $location.path("/login");
            Globals.setUser(null);
        };
    }])
    .controller('AppController', ['$scope', 'SessionService', function ($scope, SessionService) {
        $scope.test_message = "App page";
        $scope.user = SessionService.currentUser;
    }])
    .controller('HomeUserDetailsController', ['$scope', '$http', '$location', 'AuthenticationService', 'Auth', 'Globals', function ($scope, $http, $location, AuthenticationService, Auth, Globals) {
        $scope.user = {};
        $http.get('/user/' + Globals.getUser().id)
            .success(function (data) {
                $scope.user = data;
            })
            .error(function () {
                $scope.user = null;
            });
        $scope.tweet = "";
    }])
    .controller('HomeFeedController', ['$scope', '$http', '$location', 'AuthenticationService', 'Auth', 'Globals', function ($scope, $http, $location, AuthenticationService, Auth, Globals) {
        $scope.tweets = {};
        $scope.title = "Feed";
    }])
    .controller('PublicUserProfileController', ['$scope', '$http', '$location', 'AuthenticationService', 'Auth', 'Globals', '$stateParams',
        function ($scope, $http, $location, AuthenticationService, Auth, Globals, $stateParams) {
            $scope.selectedUser = {};
            $http.get('/user/' + $stateParams.username)
                .success(function (data) {
                    $scope.selectedUser = data;
                })
                .error(function () {
                    $scope.selectedUser = null;
                });
        }])
    .controller('SelectedUserDetailsController', ['$scope', '$http', '$location', 'AuthenticationService', 'Auth', 'Globals', '$stateParams',
        function ($scope, $http, $location, AuthenticationService, Auth, Globals, $stateParams) {
            $scope.selectedUser = {};
            $http.get('/user/' + $stateParams.username)
                .success(function (data) {
                    $scope.selectedUser = data;
                })
                .error(function () {
                    $scope.selectedUser = null;
                });
        }])
    .controller('SelectedUserTweetsController', ['$scope', '$http', '$location', 'AuthenticationService', 'Auth', 'Globals', '$stateParams',
        function ($scope, $http, $location, AuthenticationService, Auth, Globals, $stateParams) {
            $scope.tweets = {};
            $scope.title = "Feed";
            $http.get('/tweet/' + $stateParams.username)
                .success(function (data) {
                    $scope.tweets = data;
                })
                .error(function () {
                    $scope.tweets = null;
                });
        }]);