'use strict';

angular.module('pruebaApp')
    .factory('Player', function ($resource, DateUtils) {
        return $resource('api/players/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.birthDate = DateUtils.convertLocaleDateFromServer(data.birthDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.birthDate = DateUtils.convertLocaleDateToServer(data.birthDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.birthDate = DateUtils.convertLocaleDateToServer(data.birthDate);
                    return angular.toJson(data);
                }
            }
        });
    });
