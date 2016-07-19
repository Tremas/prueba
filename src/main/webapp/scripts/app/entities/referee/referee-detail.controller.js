'use strict';

angular.module('pruebaApp')
    .controller('RefereeDetailController', function ($scope, $rootScope, $stateParams, entity, Referee, League, Game) {
        $scope.referee = entity;
        $scope.load = function (id) {
            Referee.get({id: id}, function(result) {
                $scope.referee = result;
            });
        };
        var unsubscribe = $rootScope.$on('pruebaApp:refereeUpdate', function(event, result) {
            $scope.referee = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
