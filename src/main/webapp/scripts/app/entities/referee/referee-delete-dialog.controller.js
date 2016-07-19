'use strict';

angular.module('pruebaApp')
	.controller('RefereeDeleteController', function($scope, $uibModalInstance, entity, Referee) {

        $scope.referee = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Referee.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
