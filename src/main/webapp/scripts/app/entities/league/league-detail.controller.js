'use strict';

angular.module('pruebaApp')
    .controller('LeagueDetailController', function ($scope, $rootScope, $stateParams, entity, League, Season, Referee) {
        $scope.league = entity;
        $scope.load = function (id) {
            League.get({id: id}, function(result) {
                $scope.league = result;
            });
        };
        var unsubscribe = $rootScope.$on('pruebaApp:leagueUpdate', function(event, result) {
            $scope.league = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
