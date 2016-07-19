'use strict';

angular.module('pruebaApp')
    .controller('StadiumDetailController', function ($scope, $rootScope, $stateParams, entity, Stadium, Team) {
        $scope.stadium = entity;
        $scope.load = function (id) {
            Stadium.get({id: id}, function(result) {
                $scope.stadium = result;
            });
        };
        var unsubscribe = $rootScope.$on('pruebaApp:stadiumUpdate', function(event, result) {
            $scope.stadium = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
