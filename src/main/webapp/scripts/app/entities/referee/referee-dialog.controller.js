'use strict';

angular.module('pruebaApp').controller('RefereeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Referee', 'League', 'Game',
        function($scope, $stateParams, $uibModalInstance, entity, Referee, League, Game) {

        $scope.referee = entity;
        $scope.leagues = League.query();
        $scope.games = Game.query();
        $scope.load = function(id) {
            Referee.get({id : id}, function(result) {
                $scope.referee = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pruebaApp:refereeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.referee.id != null) {
                Referee.update($scope.referee, onSaveSuccess, onSaveError);
            } else {
                Referee.save($scope.referee, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForBirthDate = {};

        $scope.datePickerForBirthDate.status = {
            opened: false
        };

        $scope.datePickerForBirthDateOpen = function($event) {
            $scope.datePickerForBirthDate.status.opened = true;
        };
}]);
