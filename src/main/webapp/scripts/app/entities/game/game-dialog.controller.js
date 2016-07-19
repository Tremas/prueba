'use strict';

angular.module('pruebaApp').controller('GameDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Game', 'Season', 'Referee', 'Team', 'Stadistics',
        function($scope, $stateParams, $uibModalInstance, entity, Game, Season, Referee, Team, Stadistics) {

        $scope.game = entity;
        $scope.seasons = Season.query();
        $scope.referees = Referee.query();
        $scope.teams = Team.query();
        $scope.stadisticss = Stadistics.query();
        $scope.load = function(id) {
            Game.get({id : id}, function(result) {
                $scope.game = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pruebaApp:gameUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.game.id != null) {
                Game.update($scope.game, onSaveSuccess, onSaveError);
            } else {
                Game.save($scope.game, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDate = {};

        $scope.datePickerForDate.status = {
            opened: false
        };

        $scope.datePickerForDateOpen = function($event) {
            $scope.datePickerForDate.status.opened = true;
        };
}]);
