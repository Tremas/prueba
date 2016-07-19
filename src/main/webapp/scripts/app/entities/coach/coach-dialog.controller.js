'use strict';

angular.module('pruebaApp').controller('CoachDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Coach', 'Team',
        function($scope, $stateParams, $uibModalInstance, entity, Coach, Team) {

        $scope.coach = entity;
        $scope.teams = Team.query();
        $scope.load = function(id) {
            Coach.get({id : id}, function(result) {
                $scope.coach = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pruebaApp:coachUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.coach.id != null) {
                Coach.update($scope.coach, onSaveSuccess, onSaveError);
            } else {
                Coach.save($scope.coach, onSaveSuccess, onSaveError);
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
