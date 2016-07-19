'use strict';

angular.module('pruebaApp').controller('PartnerDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Partner', 'Team',
        function($scope, $stateParams, $uibModalInstance, entity, Partner, Team) {

        $scope.partner = entity;
        $scope.teams = Team.query();
        $scope.load = function(id) {
            Partner.get({id : id}, function(result) {
                $scope.partner = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pruebaApp:partnerUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.partner.id != null) {
                Partner.update($scope.partner, onSaveSuccess, onSaveError);
            } else {
                Partner.save($scope.partner, onSaveSuccess, onSaveError);
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
