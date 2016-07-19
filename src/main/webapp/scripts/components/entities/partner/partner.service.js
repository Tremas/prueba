'use strict';

angular.module('pruebaApp')
    .factory('Partner', function ($resource, DateUtils) {
        return $resource('api/partners/:id', {}, {
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
