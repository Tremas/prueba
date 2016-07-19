'use strict';

angular.module('pruebaApp').controller('StadisticsDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Stadistics', 'Game', 'Player',
        function($scope, $stateParams, $uibModalInstance, entity, Stadistics, Game, Player) {

        $scope.stadistics = entity;
        $scope.games = Game.query();
        $scope.players = Player.query();
        $scope.load = function(id) {
            Stadistics.get({id : id}, function(result) {
                $scope.stadistics = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('pruebaApp:stadisticsUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.stadistics.id != null) {
                Stadistics.update($scope.stadistics, onSaveSuccess, onSaveError);
            } else {
                Stadistics.save($scope.stadistics, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
