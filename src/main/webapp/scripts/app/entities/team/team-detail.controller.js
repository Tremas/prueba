'use strict';

angular.module('pruebaApp')
    .controller('TeamDetailController', function ($scope, $rootScope, $stateParams, entity, Team, Player, Stadium, Coach, Partner, Season, Game) {
        $scope.team = entity;
        $scope.load = function (id) {
            Team.get({id: id}, function(result) {
                $scope.team = result;
            });
        };
        var unsubscribe = $rootScope.$on('pruebaApp:teamUpdate', function(event, result) {
            $scope.team = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
