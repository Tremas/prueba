'use strict';

angular.module('pruebaApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


