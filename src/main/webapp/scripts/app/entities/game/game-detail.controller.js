'use strict';

angular.module('pruebaApp')
    .controller('GameDetailController', function ($scope, $rootScope, $stateParams, entity, Game, Season, Referee, Team, Stadistics) {
        $scope.game = entity;
        $scope.load = function (id) {
            Game.get({id: id}, function(result) {
                $scope.game = result;
            });
        };
        var unsubscribe = $rootScope.$on('pruebaApp:gameUpdate', function(event, result) {
            $scope.game = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
