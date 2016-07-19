'use strict';

angular.module('pruebaApp')
    .controller('SeasonDetailController', function ($scope, $rootScope, $stateParams, entity, Season, Team, League, Game) {
        $scope.season = entity;
        $scope.load = function (id) {
            Season.get({id: id}, function(result) {
                $scope.season = result;
            });
        };
        var unsubscribe = $rootScope.$on('pruebaApp:seasonUpdate', function(event, result) {
            $scope.season = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
