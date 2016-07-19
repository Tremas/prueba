'use strict';

angular.module('pruebaApp')
    .factory('League', function ($resource, DateUtils) {
        return $resource('api/leagues/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
