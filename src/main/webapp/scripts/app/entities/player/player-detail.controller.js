'use strict';

angular.module('pruebaApp')
    .controller('PlayerDetailController', function ($scope, $rootScope, $stateParams, entity, Player, Team, Stadistics) {
        $scope.player = entity;
        $scope.load = function (id) {
            Player.get({id: id}, function(result) {
                $scope.player = result;
            });
        };
        var unsubscribe = $rootScope.$on('pruebaApp:playerUpdate', function(event, result) {
            $scope.player = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
