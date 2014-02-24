'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('tclone.services', [])
    .value('version', '0.1')
    .factory('AuthenticationService', function($http, SessionService){
        'use strict';

        return {
            login: function(user){
                // insert auth code here
                SessionService.currentUser = user;
            },
            isLoggedIn: function(){
                return SessionService.currentUser !== null;
            }
        }
    })
    .factory('SessionService', function(){
        'use strict';
        return{
            currentUser: null
        };
    });
