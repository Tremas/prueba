'use strict';

angular.module('pruebaApp')
    .factory('Stadium', function ($resource, DateUtils) {
        return $resource('api/stadiums/:id', {}, {
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
