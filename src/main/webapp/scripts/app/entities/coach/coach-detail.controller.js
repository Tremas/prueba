'use strict';

angular.module('pruebaApp')
    .controller('CoachDetailController', function ($scope, $rootScope, $stateParams, entity, Coach, Team) {
        $scope.coach = entity;
        $scope.load = function (id) {
            Coach.get({id: id}, function(result) {
                $scope.coach = result;
            });
        };
        var unsubscribe = $rootScope.$on('pruebaApp:coachUpdate', function(event, result) {
            $scope.coach = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
