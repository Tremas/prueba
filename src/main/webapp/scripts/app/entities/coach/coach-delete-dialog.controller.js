'use strict';

angular.module('pruebaApp')
	.controller('CoachDeleteController', function($scope, $uibModalInstance, entity, Coach) {

        $scope.coach = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Coach.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
