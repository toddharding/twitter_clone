<%-- Created by IntelliJ IDEA. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en" ng-app="tclone">
<head>
    <meta charset="utf-8">
    <title>My AngularJS App</title>
    <link rel="stylesheet" href="css/app.css"/>
</head>
<body>

    <nav-links></nav-links>



<div ui-view></div>



<!-- In production use:
<script src="//ajax.googleapis.com/ajax/libs/angularjs/x.x.x/angular.min.js"></script>
-->
<script src="lib/underscore.js"></script>
<script src="lib/underscore.string.js"></script>
<script src="lib/angular/angular.js"></script>
<script src="lib/angular/angular-cookies.js"></script>
<script src="lib/angular-ui-router.js"></script>
<script src="lib/angular/angular-route.js"></script>
<script src="js/app.js"></script>
<script src="js/services.js"></script>
<script src="js/controllers.js"></script>
<script src="js/filters.js"></script>
<script src="js/directives.js"></script>
<script src="js/state_provider.js"></script>
</body>
</html>