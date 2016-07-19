'use strict';

angular.module('pruebaApp').controller('LeagueDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'League', 'Season', 'Referee',
        function($scope, $stateParams, $uibModalInstance, entity, League, Season, Referee) {

        $scope.league = entity;
        $scope.seasons = Season.query();
        $scope.referees = Referee.query();
        $scope.load = function(id) {
            League.get({id : id}, function(result) {
                $scope.league = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pruebaApp:leagueUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.league.id != null) {
                League.update($scope.league, onSaveSuccess, onSaveError);
            } else {
                League.save($scope.league, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
