'use strict';

angular.module('pruebaApp')
	.controller('StadiumDeleteController', function($scope, $uibModalInstance, entity, Stadium) {

        $scope.stadium = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Stadium.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
