'use strict';

angular.module('pruebaApp').controller('PlayerDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Player', 'Team', 'Stadistics',
        function($scope, $stateParams, $uibModalInstance, entity, Player, Team, Stadistics) {

        $scope.player = entity;
        $scope.teams = Team.query();
        $scope.stadisticss = Stadistics.query();
        $scope.load = function(id) {
            Player.get({id : id}, function(result) {
                $scope.player = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pruebaApp:playerUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.player.id != null) {
                Player.update($scope.player, onSaveSuccess, onSaveError);
            } else {
                Player.save($scope.player, onSaveSuccess, onSaveError);
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
