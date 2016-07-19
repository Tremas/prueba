'use strict';

angular.module('pruebaApp')
    .controller('StadisticsDetailController', function ($scope, $rootScope, $stateParams, entity, Stadistics, Game, Player) {
        $scope.stadistics = entity;
        $scope.load = function (id) {
            Stadistics.get({id: id}, function(result) {
                $scope.stadistics = result;
            });
        };
        var unsubscribe = $rootScope.$on('pruebaApp:stadisticsUpdate', function(event, result) {
            $scope.stadistics = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
