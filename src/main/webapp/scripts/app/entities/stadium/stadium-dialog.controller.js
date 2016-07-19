'use strict';

angular.module('pruebaApp').controller('StadiumDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Stadium', 'Team',
        function($scope, $stateParams, $uibModalInstance, entity, Stadium, Team) {

        $scope.stadium = entity;
        $scope.teams = Team.query();
        $scope.load = function(id) {
            Stadium.get({id : id}, function(result) {
                $scope.stadium = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pruebaApp:stadiumUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.stadium.id != null) {
                Stadium.update($scope.stadium, onSaveSuccess, onSaveError);
            } else {
                Stadium.save($scope.stadium, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
