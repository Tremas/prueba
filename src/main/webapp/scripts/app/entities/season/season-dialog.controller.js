'use strict';

angular.module('pruebaApp').controller('SeasonDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Season', 'Team', 'League', 'Game',
        function($scope, $stateParams, $uibModalInstance, entity, Season, Team, League, Game) {

        $scope.season = entity;
        $scope.teams = Team.query();
        $scope.leagues = League.query();
        $scope.games = Game.query();
        $scope.load = function(id) {
            Season.get({id : id}, function(result) {
                $scope.season = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pruebaApp:seasonUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.season.id != null) {
                Season.update($scope.season, onSaveSuccess, onSaveError);
            } else {
                Season.save($scope.season, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
