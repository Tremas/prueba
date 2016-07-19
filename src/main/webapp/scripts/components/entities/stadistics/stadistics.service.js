'use strict';

angular.module('pruebaApp')
    .factory('Stadistics', function ($resource, DateUtils) {
        return $resource('api/stadisticss/:id', {}, {
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
